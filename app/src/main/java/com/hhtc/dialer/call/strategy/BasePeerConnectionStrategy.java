package com.hhtc.dialer.call.strategy;


import com.hhtc.dialer.call.client.TelephonePeerConnection;

public abstract class BasePeerConnectionStrategy implements PeerConnectionStrategy {

    protected static final String TAG = "BasePeerConnectionStrategy";

    private TelephonePeerConnection telephonePeerConnection;

    public BasePeerConnectionStrategy(TelephonePeerConnection telephonePeerConnection){
        this.telephonePeerConnection=telephonePeerConnection;
    }

    public TelephonePeerConnection getTelephonePeerConnection() {
        return telephonePeerConnection;
    }
}
