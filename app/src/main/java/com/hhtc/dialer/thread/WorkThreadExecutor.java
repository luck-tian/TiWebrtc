package com.hhtc.dialer.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executes work on the network thread. request network or action to UI thread other
 */
public class WorkThreadExecutor implements Executor {

    private static final int WORK_THREAD_COUNT = 3;

    private Executor work;

    public WorkThreadExecutor() {
        work = Executors.newFixedThreadPool(WORK_THREAD_COUNT);
    }

    @Override
    public void execute(Runnable command) {
        work.execute(command);
    }
}
