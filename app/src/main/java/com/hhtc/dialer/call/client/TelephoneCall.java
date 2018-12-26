package com.hhtc.dialer.call.client;

import android.os.CountDownTimer;
import android.os.RemoteException;
import android.provider.CallLog;
import android.util.Log;

import com.hhtc.dialer.TelephoneCommunicateTelephone;
import com.hhtc.dialer.TelephoneIncomingTelegram;
import com.hhtc.dialer.call.notifiy.NotificationMgr;
import com.hhtc.dialer.call.util.DataUtils;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.data.tradition.TraditionSynchronise;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.IntentProvider;
import com.hhtc.dialer.utils.LogUtil;
import com.hhtc.dialer.utils.intentUnits;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;

import java.util.LinkedList;

import static android.media.AudioManager.MODE_IN_COMMUNICATION;

public class TelephoneCall extends CountDownTimer {

    private static final String TAG = TelephoneCall.class.getSimpleName();

    private static final long COUNT_DOWN_INTERVAL = 100;

    private static final long MILLIS_IN_FUTURE = 10 * 1000;

    private static final String AUDIO_TRACK_ID = "ARDAMSa0";
    private static final String AUDIO_ECHO_CANCELLATION_CONSTRAINT = "googEchoCancellation";
    private static final String AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT = "googAutoGainControl";
    private static final String AUDIO_HIGH_PASS_FILTER_CONSTRAINT = "googHighpassFilter";
    private static final String AUDIO_NOISE_SUPPRESSION_CONSTRAINT = "googNoiseSuppression";
    public static final Object LOCK = new Object();

    public static final int STATELESS = 0;
    public static final int MAKE_CALL = 1;
    public static final int HANG_UP = 2;
    public static final int ANSWER = 3;
    public static final int REMOTE_HANG_UP = 4;
    public static final int REMOTE_ANSWER = 5;
    public static final int BUSY_ROOM = 6;
    public static final int REMOTE_REFUSE_ANSWER = 7;
    public static final int REMOTE_TIMEOUT = 8;
    public static final int REFUSE_ANSWER = 8;

    private static TelephoneCall instance;

    private int status;

    private String remoteName;

    private String room;

    //PeerConnection对象
    private TelephonePeerConnection telephonePeerConnection;

    //PeerConnection标识
    private String localSocketId;

    private String remoteSocketId;

    //PeerConnect 音频约束
    private MediaConstraints audioConstraints;

    //PeerConnect sdp约束
    private MediaConstraints sdpMediaConstraints;

    private AudioSource localAudioSource;

    //音频Track
    private AudioTrack localAudioTrack;

    public PeerConnectionFactory factory;

    public PeerConnection.RTCConfiguration rtcConfig;

    private LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<>();

    private SignallingTransfer signallingTransfer;

    private TelephoneCommunicateTelephone telephone;

    private TelephoneIncomingTelegram communicateTelephone;

    private RecentCallLog callLog;

    private int type;

