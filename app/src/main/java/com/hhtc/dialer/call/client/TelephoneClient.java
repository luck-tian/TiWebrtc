package com.hhtc.dialer.call.client;

import android.content.Context;
import android.util.Log;

import com.hhtc.dialer.TelephoneIncomingTelegram;
import com.hhtc.dialer.call.TrustAllCerts;
import com.hhtc.dialer.call.emitter.AnswerEmitter;
import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.CandidateEmitter;
import com.hhtc.dialer.call.emitter.CreatedRoomEmitter;
import com.hhtc.dialer.call.emitter.JoinedEmitter;
import com.hhtc.dialer.call.emitter.OfferEmitter;
import com.hhtc.dialer.call.sip.BusyRoomEmitter;
import com.hhtc.dialer.call.sip.ConsentCallEmitter;
import com.hhtc.dialer.call.sip.ExitEmitter;
import com.hhtc.dialer.call.sip.LeaveRoomEmitter;
import com.hhtc.dialer.call.sip.MakeCallEmitter;
import com.hhtc.dialer.call.sip.MakeResultEmitter;
import com.hhtc.dialer.call.sip.TurnDownEmitter;
import com.hhtc.dialer.utils.LogUtil;

import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;
import org.webrtc.voiceengine.WebRtcAudioUtils;

import java.security.SecureRandom;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

public class TelephoneClient {

    private static final String TAG = "TelephoneClient";

    private static final String VIDEO_VP8_INTEL_HW_ENCODER_FIELDTRIAL = "WebRTC-IntelVP8/Enabled/";

    private static final String DISABLE_WEBRTC_AGC_FIELDTRIAL =
            "WebRTC-Audio-MinimizeResamplingOnMobile/Enabled/";
    private static final String VIDEO_FLEXFEC_FIELDTRIAL =
            "WebRTC-FlexFEC-03-Advertised/Enabled/WebRTC-FlexFEC-03/Enabled/";
    /**
     * Signaling interaction
     */
    private Socket client;

    private BaseEmitter emitter;

    private PeerConnectionFactory factory;

