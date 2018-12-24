package com.hhtc.dialer.window;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.utils.LogUtil;
import com.hhtc.dialer.utils.SizeUtils;

import java.lang.reflect.Field;
import java.util.List;

public class IncomingTelegramWindow implements View.OnTouchListener, View.OnClickListener {

    private static final String TAG = "SIPWindow";
    private static final int SHOW = 1;
    private static final int HIND = 2;
    private int state = HIND;
    private Context mContext;
    private WindowManager.LayoutParams mWindowParams;
    private WindowManager mWindowManager;

    private View mFloatLayout;
    private float mInViewX;
    private float mInViewY;
    private float mDownInScreenX;
    private float mDownInScreenY;
    private float mInScreenX;
    private float mInScreenY;

    public IncomingTelegramWindow(Context context) {
        this.mContext = context.getApplicationContext();
        initFloatWindow();
    }

    private void initFloatWindow() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (inflater == null)
            return;
        mFloatLayout = (View) inflater.inflate(R.layout.incoming_telegram_layout, null);
        mFloatLayout.setOnTouchListener(this);
        mWindowParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {//8.0新特性
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.gravity = Gravity.START | Gravity.TOP;
        mWindowParams.width = SizeUtils.dp2px(80, mContext);
        mWindowParams.height = SizeUtils.dp2px(80, mContext);
        mFloatLayout.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return floatLayoutTouch(motionEvent);
    }

    private boolean floatLayoutTouch(MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInViewX = motionEvent.getX();
                mInViewY = motionEvent.getY();
                mDownInScreenX = motionEvent.getRawX();
                mDownInScreenY = motionEvent.getRawY() - getSysBarHeight(mContext);
                mInScreenX = motionEvent.getRawX();
                mInScreenY = motionEvent.getRawY() - getSysBarHeight(mContext);
                break;
            case MotionEvent.ACTION_MOVE:
                mInScreenX = motionEvent.getRawX();
                mInScreenY = motionEvent.getRawY() - getSysBarHeight(mContext);
                mWindowParams.x = (int) (mInScreenX - mInViewX);
                mWindowParams.y = (int) (mInScreenY - mInViewY);
                mWindowManager.updateViewLayout(mFloatLayout, mWindowParams);
                break;
            case MotionEvent.ACTION_UP:
                if (mDownInScreenX == mInScreenX && mDownInScreenY == mInScreenY) {
                    onClick(null);
                }
                break;
        }
        return true;
    }

    public void showFloatWindow(String name) {
        if (state == HIND) {
            state = SHOW;
            LogUtil.e(TAG, "showFloatWindow");
            if (mFloatLayout.getParent() == null) {
                DisplayMetrics metrics = new DisplayMetrics();
                mWindowManager.getDefaultDisplay().getMetrics(metrics);
                mWindowParams.x = metrics.widthPixels;
                mWindowParams.y = metrics.heightPixels / 2 - getSysBarHeight(mContext);
                mWindowManager.addView(mFloatLayout, mWindowParams);
            }

            TextView remote = mFloatLayout.findViewById(R.id.remoteName);
            remote.setText(name);
        }
    }

    public void hideFloatWindow() {
        if (state == SHOW) {
            state = HIND;
            Log.e(TAG, "hideFloatWindow");
            if (mFloatLayout.getParent() != null)
                mWindowManager.removeView(mFloatLayout);
        }
    }


    public static int getSysBarHeight(Context contex) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        int sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = contex.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    @Override
    public void onClick(View v) {
        recovery(mContext);
    }

    private void recovery(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task_info = manager
                .getRunningTasks(24);

        String className = "";

        for (int i = 0; i < task_info.size(); i++) {
            LogUtil.i(TAG, "存在的包：" + task_info
                    .get(i).topActivity.getPackageName());
            if (context.getPackageName().equals(task_info
                    .get(i).topActivity.getPackageName())) {
                manager.moveTaskToFront(task_info.get(i).id, ActivityManager.MOVE_TASK_WITH_HOME);//关键
                className = task_info.get(i).topActivity.getClassName();


                Intent intentgo = new Intent();
                intentgo.setAction(Intent.ACTION_MAIN);
                intentgo.addCategory(Intent.CATEGORY_LAUNCHER);
                try {
                    intentgo.setComponent(new ComponentName(context, Class.forName(className)));//
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                intentgo.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intentgo);

            }
        }
    }

}