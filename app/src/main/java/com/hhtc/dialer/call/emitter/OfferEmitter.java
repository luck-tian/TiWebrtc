package com.hhtc.dialer.call.emitter;

import org.json.JSONObject;
import org.webrtc.SessionDescription;

import io.socket.client.Socket;

public class OfferEmitter extends BaseEmitter {

    public OfferEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {
        if (getTelephoneCall().getTelephonePeerConnection() != null) {
            String sdpDe = data.getString("sdp");
            //构建RTCSessionDescription
            SessionDescription sdp = new SessionDescription(
                    SessionDescription.Type.fromCanonicalForm("offer"),
                    sdpDe);
            //setRemoteDescription


            getTelephoneCall().getTelephonePeerConnection()
                    .getPeerConnection()
                    .setRemoteDescription(getTelephoneCall()
                            .getTelephonePeerConnection(), sdp);

            //answer
            getTelephoneCall().getTelephonePeerConnection()
                    .getPeerConnection()
                    .createAnswer(getTelephoneCall().getTelephonePeerConnection(),
                            getTelephoneCall().getSdpMediaConstraints());
        }
    }

    @Override
    public void bindClient(Socket client) {
        client.on("offer", getListener());
        TAG = "offer";
        super.bindClient(client);
    }
}
