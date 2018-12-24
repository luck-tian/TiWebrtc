package com.hhtc.dialer.call.fun;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.hhtc.dialer.R;
import com.hhtc.dialer.TelephoneCall;
import com.hhtc.dialer.TelephoneIncomingTelegram;
import com.hhtc.dialer.call.service.TelephoneService;
import com.hhtc.dialer.utils.IntentProvider;
import com.hhtc.dialer.utils.LogUtil;
import com.hhtc.dialer.utils.intentUnits;
import com.hhtc.dialer.window.IncomingTelegramWindow;

import java.util.Objects;

public class IncomingTelegramActivity extends AppCompatActivity {

    private static final String TAG = "IncomingTelegramActivity";

    private TextView remoteName;

    private IncomingTelegramWindow incomingTelegramWindow;

    TelephoneCall telephoneCall;

    private TelephoneIncomingTelegram telephoneCommunicateTelephone = new TelephoneIncomingTelegram.Stub() {


        @Override
        public void closeIncomingTelegramWindow() throws RemoteException {
            telephoneCall = null;
            finishAndRemoveTask();
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            telephoneCall = TelephoneCall.Stub.asInterface(service);
            try {
                telephoneCall.openTelephoneIncomingTelegramWindow(telephoneCommunicateTelephone, remoteName.getText().toString());
            } catch (RemoteException e) {
                LogUtil.d(TAG, "onServiceConnected: openTelephoneIncomingTelegramWindow is fail");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            telephoneCall = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_telegram);
        initView();
        parsing();
        checkDrawOverlays();
        Intent intent = new Intent(this, TelephoneService.class);
        intent.setAction(intentUnits.ACTION_TELEPHONE);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void checkDrawOverlays() {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 10);
        } else {
            initWindow();
        }
    }

    private void parsing() {
        Uri data = getIntent().getData();
        if (Objects.nonNull(data)) {
            String scheme = Objects.requireNonNull(data).getScheme();
            if (TextUtils.equals(scheme, "telegram")) {
                String remoteName = data.getQuery();
                this.remoteName.setText(remoteName);
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (incomingTelegramWindow != null) {
            LogUtil.e(TAG, "onResume() suspendWindow != null");
            incomingTelegramWindow.hideFloatWindow();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (incomingTelegramWindow != null) {
            incomingTelegramWindow.hideFloatWindow();
        }
    }

    private void initWindow() {
        incomingTelegramWindow = new IncomingTelegramWindow(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        showWindow();
    }

    private void showWindow() {
        if (incomingTelegramWindow != null) {
            incomingTelegramWindow.showFloatWindow(remoteName.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        if (incomingTelegramWindow != null)
            incomingTelegramWindow.hideFloatWindow();
        incomingTelegramWindow = null;
        try {
            if (null != telephoneCall)
                telephoneCall.refuseAnswer();
        } catch (RemoteException e) {
            LogUtil.d(TAG, "onDestroy: telephoneCall is null");
        }
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hangUp(null);
            if (incomingTelegramWindow != null)
                incomingTelegramWindow.hideFloatWindow();
            incomingTelegramWindow = null;
        }
        return super.onKeyDown(keyCode, event);

    }


    private void initView() {
        remoteName = findViewById(R.id.remoteName);
    }

    public void answer(View view) {
        if (incomingTelegramWindow != null)
            incomingTelegramWindow.hideFloatWindow();
        incomingTelegramWindow = null;
        try {
            if (null != telephoneCall)
                telephoneCall.answer();
            telephoneCall = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void hangUp(View view) {
        if (incomingTelegramWindow != null)
            incomingTelegramWindow.hideFloatWindow();
        incomingTelegramWindow = null;
        try {
            if (null != telephoneCall)
                telephoneCall.refuseAnswer();
            telephoneCall = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
