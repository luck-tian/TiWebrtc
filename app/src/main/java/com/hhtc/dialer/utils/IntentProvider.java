package com.hhtc.dialer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.PhoneAccount;
import android.telephony.TelephonyManager;

/**
 * Intent 提供者
 */
public abstract class IntentProvider {

    public static final String PACKAGE_NAME = "com.google.android.apps.tachyon";

    private static final String TAG = IntentProvider.class.getSimpleName();


    /**
     * 调用play
     *
     * @return
     */
    public static IntentProvider getInstallDuoIntentProvider() {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(
                        Intent.ACTION_VIEW,
                        new Uri.Builder()
                                .scheme("https")
                                .authority("play.google.com")
                                .appendEncodedPath("store/apps/details")
                                .appendQueryParameter("id", PACKAGE_NAME)
                                .appendQueryParameter(
                                        "referrer",
                                        "utm_source=dialer&utm_medium=text&utm_campaign=product") // This string is from
                                .build());
            }
        };
    }

    public static IntentProvider getContactAddOrEditProvider(String action, long id) {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(intentUnits.ACTION_ADD_CONTACT,
                        new Uri.Builder()
                                .scheme(action)
                                .query(String.valueOf(id))
                                .authority(context.getPackageName())
                                .build());
            }
        };
    }

    public static IntentProvider getContactShowProvider(long id) {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(intentUnits.ACTION_SHOW_CONTACT,
                        new Uri.Builder()
                                .scheme("show")
                                .query(String.valueOf(id))
                                .build());
            }
        };
    }

    public static IntentProvider getPlateProvider() {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(intentUnits.ACTION_PLATE,
                        new Uri.Builder()
                                .scheme("plate")
                                .build());
            }
        };
    }

    public static IntentProvider getTraditionProvider(String number) {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(Intent.ACTION_CALL,
                        Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null));
            }
        };
    }


    public static boolean isTelephonyCalling(Context context) {
        boolean result = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_OFFHOOK == telephonyManager.getCallState() || TelephonyManager.CALL_STATE_RINGING == telephonyManager.getCallState()) {
            result = true;
        }
        return result;
    }

    public static IntentProvider getSendMessageProvider(String name) {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("smsto", name, null));
            }
        };
    }

    public static IntentProvider getSendMailProvider(String name) {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", name, null));
            }
        };
    }

    public static IntentProvider getStartMailProvider() {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(Intent.ACTION_VIEW,
                        Uri.fromParts("content", "ui.email.android.com/view/mailbox", null));
            }
        };
    }

    public abstract Intent getIntent(Context context);
}
