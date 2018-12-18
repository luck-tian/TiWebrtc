package com.hhtc.dialer.data.favorite;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;

import com.hhtc.dialer.data.tradition.TraditionSynchronise;

import java.util.Objects;

public class FavoriteObserver extends ContentObserver {

    private Context context;

    public FavoriteObserver(Handler handler, Context context) {
        super(handler);
        this.context=context.getApplicationContext();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor favorite = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (Objects.nonNull(favorite)) {
            TraditionSynchronise.getInstance().analysisContactCursor(favorite, context);
            Objects.requireNonNull(favorite).close();
        }
    }
}