    private SignallingTransfer signallingTransfer;


    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);


    private Context context;

    private EglBase eglBase;


    public TelephoneClient(Context context, String sipServiceAddress) {
        //创建好Socket
        this.context = context;
        eglBase = EglBase.create();
        createClient(sipServiceAddress);

        //创建webrtc 连接工厂
        createFactory();


        //创建连接事件
        emitter = createEmitter();
        emitter.bindClient(client);
        emitter.bindCall(TelephoneCall.obtainCall(null));
        //创建信号传输
        signallingTransfer = new SignallingTransfer(client);
    }

    /**
     * 保持连接
     */
    public void createAck() {
        executor.scheduleWithFixedDelay(signallingTransfer::connect, 1, 5, TimeUnit.SECONDS);
    }

    private void createClient(String sipServiceAddress) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier((hostname, session) -> true)
                .sslSocketFactory(getSSLSocketFactory(), new TrustAllCerts())
                .build();
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);
        IO.Options opts = new IO.Options();
        opts.callFactory = okHttpClient;
        opts.forceNew = true;
        opts.webSocketFactory = okHttpClient;
        try {
            client = IO.socket(sipServiceAddress, opts);
        } catch (Exception e) {
            LogUtil.i(TAG, "webrtc init bad wdf" + Log.getStackTraceString(e));
        }
    }

    private SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private BaseEmitter createEmitter() {
        return new CreatedRoomEmitter(new JoinedEmitter(),
                new AnswerEmitter(),
                new CandidateEmitter(),
                new OfferEmitter(),
                new BusyRoomEmitter(),
                new ConsentCallEmitter(),
                new LeaveRoomEmitter(),
                new MakeCallEmitter(),
                new MakeResultEmitter(),
                new TurnDownEmitter(),
                new ExitEmitter());
    }


    private void createFactory() {
        final VideoEncoderFactory encoderFactory;
        final VideoDecoderFactory decoderFactory;
        final boolean enableH264HighProfile = false;
        final AudioDeviceModule adm = createJavaAudioDevice();

        encoderFactory = new DefaultVideoEncoderFactory(
                eglBase.getEglBaseContext(), true /* enableIntelVp8Encoder */, enableH264HighProfile);
        decoderFactory = new DefaultVideoDecoderFactory(eglBase.getEglBaseContext());
        //PeerConnectionFactory.initialize
        String fieldTrials = "";
        fieldTrials += VIDEO_FLEXFEC_FIELDTRIAL;
        fieldTrials += VIDEO_VP8_INTEL_HW_ENCODER_FIELDTRIAL;
        fieldTrials += DISABLE_WEBRTC_AGC_FIELDTRIAL;
        //PeerConnectionFactory.initialize
        PeerConnectionFactory.initialize(
                PeerConnectionFactory.InitializationOptions.builder(context)
                        .setFieldTrials(fieldTrials)
                        .setEnableInternalTracer(true)
                        .createInitializationOptions());
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();

        factory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setAudioDeviceModule(adm)
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory)
                .createPeerConnectionFactory();
        TelephoneCall.obtainCall(null).attach(factory);
    }


    private AudioDeviceModule createJavaAudioDevice() {

        boolean acousticEchoCancelerSupported = WebRtcAudioUtils.isAcousticEchoCancelerSupported();
        if (acousticEchoCancelerSupported) {
            LogUtil.d(TAG, "createJavaAudioDevice: acousticEchoCancelerSupported");
            WebRtcAudioUtils.setWebRtcBasedAcousticEchoCanceler(true);
        }

        boolean automaticGainControlSupported = WebRtcAudioUtils.isAutomaticGainControlSupported();
        if (automaticGainControlSupported) {
            LogUtil.d(TAG, "createJavaAudioDevice: automaticGainControlSupported");
            WebRtcAudioUtils.setWebRtcBasedAutomaticGainControl(true);
        }

        boolean noiseSuppressorSupported = WebRtcAudioUtils.isNoiseSuppressorSupported();
        if (noiseSuppressorSupported) {
            LogUtil.d(TAG, "createJavaAudioDevice: noiseSuppressorSupported");
            WebRtcAudioUtils.setWebRtcBasedNoiseSuppressor(true);
        }


        return JavaAudioDeviceModule.builder(context)
                .setUseHardwareAcousticEchoCanceler(true)
                .setUseHardwareNoiseSuppressor(true)
                .setUseStereoInput(true)
                .setSampleRate(8000)
                .setUseStereoOutput(true)
                .setAudioRecordErrorCallback(audioRecordErrorCallback)
                .setAudioTrackErrorCallback(audioTrackErrorCallback)
                .createAudioDeviceModule();
    }


    // Set audio record error callbacks.
    private JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback = new JavaAudioDeviceModule.AudioRecordErrorCallback() {
        @Override
        public void onWebRtcAudioRecordInitError(String errorMessage) {
            LogUtil.e(TAG, "onWebRtcAudioRecordInitError: " + errorMessage);
        }

        @Override
        public void onWebRtcAudioRecordStartError(
                JavaAudioDeviceModule.AudioRecordStartErrorCode errorCode, String errorMessage) {
            LogUtil.e(TAG, "onWebRtcAudioRecordStartError: " + errorCode + ". " + errorMessage);
        }

        @Override
        public void onWebRtcAudioRecordError(String errorMessage) {
            LogUtil.e(TAG, "onWebRtcAudioRecordError: " + errorMessage);
        }
    };

    private JavaAudioDeviceModule.AudioTrackErrorCallback audioTrackErrorCallback = new JavaAudioDeviceModule.AudioTrackErrorCallback() {
        @Override
        public void onWebRtcAudioTrackInitError(String errorMessage) {
            LogUtil.e(TAG, "onWebRtcAudioTrackInitError: " + errorMessage);
        }

        @Override
        public void onWebRtcAudioTrackStartError(
                JavaAudioDeviceModule.AudioTrackStartErrorCode errorCode, String errorMessage) {
            LogUtil.e(TAG, "onWebRtcAudioTrackStartError: " + errorCode + ". " + errorMessage);
        }

        @Override
        public void onWebRtcAudioTrackError(String errorMessage) {
            LogUtil.e(TAG, "onWebRtcAudioTrackError: " + errorMessage);
        }
    };

    public void callRemote(TelephoneCall telephoneCall) {
        emitter.bindCall(telephoneCall);
        telephoneCall.setSipTransfer(signallingTransfer)
                .call();
    }


    public void refuseAnswer() {
        getCall().refuseAnswer();
    }

    public void free() {
        executor.shutdownNow();
        executor = null;
        client.close();
        emitter = null;
        signallingTransfer = null;
    }

    public TelephoneCall getCall() {
        return emitter.getTelephoneCall();
    }

    public SignallingTransfer getSignallingTransfer() {
        return signallingTransfer;
    }


    public void attachIncomingTelegram(TelephoneIncomingTelegram telephone, TelephoneCall telephoneCall) {
        getCall().setSipTransfer(signallingTransfer);
        telephoneCall.attachIncomingTelegram(telephone);
    }


}