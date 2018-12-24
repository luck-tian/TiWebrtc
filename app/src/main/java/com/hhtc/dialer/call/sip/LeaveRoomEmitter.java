package com.hhtc.dialer.call.sip;



import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.EmitterTemp;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONObject;

import io.socket.client.Socket;

public class LeaveRoomEmitter extends BaseEmitter {

    public LeaveRoomEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {
        TelephoneThreadDispatcher.getInstance().execute(getTelephoneCall()::remoteHangUp, TelephoneThreadDispatcher.DispatcherType.WORK);
    }

    @Override
    public void bindClient(Socket client) {
        client.on("leaveRoom", getListener());
        TAG = "leaveRoom";
        super.bindClient(client);
    }
}
