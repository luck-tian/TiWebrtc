package com.hhtc.dialer.call.strategy;

import android.widget.Toast;


import com.hhtc.dialer.R;
import com.hhtc.dialer.call.client.TelephonePeerConnection;
import com.hhtc.dialer.data.tradition.TraditionSynchronise;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.LogUtil;

import static com.hhtc.dialer.call.client.TelephoneCall.STATELESS;


public class DisconnectedStrategy extends BasePeerConnectionStrategy {

    public DisconnectedStrategy(TelephonePeerConnection telephonePeerConnection) {
        super(telephonePeerConnection);
    }

    @Override
    public String name() {
        return "DISCONNECTED";
    }

    @Override
    public void execute() {
        LogUtil.d(TAG, "execute: DISCONNECTED");
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            //中断处理 ice 失去连接
            if (getTelephonePeerConnection().getTelephoneCall().getStatus() != STATELESS &&
                    !getTelephonePeerConnection().isStart()) {
                getTelephonePeerConnection().start();
            }
        }, TelephoneThreadDispatcher.DispatcherType.RTC);

        if (getTelephonePeerConnection().getTelephoneCall().getStatus() != STATELESS)
            TelephoneThreadDispatcher.getInstance().execute(() -> Toast.makeText(TraditionSynchronise.getContext(), R.string.poor_network, Toast.LENGTH_SHORT).show(), TelephoneThreadDispatcher.DispatcherType.UI);
    }
}
