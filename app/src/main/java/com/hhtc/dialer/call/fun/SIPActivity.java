package com.hhtc.dialer.call.fun;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.TelephoneCall;
import com.hhtc.dialer.TelephoneCommunicateTelephone;
import com.hhtc.dialer.call.service.TelephoneService;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.IntentProvider;
import com.hhtc.dialer.utils.LogUtil;
import com.hhtc.dialer.utils.SizeUtils;
import com.hhtc.dialer.utils.intentUnits;
import com.hhtc.dialer.view.CallTimerView;
import com.hhtc.dialer.view.SwitchIconView;
import com.hhtc.dialer.window.SIPWindow;

import java.util.Objects;

import static android.media.AudioManager.MODE_IN_COMMUNICATION;
import static android.media.AudioManager.MODE_NORMAL;

public class SIPActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SIPActivity";
    public static final String REMOTE_NAME = "remoteName";
    public static final String START_TIME = "startTime";

    private View end_call;
    private SwitchIconView call_hone_add_contacts, call_hone_address_book, call_hone_microphone, call_hone_number_keys, call_hone_video, call_hone_voice;
    private TextView hostName;
    private SIPWindow sipWindow;
    private CallTimerView call_timer;
    private String remoteName;

    private boolean closeCommunicateTelephoneWindow;
    private TelephoneCommunicateTelephone telephoneCommunicateTelephone = new TelephoneCommunicateTelephone.Stub() {
        @Override
        public void closeCommunicateTelephoneWindow() throws RemoteException {
            closeCommunicateTelephoneWindow = true;
            finishAndRemoveTask();
        }

        @Override
        public void startStartTime() throws RemoteException {
            TelephoneThreadDispatcher.getInstance().execute(() -> {
                if (!isFinishing() && !call_timer.isStart()) {
                    call_timer.setVisibility(View.VISIBLE);
                    call_timer.start(0);
                }
            }, TelephoneThreadDispatcher.DispatcherType.UI);

        }
    };

    private TelephoneCall telephoneCall;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            telephoneCall = TelephoneCall.Stub.asInterface(service);
            try {
                telephoneCall.openCommunicateTelephoneWindow(telephoneCommunicateTelephone, remoteName);
            } catch (RemoteException e) {
                LogUtil.d(TAG, "onServiceConnected: openCommunicateTelephoneWindow fail");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            telephoneCall = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (sipWindow != null) {
            sipWindow.hideFloatWindow();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip);
        initView();
        buildIntentUrl();
        Intent intent = new Intent(this, TelephoneService.class);
        intent.setAction(intentUnits.ACTION_TELEPHONE);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        checkDrawOverlays();
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void buildIntentUrl() {
        Uri data = getIntent().getData();
        if (data != null) {
            remoteName = data.getQuery();
            String authority = data.getAuthority();
            if(Objects.nonNull(authority)){
                if (!isFinishing() && !call_timer.isStart()) {
                    call_timer.setVisibility(View.VISIBLE);
                    call_timer.start(0);
                }
            }
        } else {
            remoteName = getIntent().getStringExtra(REMOTE_NAME);
        }
        hostName.setText(remoteName);
    }

    private void checkDrawOverlays() {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 10);
        } else {
            initWindow();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Settings.canDrawOverlays(this)) {
                initWindow();
            }
        }
    }

    private void initWindow() {
        if (sipWindow == null)
            sipWindow = new SIPWindow(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (sipWindow != null) {
            sipWindow.hideFloatWindow();
        }
    }

    public void initView() {
        end_call = findViewById(R.id.end_call);
        call_hone_add_contacts = findViewById(R.id.call_hone_add_contacts);
        call_hone_address_book = findViewById(R.id.call_hone_address_book);
        call_hone_microphone = findViewById(R.id.call_hone_microphone);
        call_hone_number_keys = findViewById(R.id.call_hone_number_keys);
        call_hone_video = findViewById(R.id.call_hone_video);
        call_hone_voice = findViewById(R.id.call_hone_voice);
        hostName = findViewById(R.id.remoteName);

        onClick(call_hone_add_contacts
                , call_hone_address_book
                , call_hone_microphone
                , call_hone_number_keys
                , call_hone_video
                , call_hone_voice);
        end_call.setOnClickListener(v -> {
            sipWindow = null;
            try {
                if (telephoneCall != null)
                    telephoneCall.hangUp();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        call_timer = findViewById(R.id.call_timer);

        boolean startTime = getIntent().getBooleanExtra(START_TIME, false);
        if (startTime) {
            if (!call_timer.isStart()) {
                call_timer.setVisibility(View.VISIBLE);
                call_timer.start(0);
            }
        }
    }

    private void onClick(View... vies) {
        for (View vi : vies) {
            vi.setOnClickListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        call_timer.stop();
        if (sipWindow != null)
            sipWindow.hideFloatWindow();
        sipWindow = null;
        if (!closeCommunicateTelephoneWindow) {
            if (telephoneCall != null) {
                try {
                    telephoneCall.hangUp();
                } catch (RemoteException e) {
                    LogUtil.d(TAG, "onDestroy: telephoneCall is null");
                }
            }
        }
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        showWindow();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call_hone_number_keys) {
        } else if (v.getId() == R.id.call_hone_address_book
                || v.getId() == R.id.call_hone_add_contacts
                || v.getId() == R.id.call_hone_video) {
            SwitchIconView sw = (SwitchIconView) v;
            sw.switchState();
        } else if (v.getId() == R.id.call_hone_voice) {
            SwitchIconView sw = (SwitchIconView) v;
            if (sw.switchState()) {
                switchAudio(sw.isIconEnabled());
            }
        } else if (v.getId() == R.id.call_hone_microphone) {
            SwitchIconView sw = (SwitchIconView) v;
            sw.switchState();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                telephoneCall.hangUp();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showWindow() {
        if (sipWindow != null) {
            sipWindow.showFloatWindow(remoteName);
        }
    }

    public void switchAudio(boolean sw) {
        if (sw) {
            setAudioMode(MODE_NORMAL);
        } else {
            setAudioMode(MODE_IN_COMMUNICATION);
        }
    }

    private void setAudioMode(int mode) {
        if (mode != MODE_NORMAL && mode != MODE_IN_COMMUNICATION) {
            return;
        }
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (mode == MODE_NORMAL) {
            audioManager.setSpeakerphoneOn(true);//打开扬声器
        } else if (mode == MODE_IN_COMMUNICATION) {
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
        }
        audioManager.setMode(mode);

    }
}
