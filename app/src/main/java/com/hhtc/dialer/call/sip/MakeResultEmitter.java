package com.hhtc.dialer.call.sip;

import android.text.TextUtils;
import android.util.Log;


import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.EmitterTemp;
import com.hhtc.dialer.call.notifiy.NotificationMgr;
import com.hhtc.dialer.call.util.DataUtils;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONObject;
import org.webrtc.PeerConnection;

import io.socket.client.Socket;

public class MakeResultEmitter extends BaseEmitter {

    public MakeResultEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    private boolean canStart = false;

    @Override
    public void pushData(JSONObject data) throws Exception {
        //对方的节点名字 和房间名字
        String room = data.getString("name");

        if (getTelephoneCall().getTelephonePeerConnection() != null) {
            Log.e(TAG, "pushData: " + getTelephoneCall().getTelephonePeerConnection().getPeerConnection().iceConnectionState().name());
        }
        if (getTelephoneCall() != null && (
                getTelephoneCall().getCommunicateTelephone() != null ||
                        getTelephoneCall().getTelephone() != null)) {
            //通知对方占线
            TelephoneThreadDispatcher.getInstance().execute(() -> getTelephoneCall().getSignallingTransfer().busyRoom(room, DataUtils.getTelName()), TelephoneThreadDispatcher.DispatcherType.WORK);
        } else if (getTelephoneCall().getTelephonePeerConnection() != null &&
                getTelephoneCall().getTelephonePeerConnection()
                        .getPeerConnection().iceConnectionState()
                        != PeerConnection.IceConnectionState.CLOSED) {
            TelephoneThreadDispatcher.getInstance().execute(() -> getTelephoneCall().getSignallingTransfer().busyRoom(room, DataUtils.getTelName()), TelephoneThreadDispatcher.DispatcherType.WORK);
        } else if (!canStart) {
            canStart = true;
            //通知用户有来电 锁屏 通知 ... 处理
            TelephoneThreadDispatcher.getInstance().execute(() -> NotificationMgr.getInstance().incomingTelegram(room), TelephoneThreadDispatcher.DispatcherType.WORK);
            TelephoneThreadDispatcher.getInstance().executeDelay(() -> canStart = false, 1000);
        } else if (TextUtils.equals(NotificationMgr.getInstance().getRemoteName(), room)) {
            //多次
        } else {
            TelephoneThreadDispatcher.getInstance().execute(() -> getTelephoneCall().getSignallingTransfer().busyRoom(room, DataUtils.getTelName()), TelephoneThreadDispatcher.DispatcherType.WORK);
        }
    }

    @Override
    public void bindClient(Socket client) {
        client.on("makeResult", getListener());
        TAG = "makeResult";
        super.bindClient(client);
    }
}
