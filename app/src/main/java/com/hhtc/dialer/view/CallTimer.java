package com.hhtc.dialer.view;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CallTimer {


    private final long mMillisInFuture;


    private final long mCountdownInterval;

    private long mStopTimeInFuture;


    private boolean mCancelled = false;


    public CallTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }


    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }


    public synchronized final CallTimer start() {
        mCancelled = false;
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    public abstract void onTick(long millisUntilFinished);


    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CallTimer.this) {
                if (mCancelled) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish();
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}