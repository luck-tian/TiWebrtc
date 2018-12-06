package com.hhtc.dialer.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executes work on the IO thread. for disk io action
 */
public class IOThreadExecutor implements Executor {

    private Executor IO;

    public IOThreadExecutor() {
        IO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(Runnable command) {
        IO.execute(command);
    }
}
