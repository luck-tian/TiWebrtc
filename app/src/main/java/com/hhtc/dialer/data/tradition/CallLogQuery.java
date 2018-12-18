package com.hhtc.dialer.data.tradition;

import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CallLogQuery {

  public static final int ID = 0;
  public static final int NUMBER = 1;
  public static final int DATE = 2;
  public static final int DURATION = 3;
  public static final int CALL_TYPE = 4;
  public static final int COUNTRY_ISO = 5;
  public static final int VOICEMAIL_URI = 6;
  public static final int GEOCODED_LOCATION = 7;
  public static final int CACHED_NAME = 8;
  public static final int CACHED_NUMBER_TYPE = 9;
  public static final int CACHED_NUMBER_LABEL = 10;
  public static final int CACHED_LOOKUP_URI = 11;
  public static final int CACHED_MATCHED_NUMBER = 12;
  public static final int CACHED_NORMALIZED_NUMBER = 13;
  public static final int CACHED_PHOTO_ID = 14;
  public static final int CACHED_FORMATTED_NUMBER = 15;
  public static final int IS_READ = 16;
  public static final int NUMBER_PRESENTATION = 17;
  public static final int ACCOUNT_COMPONENT_NAME = 18;
  public static final int ACCOUNT_ID = 19;
  public static final int FEATURES = 20;
  public static final int DATA_USAGE = 21;
  public static final int TRANSCRIPTION = 22;
  public static final int CACHED_PHOTO_URI = 23;

  @RequiresApi(Build.VERSION_CODES.N)
  public static final int POST_DIAL_DIGITS = 24;

  @RequiresApi(Build.VERSION_CODES.N)
  public static final int VIA_NUMBER = 25;

  @RequiresApi(Build.VERSION_CODES.O)
  public static final int TRANSCRIPTION_STATE = 26;

  private static final String[] PROJECTION_M =
      new String[] {
        CallLog.Calls._ID, // 0
        CallLog.Calls.NUMBER, // 1
        CallLog.Calls.DATE, // 2
        CallLog.Calls.DURATION, // 3
        CallLog.Calls.TYPE, // 4
        CallLog.Calls.COUNTRY_ISO, // 5
        CallLog.Calls.VOICEMAIL_URI, // 6
        CallLog.Calls.GEOCODED_LOCATION, // 7
        CallLog.Calls.CACHED_NAME, // 8
        CallLog.Calls.CACHED_NUMBER_TYPE, // 9
        CallLog.Calls.CACHED_NUMBER_LABEL, // 10
        CallLog.Calls.CACHED_LOOKUP_URI, // 11
        CallLog.Calls.CACHED_MATCHED_NUMBER, // 12
        CallLog.Calls.CACHED_NORMALIZED_NUMBER, // 13
        CallLog.Calls.CACHED_PHOTO_ID, // 14
        CallLog.Calls.CACHED_FORMATTED_NUMBER, // 15
        CallLog.Calls.IS_READ, // 16
        CallLog.Calls.NUMBER_PRESENTATION, // 17
        CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME, // 18
        CallLog.Calls.PHONE_ACCOUNT_ID, // 19
        CallLog.Calls.FEATURES, // 20
        CallLog.Calls.DATA_USAGE, // 21
        CallLog.Calls.TRANSCRIPTION, // 22
        CallLog.Calls.CACHED_PHOTO_URI, // 23
      };

  private static final String[] PROJECTION_N;

  static {
    List<String> projectionList = new ArrayList<>(Arrays.asList(PROJECTION_M));
    projectionList.add(CallLog.Calls.POST_DIAL_DIGITS);
    projectionList.add(CallLog.Calls.VIA_NUMBER);
    PROJECTION_N = projectionList.toArray(new String[projectionList.size()]);
  }

  private static final String[] PROJECTION_O;

  // Copied from android.provider.CallLog.Calls
  private static final String TRANSCRIPTION_STATE_COLUMN = "transcription_state";

  static {
    List<String> projectionList = new ArrayList<>(Arrays.asList(PROJECTION_N));
    projectionList.add(TRANSCRIPTION_STATE_COLUMN);
    PROJECTION_O = projectionList.toArray(new String[projectionList.size()]);
  }

  @NonNull
  public static String[] getProjection() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      return PROJECTION_O;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return PROJECTION_N;
    }
    return PROJECTION_M;
  }
}