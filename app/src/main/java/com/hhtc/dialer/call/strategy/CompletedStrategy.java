package com.hhtc.dialer.call.strategy;

import com.hhtc.dialer.call.client.TelephonePeerConnection;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.LogUtil;


public class CompletedStrategy extends BasePeerConnectionStrategy {

    public CompletedStrategy(TelephonePeerConnection telephonePeerConnection) {
        super(telephonePeerConnection);
    }

    @Override
    public String name() {
        return "COMPLETED";
    }

    @Override
    public void execute() {
        LogUtil.d(TAG, "execute: COMPLETED");
        //连接成功
        TelephoneThreadDispatcher.getInstance().execute(() -> getTelephonePeerConnection().onStop(),TelephoneThreadDispatcher.DispatcherType.WORK);

    }
}
