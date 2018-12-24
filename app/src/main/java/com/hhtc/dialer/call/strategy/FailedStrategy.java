package com.hhtc.dialer.call.strategy;


import com.hhtc.dialer.call.client.TelephonePeerConnection;
import com.hhtc.dialer.utils.LogUtil;

public class FailedStrategy extends BasePeerConnectionStrategy{

    public FailedStrategy(TelephonePeerConnection telephonePeerConnection) {
        super(telephonePeerConnection);
    }

    @Override
    public String name() {
        return "FAILED";
    }

    @Override
    public void execute() {
        //连接失败 不处理
        LogUtil.d(TAG, "execute: FAILED");
    }
}
