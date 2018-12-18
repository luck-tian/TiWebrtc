package com.hhtc.dialer.data.favorite;

import android.provider.ContactsContract;

public final class FavoriteQuery {

//  public static final int PHONE_ID = 0;
//  public static final int PHONE_DISPLAY_NAME = 1;
//  public static final int PHONE_STARRED = 2;
//  public static final int PHONE_PHOTO_URI = 3;
//  public static final int PHONE_LOOKUP_KEY = 4;
//  public static final int PHONE_PHOTO_ID = 5;
//  public static final int PHONE_NUMBER = 6;
//  public static final int PHONE_TYPE = 7;
//  public static final int PHONE_LABEL = 8;
//  public static final int PHONE_IS_SUPER_PRIMARY = 9;
//  public static final int PHONE_PINNED = 10;
//  public static final int PHONE_CONTACT_ID = 11;

  public static final String[] PHONE_PROJECTION =
          new String[] {
                  ContactsContract.CommonDataKinds.Phone._ID, // 0
                  ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // 1
                  ContactsContract.CommonDataKinds.Phone.STARRED, // 2
                  ContactsContract.CommonDataKinds.Phone.PHOTO_URI, // 3
                  ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY, // 4
                  ContactsContract.CommonDataKinds.Phone.PHOTO_ID, // 5
                  ContactsContract.CommonDataKinds.Phone.NUMBER, // 6
                  ContactsContract.CommonDataKinds.Phone.TYPE, // 7
                  ContactsContract.CommonDataKinds.Phone.LABEL, // 8
                  ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY, // 9
                  ContactsContract.CommonDataKinds.Phone.PINNED, // 10
                  ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 11
          };

}