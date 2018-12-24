package com.hhtc.dialer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hhtc.dialer.utils.LogUtil;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.e(TAG, "onReceive =======  ");
    }
}