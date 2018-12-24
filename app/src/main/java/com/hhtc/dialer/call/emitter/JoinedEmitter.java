package com.hhtc.dialer.call.emitter;


import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONObject;

import io.socket.client.Socket;

public class JoinedEmitter extends BaseEmitter {

    public JoinedEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {
        String fromId = data.getString("id");
        TelephoneThreadDispatcher.getInstance().execute(() -> getTelephoneCall().createNewConnection(fromId), TelephoneThreadDispatcher.DispatcherType.RTC);

    }

    @Override
    public void bindClient(Socket client) {
        client.on("joined", getListener());
        TAG = "joined";
        super.bindClient(client);
    }

}
