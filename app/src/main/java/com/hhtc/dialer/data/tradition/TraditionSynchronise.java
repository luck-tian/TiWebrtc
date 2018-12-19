package com.hhtc.dialer.data.tradition;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;
import android.util.Log;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadUniqueCallback;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.data.favorite.FavoriteObserver;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.LogUtil;

import java.util.ArrayList;
import java.util.Objects;

import static android.provider.ContactsContract.Contacts.Entity.RAW_CONTACT_ID;
import static com.hhtc.dialer.data.bean.RecentCallLog.TRADITIONAL;

public class TraditionSynchronise {

    private static final String TAG = "TraditionSynchronise";

    private static final Object LOCK = new Object();

    private static TraditionSynchronise instance;

    private static Context context;

    private TraditionSynchronise() {

    }

    public static TraditionSynchronise getInstance() {
        synchronized (LOCK) {
            if (Objects.isNull(instance)) {
                instance = new TraditionSynchronise();
            }
            return instance;
        }
    }

    @RequiresPermission(allOf = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS})
    public void startSynchronization(Context context) {
        //同步数据请求权限
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            this.context = context.getApplicationContext();

            //同步通话记录
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, CallLogQuery.getProjection(), null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            if (Objects.nonNull(cursor)) {
                analysisCallLogCursor(cursor, context);
                Objects.requireNonNull(cursor).close();
            }
            context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, new CallLogObserver(new Handler(Looper.getMainLooper()), context));


            //同步收藏数据
            Cursor favorite = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (Objects.nonNull(favorite)) {
                analysisContactCursor(favorite, context);
                Objects.requireNonNull(favorite).close();
            }

            context.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new FavoriteObserver(new Handler(Looper.getMainLooper()), context));
        }, TelephoneThreadDispatcher.DispatcherType.IO);

    }


    /**
     * 解析数据
     *
     * @param cursor
     */
    public void analysisCallLogCursor(Cursor cursor, Context context) {
        while (cursor.moveToNext()) {
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));//获取通话类型：1.呼入2.呼出3.未接
            String cachedName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));//获取联系人的名字
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));//获取联系人的电话号码
            long data = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));//获取通话日期
            String photoUri = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI));//获取通话日期

            RecentCallLog recentCallLog = new RecentCallLog();
            recentCallLog.setCallType(type);
            if (TextUtils.isEmpty(cachedName)) {
                recentCallLog.setName(number);
            } else {
                recentCallLog.setName(cachedName);
            }
            recentCallLog.setTel(number);
            recentCallLog.setCallTime(data);
            recentCallLog.setPhoto(photoUri);
            recentCallLog.setTelOrVideo(RecentCallLog.TEL_TYPE);
            recentCallLog.setTradition(true);

            //查询数据库是否含有该记录
            Injection.provideTasksRepository(context).loadCallLogTradition(data, type, new LoadUniqueCallback<RecentCallLog>() {
                @Override
                public void onTasksLoaded(RecentCallLog data) {
                    //存在该条记录则放弃操作
                    LogUtil.i(TAG, "onTasksLoaded: recentCallLog 存在 " + recentCallLog.toString());
                }

                @Override
                public void onDataNotAvailable() {
                    //不存在则插入
                    Injection.provideTasksRepository(context).insertRecent(recentCallLog);
                    LogUtil.i(TAG, "onDataNotAvailable: recentCallLog 不存在 " + recentCallLog.toString());
                }
            });
        }

    }

    /**
     * 解析favorite
     *
     * @param cursor
     */
    public void analysisContactCursor(Cursor cursor, Context context) {

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            boolean starred = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)) != 0;

            //该联系人为收藏联系人
            long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);

            int columnIndex = cursor.getColumnIndex(RAW_CONTACT_ID);
            LogUtil.i(TAG, "analysisContactCursor: columnIndex" + columnIndex);

            while (Objects.requireNonNull(phones).moveToNext()) {
                String phoneNumber = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                DialerContact dialerContact = new DialerContact();
                dialerContact.setFavorite(starred);
                dialerContact.setVideo(name);
                dialerContact.setTraditionId(contactId);
                dialerContact.setName(name);
                dialerContact.setMessage(name);
                dialerContact.setEmail(name);
                dialerContact.setTel(phoneNumber);
                dialerContact.setType(TRADITIONAL);

                Injection.provideTasksRepository(context).loadContactLiveTradition(contactId, new LoadUniqueCallback<DialerContact>() {
                    @Override
                    public void onTasksLoaded(DialerContact data) {
                        //存在该条记录则放弃操作
                        LogUtil.i(TAG, "onTasksLoaded: dialerContact 存在 " + data.toString());
                        dialerContact.setId(data.getId());
                        Injection.provideTasksRepository(context).insertContact(dialerContact);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        Injection.provideTasksRepository(context).insertContact(dialerContact);
                        LogUtil.i(TAG, "onDataNotAvailable: dialerContact 不存在 " + dialerContact.toString());
                    }
                });
                break;
            }
            phones.close();

        }

    }

    /**
     * 修改电话联系人
     *
     * @param contact
     */
    public static void changeContact(DialerContact contact) {
        if (Objects.nonNull(context)) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
            ContentValues values = new ContentValues();
            values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName());
            Uri uri = ContactsContract.Data.CONTENT_URI;
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(uri)
                    .withSelection(ContactsContract.Data.RAW_CONTACT_ID + " =? AND " + ContactsContract.Data.MIMETYPE + "=?",
                            new String[]{contact.getTraditionId() + "", ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                    .withYieldAllowed(true);
            builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName());
            ops.add(builder.build());
            try {
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                LogUtil.i(TAG, "changeContact: " + Log.getStackTraceString(e));
            }
            ops.clear();


        }
    }


    /**
     * 删除联系人
     *
     * @param contact
     */
    public static void deleteContact(DialerContact contact) {
        if (Objects.nonNull(context)) {
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID},"display_name=?", new String[]{contact.getName()}, null);
            if(Objects.requireNonNull(cursor).moveToFirst()){
                int id = cursor.getInt(0);
                resolver.delete(uri, "display_name=?", new String[]{contact.getName()});
                uri = Uri.parse("content://com.android.contacts/data");
                resolver.delete(uri, "raw_contact_id=?", new String[]{id+""});
            }

        }
    }

    public static Context getContext() {
        return context;
    }
}
