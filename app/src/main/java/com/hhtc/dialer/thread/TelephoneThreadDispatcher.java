package com.hhtc.dialer.thread;

import android.os.Handler;
import android.os.HandlerThread;


/**
 * thread dispatcher to service telephone
 */
public class TelephoneThreadDispatcher {

    private static TelephoneThreadDispatcher INSTANCE;

    private static final Object LOCK = new Object();

    private IOThreadExecutor ioThreadExecutor;

    private WorkThreadExecutor workThreadExecutor;

    private UiThreadExecutor uiThreadExecutor;

    private HandlerThread work = new HandlerThread("work");

    private Handler mHandler;

    private TelephoneThreadDispatcher(IOThreadExecutor ioThreadExecutor,
                                      WorkThreadExecutor workThreadExecutor,
                                      UiThreadExecutor uiThreadExecutor) {
        this.ioThreadExecutor = ioThreadExecutor;
        this.workThreadExecutor = workThreadExecutor;
        this.uiThreadExecutor = uiThreadExecutor;
        work.start();
        mHandler = new Handler(work.getLooper());
    }

    public static TelephoneThreadDispatcher getInstance() {
        synchronized (LOCK) {
            if (null == INSTANCE) {
                INSTANCE = new TelephoneThreadDispatcher();
            }
        }
        return INSTANCE;
    }

    public TelephoneThreadDispatcher() {
        this(new IOThreadExecutor(), new WorkThreadExecutor(), new UiThreadExecutor());
    }


    public void execute(Runnable command, DispatcherType type) {
        if (type == DispatcherType.IO) {
            ioThreadExecutor.execute(command);
        } else if (type == DispatcherType.WORK) {
            workThreadExecutor.execute(command);
        } else if (type == DispatcherType.UI) {
            uiThreadExecutor.execute(command);
        } else if (type == DispatcherType.RTC) {
            mHandler.post(command);
        }
    }

    public void executeDelay(Runnable command, long delayMillis) {
        mHandler.postDelayed(command, delayMillis);
    }


    public enum DispatcherType {
        IO,
        WORK,
        UI,
        RTC
    }

    public Handler getHandler() {
        return mHandler;
    }
}
