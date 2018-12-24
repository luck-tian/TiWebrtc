package com.hhtc.dialer.call.strategy;


import com.hhtc.dialer.call.client.TelephonePeerConnection;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.LogUtil;

public class ConnectedStrategy extends BasePeerConnectionStrategy{

    public ConnectedStrategy(TelephonePeerConnection telephonePeerConnection) {
        super(telephonePeerConnection);
    }

    @Override
    public String name() {
        return "CONNECTED";
    }

    @Override
    public void execute() {
        LogUtil.d(TAG, "execute: CONNECTED");
        TelephoneThreadDispatcher.getInstance().execute(() -> getTelephonePeerConnection().onStop(),TelephoneThreadDispatcher.DispatcherType.WORK);

    }
}
