package com.hhtc.dialer.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

public class LogUtil {

    public static final String TAG = "Dialer";
    private static final String SEPARATOR = " - ";

    private LogUtil() {
    }


    public static void v(@NonNull String tag, @Nullable String msg, @Nullable Object... args) {
        println(android.util.Log.VERBOSE, TAG, tag, msg, args);
    }


    public static void d(@NonNull String tag, @Nullable String msg, @Nullable Object... args) {
        println(android.util.Log.DEBUG, TAG, tag, msg, args);
    }


    public static void i(@NonNull String tag, @Nullable String msg, @Nullable Object... args) {
        println(android.util.Log.INFO, TAG, tag, msg, args);
    }


    public static void enterBlock(String tag) {
        println(android.util.Log.INFO, TAG, tag, "enter");
    }


    public static void w(@NonNull String tag, @Nullable String msg, @Nullable Object... args) {
        println(android.util.Log.WARN, TAG, tag, msg, args);
    }


    public static void e(@NonNull String tag, @Nullable String msg, @Nullable Object... args) {
        println(android.util.Log.ERROR, TAG, tag, msg, args);
    }

    public static void e(@NonNull String tag, @Nullable String msg, @NonNull Throwable throwable) {
        if (!TextUtils.isEmpty(msg)) {
            println(
                    android.util.Log.ERROR,
                    TAG,
                    tag,
                    msg + "\n" + android.util.Log.getStackTraceString(throwable));
        }
    }


    public static String sanitizePii(@Nullable Object object) {
        if (object == null) {
            return "null";
        }
        if (isDebugEnabled()) {
            return object.toString();
        }
        return "Redacted-" + object.toString().length() + "-chars";
    }

    public static char sanitizeDialPadChar(char ch) {
        if (isDebugEnabled()) {
            return ch;
        }
        if (is12Key(ch)) {
            return '*';
        }
        return ch;
    }


    public static String sanitizePhoneNumber(@Nullable String phoneNumber) {
        if (isDebugEnabled()) {
            return phoneNumber;
        }
        if (phoneNumber == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(phoneNumber.length());
        for (char c : phoneNumber.toCharArray()) {
            stringBuilder.append(sanitizeDialPadChar(c));
        }
        return stringBuilder.toString();
    }

    public static boolean isVerboseEnabled() {
        return android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
    }

    public static boolean isDebugEnabled() {
        return android.util.Log.isLoggable(TAG, android.util.Log.DEBUG);
    }

    private static boolean is12Key(char ch) {
        return PhoneNumberUtils.is12Key(ch);
    }

    private static void println(
            int level,
            @NonNull String tag,
            @NonNull String localTag,
            @Nullable String msg,
            @Nullable Object... args) {
        String formattedMsg;
        boolean hasArgs = args == null || args.length > 0;
        if ((level >= android.util.Log.INFO) || android.util.Log.isLoggable(tag, level)) {
            formattedMsg = localTag;
            if (!TextUtils.isEmpty(msg)) {
                formattedMsg += SEPARATOR + (hasArgs ? String.format(msg, args) : msg);
            }
            android.util.Log.println(level, tag, formattedMsg);
        }
    }
}
