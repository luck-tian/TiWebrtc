package com.hhtc.dialer.call.emitter;


import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.Socket;

/**
 * service create room
 */
public class CreatedRoomEmitter extends BaseEmitter {

    public CreatedRoomEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws Exception {
        String socketId = data.getString("id");
        //room id
        String roomId = data.getString("room");
        //peer 0 或者 1
        JSONArray peers = data.getJSONArray("peers");
        getTelephoneCall().setLocalSocketId(socketId);
        getTelephoneCall().setRoom(roomId);

        //peers WebRtcPeerConnection，[from,to,room,sdp]
        for (int i = 0; i < peers.length(); i++) {
            JSONObject remoteInfo = peers.getJSONObject(i);
            String remoteSocketId = remoteInfo.getString("id");
            TelephoneThreadDispatcher.getInstance().execute(() -> getTelephoneCall().createNewConnection(remoteSocketId)
                    .createOffer(getTelephoneCall().getTelephonePeerConnection(),
                            getTelephoneCall().getSdpMediaConstraints()), TelephoneThreadDispatcher.DispatcherType.RTC);


        }
    }

    @Override
    public void bindClient(Socket client) {
        client.on("created", getListener());
        TAG = "created";
        super.bindClient(client);
    }

}
