package com.hhtc.dialer.call.sip;

import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.EmitterTemp;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONObject;

import io.socket.client.Socket;

public class ConsentCallEmitter extends BaseEmitter {

    public ConsentCallEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {
        //对方接听该电话
        TelephoneThreadDispatcher.getInstance().execute(getTelephoneCall()::remoteAnswer, TelephoneThreadDispatcher.DispatcherType.WORK);
    }

    @Override
    public void bindClient(Socket client) {
        client.on("consentCall", getListener());
        TAG = "consentCall";
        super.bindClient(client);
    }
}
