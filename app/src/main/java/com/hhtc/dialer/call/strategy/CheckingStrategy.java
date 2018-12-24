package com.hhtc.dialer.call.strategy;

import com.hhtc.dialer.call.client.TelephonePeerConnection;
import com.hhtc.dialer.utils.LogUtil;


public class CheckingStrategy extends BasePeerConnectionStrategy{

    public CheckingStrategy(TelephonePeerConnection telephonePeerConnection) {
        super(telephonePeerConnection);
    }

    @Override
    public String name() {
        return "CHECKING";
    }

    @Override
    public void execute() {
        LogUtil.d(TAG, "execute: CHECKING");
    }
}
