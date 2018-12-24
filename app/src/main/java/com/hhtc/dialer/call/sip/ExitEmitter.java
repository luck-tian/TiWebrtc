package com.hhtc.dialer.call.sip;

import android.text.TextUtils;

import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.EmitterTemp;

import org.json.JSONObject;

import io.socket.client.Socket;

public class ExitEmitter extends BaseEmitter {

    public ExitEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {
        if (data.has("name")) {
            String name = data.getString("name");
            if (getTelephoneCall() != null && TextUtils.equals(getTelephoneCall().getRemoteName(), name)) {
                //对方退出
               // TelephoneThreadDispatcher.getInstance().execute(getTelephoneCall()::remoteHangUp, TelephoneThreadDispatcher.DispatcherType.WORK);
            }
        }
    }

    @Override
    public void bindClient(Socket client) {
        client.on("exit", getListener());
        TAG = "exit";
        super.bindClient(client);
    }
}
