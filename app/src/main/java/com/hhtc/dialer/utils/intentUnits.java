package com.hhtc.dialer.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.hhtc.dialer.add.ContactAddOrEditActivity;
import com.hhtc.dialer.show.ShowContactInfoActivity;

public class intentUnits {

    public static void startAddContact(Activity activity) {
        Intent intent = new Intent(activity, ContactAddOrEditActivity.class);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
                        activity.getWindow().getDecorView().getWidth() / 2,
                        activity.getWindow().getDecorView().getHeight() / 2,
                        0, 0);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void startShowContact(Activity activity, long contactId) {
        Intent intent = new Intent(activity, ShowContactInfoActivity.class);
        intent.putExtra(ShowContactInfoActivity.LOAD_ID, contactId);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
                        activity.getWindow().getDecorView().getWidth() / 2,
                        activity.getWindow().getDecorView().getHeight() / 2,
                        0, 0);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void startAddContact(Activity activity, long loadId) {
        Intent intent = new Intent(activity, ContactAddOrEditActivity.class);
        intent.putExtra(ContactAddOrEditActivity.CONTACT_MODEL_ID, loadId);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(activity.getWindow().getDecorView(),
                        activity.getWindow().getDecorView().getWidth() / 2,
                        activity.getWindow().getDecorView().getHeight() / 2,
                        0, 0);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
