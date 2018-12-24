package com.hhtc.dialer.call.strategy;

import com.hhtc.dialer.call.client.TelephonePeerConnection;
import com.hhtc.dialer.utils.LogUtil;

public class ClosedStrategy extends BasePeerConnectionStrategy {

    public ClosedStrategy(TelephonePeerConnection telephonePeerConnection) {
        super(telephonePeerConnection);
    }

    @Override
    public String name() {
        return "CLOSED";
    }

    @Override
    public void execute() {
        getTelephonePeerConnection().getTelephoneCall().remoteAnswer();
        LogUtil.d(TAG, "execute: CLOSED");
    }
}
