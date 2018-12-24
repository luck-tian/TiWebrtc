package com.hhtc.dialer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.hhtc.dialer.add.ContactAddOrEditActivity;
import com.hhtc.dialer.data.tradition.TraditionSynchronise;
import com.hhtc.dialer.show.ShowContactInfoActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class intentUnits {

    public static final String ACTION_ADD_CONTACT = "android.intent.action.addContact";

    public static final String ACTION_SHOW_CONTACT = "android.intent.action.showContact";

    public static final String ACTION_PLATE = "android.intent.action.plate";

    public static final String ACTION_SIP_CALL = "android.intent.action.sipCall";

    public static final String ACTION_TELEGRAM = "android.intent.action.telegram";

    public static final String ACTION_TELEPHONE = "android.intent.action.telephone";

    public static void startShowContact(Activity activity, Intent intent) {
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

    public static void startTradition(Activity activity, Intent intent) {
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
//                        activity.getWindow().getDecorView().getWidth() / 2,
//                        activity.getWindow().getDecorView().getHeight() / 2,
//                        0, 0);
//        ActivityCompat.startActivity(activity, intent, options.toBundle());

        activity.startActivity(intent);
    }


    public static void startCall(Intent intent) {
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
//                        activity.getWindow().getDecorView().getWidth() / 2,
//                        activity.getWindow().getDecorView().getHeight() / 2,
//                        0, 0);
//        ActivityCompat.startActivity(activity, intent, options.toBundle());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        TraditionSynchronise.getContext().startActivity(intent);
    }

    public static void startService(Context context,Intent intent) {
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
//                        activity.getWindow().getDecorView().getWidth() / 2,
//                        activity.getWindow().getDecorView().getHeight() / 2,
//                        0, 0);
//        ActivityCompat.startActivity(activity, intent, options.toBundle());

        context.startService(intent);
    }
}
