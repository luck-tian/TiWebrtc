package com.hhtc.dialer.call.client;

import android.util.Log;


import com.hhtc.dialer.call.strategy.CheckingStrategy;
import com.hhtc.dialer.call.strategy.ClosedStrategy;
import com.hhtc.dialer.call.strategy.CompletedStrategy;
import com.hhtc.dialer.call.strategy.ConnectedStrategy;
import com.hhtc.dialer.call.strategy.DisconnectedStrategy;
import com.hhtc.dialer.call.strategy.FailedStrategy;
import com.hhtc.dialer.call.strategy.NewStrategy;
import com.hhtc.dialer.call.strategy.PeerConnectionExecutor;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpTransceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.Locale;

import static com.hhtc.dialer.call.client.TelephoneCall.STATELESS;


public class TelephonePeerConnection extends RetryConnectionTimer implements PeerConnection.Observer, SdpObserver {

    private static final String TAG = "TelephonePeerConnection";

    private static final long COUNT_DOWN_INTERVAL = 100;

    private static final long MILLIS_IN_FUTURE = 30 * 1000;

    private static final int HOUR = 3600000;
    private static final int MIN = 60000;
    private static final int SEC = 1000;

    private PeerConnection peerConnection;

    private TelephoneCall telephoneCall;

    private String remoteId;

    private long timeOut;

    PeerConnectionExecutor peerConnectionExecutor;

    public TelephonePeerConnection(String remoteId, PeerConnectionFactory factory,
                                   PeerConnection.RTCConfiguration rtcConfig, TelephoneCall telephoneCall) {
        super(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
        this.remoteId = remoteId;
        this.telephoneCall = telephoneCall;
        peerConnection = factory.createPeerConnection(rtcConfig, this);
        peerConnectionExecutor = new PeerConnectionExecutor()
                .addStrategy(new CheckingStrategy(this))
                .addStrategy(new ClosedStrategy(this))
                .addStrategy(new CompletedStrategy(this))
                .addStrategy(new ConnectedStrategy(this))
                .addStrategy(new DisconnectedStrategy(this))
                .addStrategy(new FailedStrategy(this))
                .addStrategy(new NewStrategy(this));
    }


    public PeerConnection getPeerConnection() {
        return peerConnection;
    }

    /**
     * Triggered when signaling status changes
     *
     * @param signalingState
     */
    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Log.d(TAG, "onSignalingChange: " + signalingState.name());
    }

    /**
     * Triggered when the IceConnectionState connection state changes
     *
     * @param iceConnectionState
     */
    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        Log.d(TAG, "onIceConnectionChange: " + iceConnectionState.name());
        //状态发送改变是的时候 ice连接状态 实现超时
        peerConnectionExecutor.getStrategy(iceConnectionState.name()).execute();
    }

    /**
     * IceConnectionState connection receives state changes
     *
     * @param changes
     */
    @Override
    public void onIceConnectionReceivingChange(boolean changes) {
        Log.d(TAG, "onIceConnectionReceivingChange: " + changes);
    }

    /**
     * IceConnectionState network information acquisition state change
     *
     * @param iceGatheringState
     */
    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.d(TAG, "onIceGatheringChange: " + iceGatheringState.name());

    }

    /**
     * The new ice address was found triggered
     *
     * @param iceCandidate
     */
    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        Log.d(TAG, "onIceCandidate: " + iceCandidate.toString());
        try {
            //构建信令数据
            JSONObject message = new JSONObject();
            message.put("from", telephoneCall.getLocalSocketId());
            message.put("to", remoteId);
            message.put("room", telephoneCall.getRoom());
            //candidate参数
            JSONObject candidate = new JSONObject();
            candidate.put("sdpMid", iceCandidate.sdpMid);
            candidate.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
            candidate.put("sdp", iceCandidate.sdp);
            message.put("candidate", candidate);
            //向信令服务器发送信令
            telephoneCall.getSignallingTransfer().sendSignalling("candidate", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * ice address removed trigger
     *
     * @param iceCandidates
     */
    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        Log.d(TAG, "onIceCandidatesRemoved: " + iceCandidates.toString());
    }

    /**
     * Trigger note when Peer connects remote audio-video data arrival: replace with onTrack callback
     *
     * @param mediaStream
     */
    @Override
    public void onAddStream(MediaStream mediaStream) {
        Log.d(TAG, "onAddStream: " + mediaStream.getId());
    }

    /**
     * Triggered when Peer connection remotes remote audio and video data
     *
     * @param mediaStream
     */
    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Log.d(TAG, "onRemoveStream: " + mediaStream.getId());
    }

    /**
     * Triggered when the remote Peer connection opens a data transfer channel
     *
     * @param dataChannel
     */
    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Log.d(TAG, "onDataChannel: " + dataChannel.label());
    }

    /**
     * Triggered when the channel interaction protocol needs to be renegotiated
     */
    @Override
    public void onRenegotiationNeeded() {
        Log.d(TAG, "onRenegotiationNeeded: ");
    }

    /**
     * Triggered when a new track is signaled by the remote peer, as a result of setRemoteDescription
     *
     * @param rtpReceiver
     * @param mediaStreams
     */
    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
        Log.d(TAG, "onAddTrack: " + rtpReceiver.id());
    }

    /**
     * @param transceiver
     */
    @Override
    public void onTrack(RtpTransceiver transceiver) {
        Log.d(TAG, "onTrack: " + transceiver.getMid());
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        Log.d(TAG, "onCreateSuccess: " + sessionDescription.description);
        String type = sessionDescription.type.canonicalForm();
        Log.d(TAG, "onCreateSuccess " + type);
        //设置本地LocalDescription
        peerConnection.setLocalDescription(this, sessionDescription);
        //构建信令数据
        try {
            JSONObject message = new JSONObject();
            message.put("from", telephoneCall.getLocalSocketId());
            message.put("to", remoteId);
            message.put("room", telephoneCall.getRoom());
            message.put("sdp", sessionDescription.description);
            //向信令服务器发送信令
            telephoneCall.getSignallingTransfer().sendSignalling(type, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSetSuccess() {
        Log.d(TAG, "onSetSuccess: ");
    }

    @Override
    public void onCreateFailure(String s) {
        Log.d(TAG, "onCreateFailure: " + s);
    }

    @Override
    public void onSetFailure(String s) {
        Log.d(TAG, "onSetFailure: " + s);
    }


    @Override
    public void onStart() {
        timeOut = 0;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeOut += COUNT_DOWN_INTERVAL;
        Log.d(TAG, "onTick: 超时 : " + generateCountdownText(timeOut) + "  " + isCancelled());
    }

    @Override
    public void onFinish() {
        Log.d(TAG, "onFinish: " + isCancelled());
        if (telephoneCall.getStatus() != STATELESS && !isCancelled()) {
            telephoneCall.hangUp();
        }
    }

    @Override
    public void onStop() {
        cancel();
        Log.d(TAG, "onStop: onStop");

    }

    static String generateCountdownText(long duration) {
        int hr = (int) (duration / HOUR);
        int min = (int) ((duration - (hr * HOUR)) / MIN);
        int sec = (int) ((duration - (hr * HOUR) - (min * MIN)) / SEC);

        Locale locale = Locale.getDefault();
        String format = "%02d";
        String formattedSec = String.format(locale, format, sec);

        return String.format(locale, "%s", formattedSec);
    }

    public TelephoneCall getTelephoneCall() {
        return telephoneCall;
    }

    public void changeSocketId(String remoteSocketId) {
        this.remoteId = remoteSocketId;
    }
}
