package com.hhtc.dialer.data.tradition;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;

import com.hhtc.dialer.utils.LogUtil;

import java.util.Objects;

public class CallLogObserver extends ContentObserver {

    private static final String TAG = "CallLogObserver";

    private Context context;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public CallLogObserver(Handler handler, Context context) {
        super(handler);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onChange(boolean selfChange) {
        LogUtil.i(TAG, "onChange: " + selfChange);
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, CallLogQuery.getProjection(), null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (Objects.nonNull(cursor)) {
            TraditionSynchronise.getInstance().analysisCursor(cursor, context);
            Objects.requireNonNull(cursor).close();
        }
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        LogUtil.i(TAG, "onChange: " + selfChange + " uri:" + uri);
    }
}
