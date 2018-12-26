package com.hhtc.dialer.userprovider;

import android.annotation.SuppressLint;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.UserInfo;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.Iterator;
import java.util.Objects;


/**
 * 用户信息提供者
 */
public class UserProvider extends ContentProvider {

    private static final String TAG = "UserProvider";

    public static final String AUTHORITY = "com.hhtc.dialer.user.provider";

    public static final int USER_CODE = 1;

    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, "user", USER_CODE);

    }


    private Repository repository;

    private Context mContext;

    @Override
    public boolean onCreate() {
        this.mContext = getContext();
        repository = Injection.provideTasksRepository(Objects.requireNonNull(getContext()));
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        @SuppressLint("RestrictedApi") RoomSQLiteQuery query = RoomSQLiteQuery.acquire("SELECT * FROM user_name", 0);
        return repository.getDatabase().query(query);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        TelephoneThreadDispatcher.getInstance().execute(() -> {
            Iterator<String> iterator = Objects.requireNonNull(values).keySet().iterator();
            UserInfo info = new UserInfo();
            while (iterator.hasNext()) {
                String name = iterator.next();
                if (Objects.equals("user_name", name)) {
                    info.setName(values.getAsString(name));
                }
            }

            if (Objects.nonNull(info.getName())) {
                repository.getDatabase().getOpenHelper().getWritableDatabase().execSQL("delete from user_name");
                repository.getDatabase().getUserInfoDao().insertUserInfo(info);
                mContext.getContentResolver().notifyChange(uri, null);
            }
        },TelephoneThreadDispatcher.DispatcherType.WORK);

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        repository.getDatabase().getOpenHelper().getWritableDatabase().execSQL("delete from user_name");
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        UserInfo info = repository.getDatabase().getUserInfoDao().loadUserInfoWDF();
        String user_name = Objects.requireNonNull(values).getAsString("user_name");
        info.setName(user_name);
        repository.getDatabase().getUserInfoDao().updateUserInfo(info);
        return 0;
    }
}
