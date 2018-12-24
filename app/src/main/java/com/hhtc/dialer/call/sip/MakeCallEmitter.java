package com.hhtc.dialer.call.sip;

import android.text.TextUtils;


import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.EmitterTemp;

import org.json.JSONObject;

import io.socket.client.Socket;

public class MakeCallEmitter extends BaseEmitter {

    public MakeCallEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {

        try {
            String room = data.getString("name");
            if (!TextUtils.isEmpty(room)) {
                telephoneCall.getSignallingTransfer().createAndJoinRoom(room);
            }
        } catch (Exception e) {
            //等待超时
        }

    }

    @Override
    public void bindClient(Socket client) {
        client.on("makeCall", getListener());
        TAG = "makeCall";
        super.bindClient(client);
    }
}
