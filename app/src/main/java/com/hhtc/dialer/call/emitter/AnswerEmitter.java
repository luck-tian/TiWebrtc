package com.hhtc.dialer.call.emitter;


import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONObject;
import org.webrtc.SessionDescription;

import io.socket.client.Socket;

public class AnswerEmitter extends BaseEmitter {

    public AnswerEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {

        //RTCSessionDescription参数
        SessionDescription sdp = new SessionDescription(
                SessionDescription.Type.fromCanonicalForm("answer"),
                data.getString("sdp")
        );
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            //setRemoteDescription
            getTelephoneCall().getTelephonePeerConnection().getPeerConnection()
                    .setRemoteDescription(getTelephoneCall().getTelephonePeerConnection(), sdp);
        },TelephoneThreadDispatcher.DispatcherType.RTC);


    }

    @Override
    public void bindClient(Socket client) {
        client.on("answer", getListener());
        TAG = "answer";
        super.bindClient(client);
    }
}
