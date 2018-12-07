package com.hhtc.dialer.permission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.hhtc.dialer.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ADD_VOICEMAIL;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.MODIFY_PHONE_STATE;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_VOICEMAIL;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_VOICEMAIL;

public class PermissionsUtil {


    private static final String PERMISSION_PREFERENCE = "dialer_permissions";
    private static final String CEQUINT_PERMISSION = "com.cequint.ecid.CALLER_ID_LOOKUP";


    public static final List<String> allPhoneGroupPermissionsUsedInDialer =
            Collections.unmodifiableList(
                    Arrays.asList(
                            READ_CALL_LOG,
                            WRITE_CALL_LOG,
                            READ_PHONE_STATE,
                            MODIFY_PHONE_STATE,
                            SEND_SMS,
                            CALL_PHONE,
                            RECORD_AUDIO,
                            ADD_VOICEMAIL,
                            WRITE_VOICEMAIL,
                            READ_VOICEMAIL));

    public static final List<String> allContactsGroupPermissionsUsedInDialer =
            Collections.unmodifiableList(Arrays.asList(READ_CONTACTS, WRITE_CONTACTS));

    public static final List<String> allLocationGroupPermissionsUsedInDialer =
            Collections.unmodifiableList(Arrays.asList(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION));

    public static boolean hasPhonePermissions(Context context) {
        return hasPermission(context, permission.CALL_PHONE);
    }

    public static boolean hasReadPhoneStatePermissions(Context context) {
        return hasPermission(context, permission.READ_PHONE_STATE);
    }

    public static boolean hasContactsReadPermissions(Context context) {
        return hasPermission(context, permission.READ_CONTACTS);
    }

    public static boolean hasLocationPermissions(Context context) {
        return hasPermission(context, permission.ACCESS_FINE_LOCATION);
    }

    public static boolean hasCameraPermissions(Context context) {
        return hasPermission(context, permission.CAMERA);
    }

    public static boolean hasMicrophonePermissions(Context context) {
        return hasPermission(context, RECORD_AUDIO);
    }

    public static boolean hasCallLogReadPermissions(Context context) {
        return hasPermission(context, permission.READ_CALL_LOG);
    }

    public static boolean hasCallLogWritePermissions(Context context) {
        return hasPermission(context, permission.WRITE_CALL_LOG);
    }

    public static boolean hasCequintPermissions(Context context) {
        return hasPermission(context, CEQUINT_PERMISSION);
    }

    public static boolean hasReadVoicemailPermissions(Context context) {
        return hasPermission(context, permission.READ_VOICEMAIL);
    }

    public static boolean hasWriteVoicemailPermissions(Context context) {
        return hasPermission(context, permission.WRITE_VOICEMAIL);
    }

    public static boolean hasAddVoicemailPermissions(Context context) {
        return hasPermission(context, permission.ADD_VOICEMAIL);
    }

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }


    public static boolean isFirstRequest(Context context, String permission) {
        return context
                .getSharedPreferences(PERMISSION_PREFERENCE, Context.MODE_PRIVATE)
                .getBoolean(permission, true);
    }


    public static void permissionRequested(Context context, String permission) {
        context
                .getSharedPreferences(PERMISSION_PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(permission, false)
                .apply();
    }


    public static void registerPermissionReceiver(
            Context context, BroadcastReceiver receiver, String permission) {
        LogUtil.i("PermissionsUtil.registerPermissionReceiver", permission);
        final IntentFilter filter = new IntentFilter(permission);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public static void unregisterPermissionReceiver(Context context, BroadcastReceiver receiver) {
        LogUtil.i("PermissionsUtil.unregisterPermissionReceiver", null);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public static void notifyPermissionGranted(Context context, String permission) {
        LogUtil.i("PermissionsUtil.notifyPermissionGranted", permission);
        final Intent intent = new Intent(permission);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    @NonNull
    public static String[] getPermissionsCurrentlyDenied(
            @NonNull Context context, @NonNull List<String> permissionsList) {
        List<String> permissionsCurrentlyDenied = new ArrayList<>();
        for (String permission : permissionsList) {
            if (!hasPermission(context, permission)) {
                permissionsCurrentlyDenied.add(permission);
            }
        }
        return permissionsCurrentlyDenied.toArray(new String[permissionsCurrentlyDenied.size()]);
    }

}