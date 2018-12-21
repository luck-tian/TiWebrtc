package com.hhtc.dialer.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.hhtc.dialer.add.ContactAddOrEditActivity;
import com.hhtc.dialer.show.ShowContactInfoActivity;

public class intentUnits {

    public static final String ACTION_ADD_CONTACT = "android.intent.action.addContact";

    public static final String ACTION_SHOW_CONTACT = "android.intent.action.showContact";

    public static final String ACTION_PLATE = "android.intent.action.plate";

    public static void startShowContact(Activity activity,Intent intent) {
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
//                        activity.getWindow().getDecorView().getWidth() / 2,
//                        activity.getWindow().getDecorView().getHeight() / 2,
//                        0, 0);
//        ActivityCompat.startActivity(activity, intent, options.toBundle());

        activity.startActivity(intent);
    }

    public static void startAddContact(Activity activity, Intent intent) {
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
//                        activity.getWindow().getDecorView().getWidth() / 2,
//                        activity.getWindow().getDecorView().getHeight() / 2,
//                        0, 0);
//        ActivityCompat.startActivity(activity, intent, options.toBundle());

        activity.startActivity(intent);
    }

    public static void startPlate(Activity activity, Intent intent) {
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
//                        activity.getWindow().getDecorView().getWidth() / 2,
//                        activity.getWindow().getDecorView().getHeight() / 2,
//                        0, 0);
//        ActivityCompat.startActivity(activity, intent, options.toBundle());

        activity.startActivity(intent);
    }
}
