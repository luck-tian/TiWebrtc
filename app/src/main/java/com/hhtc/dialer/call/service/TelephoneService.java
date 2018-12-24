package com.hhtc.dialer.call.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hhtc.dialer.R;
import com.hhtc.dialer.TelephoneCommunicateTelephone;
import com.hhtc.dialer.TelephoneIncomingTelegram;
import com.hhtc.dialer.call.client.TelephoneCall;
import com.hhtc.dialer.call.client.TelephoneClient;
import com.hhtc.dialer.call.util.DataUtils;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.webrtc.PeerConnection;

import static com.hhtc.dialer.call.client.TelephoneCall.MAKE_CALL;
import static com.hhtc.dialer.call.client.TelephoneCall.REMOTE_HANG_UP;
import static com.hhtc.dialer.call.client.TelephoneCall.STATELESS;


/**
 * long time background phone server for call
 */
public class TelephoneService extends Service {

    private static final String TAG = TelephoneService.class.getSimpleName();
    /**
     * Signaling service address (ip address)
     */
    private static String SIP_SERVICE_ADDRESS = "https://13.229.204.253:8443";
    private static int PUSH_NOTIFICATION_ID = 8087;
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";

    private TelephoneClient telephoneClient;

    private static final Object LOCK = new Object();

    private com.hhtc.dialer.TelephoneCall.Stub bind = new com.hhtc.dialer.TelephoneCall.Stub() {
        @Override
        public void makeCall(String remoteName) throws RemoteException {
            localMakeCall(remoteName);
        }

        @Override
        public void hangUp() throws RemoteException {
            localHangUp();
        }

        @Override
        public void refuseAnswer() throws RemoteException {
            localRefuseAnswer();
        }

        @Override
        public void answer() throws RemoteException {
            localAnswer();
        }

        @Override
        public void openCommunicateTelephoneWindow(TelephoneCommunicateTelephone telephone, String remoteName) throws RemoteException {
            synchronized (TelephoneCall.LOCK) {
                Log.d("TelephoneCall", "openCommunicateTelephoneWindow: ");
                if (telephoneClient.getCall().getTelephone() != null) {
                    telephone.closeCommunicateTelephoneWindow();
                    TelephoneThreadDispatcher.getInstance().execute(() -> Toast.makeText(getApplicationContext(), R.string.other_person_phone, Toast.LENGTH_SHORT).show(), TelephoneThreadDispatcher.DispatcherType.UI);
                } else if (telephoneClient.getCall().getStatus() == REMOTE_HANG_UP) {
                    telephone.closeCommunicateTelephoneWindow();
                } else {
                    telephoneClient.getCall().attachCommunicateWindow(telephone);
                    if (telephoneClient.getCall().getStatus() < MAKE_CALL) {
                        localMakeCall(remoteName);
                    }
                }
            }
        }

        @Override
        public void openTelephoneIncomingTelegramWindow(TelephoneIncomingTelegram telephone, String remoteName) throws RemoteException {
            TelephoneCall telephoneCall = TelephoneCall.obtainCall(remoteName);
            telephoneClient.attachIncomingTelegram(telephone, telephoneCall);
        }

    };


    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
        telephoneClient = new TelephoneClient(getApplication(), SIP_SERVICE_ADDRESS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephoneClient.free();
    }

    /**
     * 打电话
     */
    private void localMakeCall(String remoteName) {
        synchronized (LOCK) {
            if (TextUtils.isEmpty(remoteName) ||
                    remoteName.equals("null")) {
                TelephoneThreadDispatcher.getInstance().execute(() ->
                        Toast.makeText(getApplicationContext(), R.string.add_contact_input_name_tips,
                                Toast.LENGTH_SHORT).show(), TelephoneThreadDispatcher.DispatcherType.UI);
            } else if (telephoneClient.getCall() != null &&
                    telephoneClient.getCall().getTelephonePeerConnection() != null &&
                    telephoneClient.getCall()
                            .getTelephonePeerConnection()
                            .getPeerConnection()
                            .iceConnectionState() != PeerConnection.IceConnectionState.CLOSED) {
                //还没有释放资源
                telephoneClient.getCall().peerClose();
                TelephoneThreadDispatcher.getInstance().execute(() -> Toast.makeText(getApplicationContext(), R.string.other_person_phone, Toast.LENGTH_SHORT).show(), TelephoneThreadDispatcher.DispatcherType.UI);
            } else if ((telephoneClient.getCall() != null &&
                    telephoneClient.getCall().getStatus() != STATELESS) ||
                    (TextUtils.equals(remoteName, DataUtils.getTelName()))) {
                //正在打电话
                TelephoneThreadDispatcher.getInstance().execute(() -> Toast.makeText(getApplicationContext(), R.string.other_person_phone, Toast.LENGTH_SHORT).show(), TelephoneThreadDispatcher.DispatcherType.UI);
            } else {
                Log.d(TAG, "localMakeCall: " + remoteName + " telephoneClient.getCall().getStatus(): " + telephoneClient.getCall().getStatus());
                TelephoneCall telephoneCall = TelephoneCall.obtainCall(remoteName);
                telephoneClient.callRemote(telephoneCall);
            }
        }
    }

    /**
     * 挂电话
     */
    private void localHangUp() {
        telephoneClient.getCall().hangUp();
    }

    /**
     * 拒绝接听电话
     */
    private void localRefuseAnswer() {
        Log.d(TAG, "localRefuseAnswer: " + telephoneClient.getCall().getRemoteName());
        telephoneClient.refuseAnswer();
    }

    /**
     * 接听电话
     */
    private void localAnswer() {
        Log.e(TAG, "localAnswer: " + telephoneClient.getCall().getRemoteName());
        telephoneClient.getCall().answer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }


    private synchronized void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PUSH_CHANNEL_ID);
        Intent launchIntentForPackage = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntentForPackage, 0);
        builder.setContentTitle("Phone service")
                .setContentIntent(pendingIntent)
                .setTicker("Phone service")
                .setWhen(System.currentTimeMillis())
                .setChannelId(PUSH_CHANNEL_ID)
                .setDefaults(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        if (notificationManager != null) {
            notificationManager.cancel(PUSH_NOTIFICATION_ID);
        }
        startForeground(PUSH_NOTIFICATION_ID, notification);

    }

}
