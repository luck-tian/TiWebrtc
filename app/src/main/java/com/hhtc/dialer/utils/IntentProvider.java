package com.hhtc.dialer.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.telecom.PhoneAccount;
import android.telephony.TelephonyManager;

import java.util.List;

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

    public static IntentProvider getTelegramProvider(String remoteName) {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(intentUnits.ACTION_TELEGRAM,
                        new Uri.Builder()
                                .scheme("telegram")
                                .query(remoteName)
                                .build());
            }
        };
    }

    public static IntentProvider getCallProvider(String remoteName) {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                return new Intent(intentUnits.ACTION_SIP_CALL,
                        new Uri.Builder()
                                .scheme("call")
                                .query(remoteName)
                                .build());
            }
        };
    }

    public static IntentProvider getCallServiceProvider() {
        return new IntentProvider() {
            @Override
            public Intent getIntent(Context context) {
                Intent intent = new Intent(intentUnits.ACTION_TELEPHONE,
                        new Uri.Builder()
                                .scheme("service")
                                .build());
                return createExplicitFromImplicitIntent(context,intent);

            }
        };
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    public abstract Intent getIntent(Context context);
}
