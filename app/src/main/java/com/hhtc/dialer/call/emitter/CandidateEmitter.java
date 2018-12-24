package com.hhtc.dialer.call.emitter;

import org.json.JSONObject;
import org.webrtc.IceCandidate;

import io.socket.client.Socket;

public class CandidateEmitter extends BaseEmitter {

    public CandidateEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {

        JSONObject candidate = data.getJSONObject("candidate");
        IceCandidate iceCandidate = new IceCandidate(
                candidate.getString("sdpMid"), //id
                candidate.getInt("sdpMLineIndex"),
                candidate.getString("sdp")//
        );

        getTelephoneCall()
                .getTelephonePeerConnection()
                .getPeerConnection()
                .addIceCandidate(iceCandidate);


    }

    @Override
    public void bindClient(Socket client) {
        client.on("candidate", getListener());
        TAG = "candidate";
        super.bindClient(client);
    }

}
