package com.hhtc.dialer.call.service;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import java.util.function.Consumer;

public class UserContentObserver extends ContentObserver {


    private Consumer<Boolean> changeUser;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public UserContentObserver(Handler handler, Consumer<Boolean> changeUser) {
        super(handler);
        this.changeUser = changeUser;
    }


    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
    }

    @Override
    public void onChange(boolean selfChange) {
        changeUser.accept(selfChange);
    }
}
