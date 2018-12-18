package com.hhtc.dialer.data.tradition;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadUniqueCallback;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.LogUtil;

import java.util.Objects;

public class TraditionSynchronise {

    private static final String TAG = "TraditionSynchronise";

    private static final Object LOCK = new Object();

    private static TraditionSynchronise instance;

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

    @RequiresPermission(value = Manifest.permission.READ_CALL_LOG)
    public void startSynchronization(Context context) {
        //同步数据请求权限
        TelephoneThreadDispatcher.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = context.getContentResolver();
                Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, CallLogQuery.getProjection(), null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
                if (Objects.nonNull(cursor)) {
                    analysisCursor(cursor, context);
                    Objects.requireNonNull(cursor).close();
                }

                context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, new CallLogObserver(new Handler(Looper.getMainLooper()), context));

            }
        }, TelephoneThreadDispatcher.DispatcherType.IO);

    }

    /**
     * 解析数据
     *
     * @param cursor
     */
    public void analysisCursor(Cursor cursor, Context context) {
        cursor.moveToFirst();
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

}
