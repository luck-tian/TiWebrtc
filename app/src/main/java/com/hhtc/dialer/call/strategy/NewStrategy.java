package com.hhtc.dialer.call.strategy;


import com.hhtc.dialer.call.client.TelephonePeerConnection;
import com.hhtc.dialer.utils.LogUtil;


public class NewStrategy extends BasePeerConnectionStrategy {

    public NewStrategy(TelephonePeerConnection telephonePeerConnection) {
        super(telephonePeerConnection);
    }

    @Override
    public String name() {
        return "NEW";
    }

    @Override
    public void execute() {
        LogUtil.d(TAG, "execute: NEW");
    }
}
