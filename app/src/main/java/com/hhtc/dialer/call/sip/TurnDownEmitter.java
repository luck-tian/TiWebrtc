package com.hhtc.dialer.call.sip;



import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.EmitterTemp;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONObject;
import org.webrtc.SessionDescription;

import io.socket.client.Socket;

public class TurnDownEmitter extends BaseEmitter {

    public TurnDownEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {
        //对方挂断电话
        if (getTelephoneCall() != null)
            TelephoneThreadDispatcher.getInstance().execute(getTelephoneCall()::remoteRefuseAnswer, TelephoneThreadDispatcher.DispatcherType.WORK);
    }

    @Override
    public void bindClient(Socket client) {
        client.on("turnDown", getListener());
        TAG = "turnDown";
        super.bindClient(client);
    }
}