    private TelephoneCall() {
        super(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
        createIceServers();
    }


    private void createIceServers() {
        iceServers.add(PeerConnection.IceServer.builder("turn:numb.viagenie.ca")
                .setUsername("tianhong_liang@163.com")
                .setPassword("tianhong_liang")
                .createIceServer());

        iceServers.add(PeerConnection.IceServer.builder("turn:numb.viagenie.ca")
                .setUsername("webrtc@live.com")
                .setPassword("muazkh")
                .createIceServer());

        iceServers.add(PeerConnection.IceServer.builder("stun:123.206.72.254")
                .setUsername("webrtc")
                .setPassword("webrtc")
                .createIceServer());
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.xten.com").createIceServer());
    }

    public void attach(PeerConnectionFactory factory) {
        this.factory = factory;
        createMediaConstraint();
        createSdpMediaConstraint();
        createLocalAudioSource();
        createLocalAudioTrack();
        createConnection();
    }


    public PeerConnection createNewConnection(String remoteSocketId) {
        this.remoteSocketId = remoteSocketId;
        if (telephonePeerConnection == null) {
            telephonePeerConnection = new TelephonePeerConnection(remoteSocketId, factory, rtcConfig, this);
            telephonePeerConnection.getPeerConnection().addTrack(getLocalAudioTrack());
        } else {
            if (telephonePeerConnection.getPeerConnection().iceConnectionState() == PeerConnection.IceConnectionState.CLOSED) {
                telephonePeerConnection = new TelephonePeerConnection(remoteSocketId, factory, rtcConfig, this);
                telephonePeerConnection.getPeerConnection().addTrack(getLocalAudioTrack());
            } else {
                telephonePeerConnection.getPeerConnection().close();
                telephonePeerConnection = new TelephonePeerConnection(remoteSocketId, factory, rtcConfig, this);
                telephonePeerConnection.getPeerConnection().addTrack(getLocalAudioTrack());
            }
        }


        return telephonePeerConnection.getPeerConnection();
    }


    public static TelephoneCall obtainCall(String remoteName) {
        synchronized (LOCK) {
            if (null == instance) {
                instance = new TelephoneCall();
            }
            instance.remoteName = remoteName;
            instance.status = STATELESS;
            return instance;
        }
    }

    /**
     * 创建ice连接
     */
    private void createConnection() {
        createRtcConfig();
    }

    /**
     * 创建音频约束
     */
    private void createMediaConstraint() {
        audioConstraints = new MediaConstraints();
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_ECHO_CANCELLATION_CONSTRAINT, "true"));
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_AUTO_GAIN_CONTROL_CONSTRAINT, "true"));
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_HIGH_PASS_FILTER_CONSTRAINT, "true"));
        audioConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair(AUDIO_NOISE_SUPPRESSION_CONSTRAINT, "true"));
    }


    /**
     * 创建sdp约束
     */
    private void createSdpMediaConstraint() {
        //SDP createOffer  createAnswer
        sdpMediaConstraints = new MediaConstraints();
        sdpMediaConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
                "OfferToReceiveVideo", "true"));
        sdpMediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
    }

    private void createLocalAudioSource() {
        localAudioSource = factory.createAudioSource(audioConstraints);
    }

    private void createLocalAudioTrack() {
        localAudioTrack = factory.createAudioTrack(AUDIO_TRACK_ID, localAudioSource);
        localAudioTrack.setEnabled(true);
    }


    private void createRtcConfig() {
        rtcConfig =
                new PeerConnection.RTCConfiguration(iceServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;
        // Enable DTLS for normal calls and disable for loopback calls.
        rtcConfig.enableDtlsSrtp = true;
        rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN;
    }


    public void call() {
        synchronized (LOCK) {
            status = MAKE_CALL;
            signallingTransfer.makeCall(DataUtils.getTelName(), remoteName);
            type = android.provider.CallLog.Calls.OUTGOING_TYPE;
            callLog = new RecentCallLog();
            if (telephone == null) {
                intentUnits.startCall(IntentProvider.getCallProvider(remoteName).getIntent(TraditionSynchronise.getContext()));
            }
            NotificationMgr.getInstance().setAudioMode(MODE_IN_COMMUNICATION);
            start();
        }
    }

    /**
     * hang up
     */
    public void hangUp() {
        synchronized (LOCK) {
            cancel();
            if (callLog != null) {
                status = HANG_UP;
                LogUtil.d(TAG, "hangUp: ");
                DataUtils.insertCallLog(callLog, type, remoteName);
                cancel();
                try {
                    telephone.closeCommunicateTelephoneWindow();
                    telephone = null;
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "hangUp: closeCommunicateTelephoneWindow fail" + Log.getStackTraceString(e));
                }

                getSignallingTransfer().leaveRoom(DataUtils.getTelName(), remoteName);
                getSignallingTransfer().exitRoom(getLocalSocketId(), getRoom());

                peerClose();

                callLog = null;
                status = STATELESS;
            }
        }
    }

    public void answer() {
        synchronized (LOCK) {
            LogUtil.d(TAG, "answer: ");
            if (callLog != null) {
                cancel();
                status = ANSWER;
                //停止通知
                NotificationMgr.getInstance().stopNotification();
                //关闭扬声器
                NotificationMgr.getInstance().setAudioMode(MODE_IN_COMMUNICATION);
                //通知对方同意该电话
                getSignallingTransfer().consentCall(getRemoteName(), DataUtils.getTelName());
                //进入房间
                getSignallingTransfer().createAndJoinRoom(getRemoteName());
                LogUtil.e(TAG, "answer: create android join room : " + getRemoteName());
                //启动接听电话界面
                intentUnits.startCall(IntentProvider.getCallProvider2(remoteName).getIntent(TraditionSynchronise.getContext()));
                //设置日志类型
                type = android.provider.CallLog.Calls.INCOMING_TYPE;


                try {
                    if (communicateTelephone != null)
                        communicateTelephone.closeIncomingTelegramWindow();
                    communicateTelephone = null;
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "remoteHangUp: closeCommunicateTelephoneWindow fail" + Log.getStackTraceString(e));
                }

                if (telephone != null) {
                    try {
                        telephone.startStartTime();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //对方先挂断
                try {
                    if (telephone != null)
                        telephone.closeCommunicateTelephoneWindow();
                    telephone = null;
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "answer: closeCommunicateTelephoneWindow fail" + Log.getStackTraceString(e));
                }
                peerClose();
            }
        }
    }

    /**
     * 对方挂电话
     */
    public void remoteHangUp() {

        synchronized (LOCK) {
            LogUtil.d(TAG, "remoteHangUp: ");
            status = REMOTE_HANG_UP;
            if (callLog != null) {
                cancel();
                NotificationMgr.getInstance().stopNotification();
                if (type == android.provider.CallLog.Calls.MISSED_TYPE) {
                    try {
                        if (communicateTelephone != null)
                            communicateTelephone.closeIncomingTelegramWindow();
                        communicateTelephone = null;
                    } catch (RemoteException e) {
                        LogUtil.d(TAG, "remoteHangUp: closeCommunicateTelephoneWindow fail" + Log.getStackTraceString(e));
                    }
                } else {
                    try {
                        if (telephone != null)
                            telephone.closeCommunicateTelephoneWindow();
                        telephone = null;
                    } catch (RemoteException e) {
                        LogUtil.d(TAG, "remoteHangUp: closeCommunicateTelephoneWindow fail" + Log.getStackTraceString(e));
                    }
                    peerClose();
                }

                DataUtils.insertCallLog(callLog, type, remoteName);


                callLog = null;


                if (null != getTelephonePeerConnection())
                    getTelephonePeerConnection().onStop();
            }
            if (getSignallingTransfer() != null)
                getSignallingTransfer().exitRoom(getLocalSocketId(), getRoom());
            TelephoneThreadDispatcher.getInstance().executeDelay(() -> status = STATELESS, 400);
        }
    }

    /**
     * 对方接听电话
     */
    public void remoteAnswer() {
        synchronized (LOCK) {
            LogUtil.d(TAG, "remoteAnswer: ");
            status = REMOTE_ANSWER;

            if (callLog != null) {
                if (telephone != null) {
                    try {
                        telephone.startStartTime();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                cancel();

            } else {
                status = STATELESS;
            }
            if (null != getTelephonePeerConnection())
                getTelephonePeerConnection().onStop();
        }
    }

    /**
     * 占线
     */
    public void busyRoom() {
        synchronized (LOCK) {
            if (callLog != null) {
                cancel();
                status = BUSY_ROOM;
                DataUtils.insertCallLog(callLog, type, remoteName);
                try {
                    if (telephone != null)
                        telephone.closeCommunicateTelephoneWindow();
                    telephone = null;
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "busyRoom: closeCommunicateTelephoneWindow exception " + Log.getStackTraceString(e));
                }
                cancel();

                peerClose();

                getSignallingTransfer().exitRoom(getLocalSocketId(), getRoom());
                callLog = null;
                status = STATELESS;
            }
        }
    }

    /**
     * 对方拒绝接听
     */
    public void remoteRefuseAnswer() {
        synchronized (LOCK) {
            if (callLog != null) {
                status = REMOTE_REFUSE_ANSWER;
                cancel();
                DataUtils.insertCallLog(callLog, type, remoteName);
                try {
                    if (telephone != null)
                        telephone.closeCommunicateTelephoneWindow();
                    telephone = null;
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "remoteRefuseAnswer: closeCommunicateTelephoneWindow exception " + Log.getStackTraceString(e));
                }

                peerClose();

                callLog = null;

                status = STATELESS;
            }
        }
    }


    /**
     * 拒绝通话
     */
    public void refuseAnswer() {
        synchronized (LOCK) {
            cancel();
            if (callLog != null) {
                status = REFUSE_ANSWER;
                try {
                    if (communicateTelephone != null)
                        communicateTelephone.closeIncomingTelegramWindow();
                    communicateTelephone = null;
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "remoteHangUp: closeCommunicateTelephoneWindow fail" + Log.getStackTraceString(e));
                }

                NotificationMgr.getInstance().stopNotification();
                getSignallingTransfer().turnDown(remoteName, DataUtils.getTelName());

                peerClose();

                DataUtils.insertCallLog(callLog, type, remoteName);

                callLog = null;

                getSignallingTransfer().exitRoom(getLocalSocketId(), getRoom());

                status = STATELESS;

            }
        }
    }

    /**
     * 超时
     */
    public void remoteTimeout() {
        synchronized (LOCK) {
            if (callLog != null) {
                LogUtil.d(TAG, "remoteTimeout: ");
                status = REMOTE_TIMEOUT;
                if (telephone != null) {
                    try {
                        telephone.closeCommunicateTelephoneWindow();
                        telephone = null;
                    } catch (Exception e) {
                        LogUtil.d(TAG, "remoteRefuseAnswer: closeCommunicateTelephoneWindow exception " + Log.getStackTraceString(e));
                    }


                }

                if (communicateTelephone != null) {
                    try {
                        communicateTelephone.closeIncomingTelegramWindow();
                        communicateTelephone = null;
                    } catch (Exception e) {
                        LogUtil.d(TAG, "remoteRefuseAnswer: closeCommunicateTelephoneWindow exception " + Log.getStackTraceString(e));
                    }

                }

                DataUtils.insertCallLog(callLog, type, remoteName);
                getSignallingTransfer().leaveRoom(DataUtils.getTelName(), remoteName);

                peerClose();

                callLog = null;

                getSignallingTransfer().exitRoom(getLocalSocketId(), getRoom());
                status = STATELESS;

            }
        }
    }


    public void peerClose() {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            if (telephonePeerConnection != null &&
                    (telephonePeerConnection.getPeerConnection().iceConnectionState() != PeerConnection.IceConnectionState.CLOSED ||
                            telephonePeerConnection.getPeerConnection().iceConnectionState() != PeerConnection.IceConnectionState.NEW)) {
                LogUtil.d(TAG, "peerClose: telephonePeerConnection status " + telephonePeerConnection.getPeerConnection().iceConnectionState());
                if (null != getTelephonePeerConnection())
                    getTelephonePeerConnection().onStop();
                telephonePeerConnection.getPeerConnection().close();
                System.gc();
                LogUtil.d(TAG, "peerClose: telephonePeerConnection start close");
            }

        }, TelephoneThreadDispatcher.DispatcherType.WORK);
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getLocalSocketId() {
        return localSocketId;
    }

    public void setLocalSocketId(String localSocketId) {
        this.localSocketId = localSocketId;
    }

    public MediaConstraints getSdpMediaConstraints() {
        return sdpMediaConstraints;
    }

    public AudioSource getLocalAudioSource() {
        return localAudioSource;
    }

    public AudioTrack getLocalAudioTrack() {
        return localAudioTrack;
    }


    public TelephoneCall setSipTransfer(SignallingTransfer signallingTransfer) {
        this.signallingTransfer = signallingTransfer;
        return this;
    }

    public SignallingTransfer getSignallingTransfer() {
        return signallingTransfer;
    }

    public String getRemoteSocketId() {
        return remoteSocketId;
    }

    public void attachCommunicateWindow(TelephoneCommunicateTelephone telephone) {
        this.telephone = telephone;
    }

    public void attachIncomingTelegram(TelephoneIncomingTelegram telephone) {
        this.communicateTelephone = telephone;
        type = android.provider.CallLog.Calls.MISSED_TYPE;
        callLog = new RecentCallLog();
        start();
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        TelephoneThreadDispatcher.getInstance().execute(this::remoteTimeout, TelephoneThreadDispatcher.DispatcherType.WORK);
    }


    public TelephoneIncomingTelegram getCommunicateTelephone() {
        return communicateTelephone;
    }

    public TelephoneCommunicateTelephone getTelephone() {
        return telephone;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public int getStatus() {
        return status;
    }

    public TelephonePeerConnection getTelephonePeerConnection() {
        return telephonePeerConnection;
    }


    public RecentCallLog getCallLog() {
        return callLog;
    }
}
