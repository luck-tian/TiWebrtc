package com.hhtc.dialer.call.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.hhtc.dialer.R;
import com.hhtc.dialer.TelephoneCommunicateTelephone;
import com.hhtc.dialer.TelephoneIncomingTelegram;
import com.hhtc.dialer.call.client.TelephoneCall;
import com.hhtc.dialer.call.client.TelephoneClient;
import com.hhtc.dialer.call.util.DataUtils;
import com.hhtc.dialer.data.tradition.TraditionSynchronise;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.LogUtil;

import org.webrtc.PeerConnection;

import java.util.Objects;

import static com.hhtc.dialer.call.client.TelephoneCall.MAKE_CALL;
import static com.hhtc.dialer.call.client.TelephoneCall.REMOTE_HANG_UP;
import static com.hhtc.dialer.call.client.TelephoneCall.STATELESS;


/**
 * long time background phone server for call
 */
public class TelephoneService extends Service {

    private static final String TAG = TelephoneService.class.getSimpleName();

    public static Uri URI_USER = new Uri.Builder()
            .scheme("content")
            .authority("com.hhtc.dialer.user.provider")
            .query("user")
            .build();

    /**
     * Signaling service address (ip address)
     */
    private static String SIP_SERVICE_ADDRESS = "https://xxxxxx:";
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
                LogUtil.d("TelephoneCall", "openCommunicateTelephoneWindow: ");
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
        TraditionSynchronise.setContext(getApplicationContext());
        showNotification();
        telephoneClient = new TelephoneClient(getApplication(), SIP_SERVICE_ADDRESS);
        obtainUser();
    }

    private void obtainUser() {
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(URI_USER, true, new UserContentObserver(TelephoneThreadDispatcher.getInstance().getHandler(), this::changedUserInfo));
        @SuppressLint("Recycle") Cursor query = resolver.query(URI_USER, null, null, null, null);
        obtainUserInfo(query);
    }

    private void obtainUserInfo(Cursor query) {
        if (Objects.requireNonNull(query).moveToNext()) {
            DataUtils.userName = query.getString(query.getColumnIndex("user_name"));
            telephoneClient.createAck();
        }
        query.close();
    }

    private void changedUserInfo(boolean change) {
        ContentResolver resolver = getContentResolver();
        @SuppressLint("Recycle") Cursor query = resolver.query(URI_USER, null, null, null, null);
        obtainUserInfo(query);
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
                LogUtil.d(TAG, "localMakeCall: " + remoteName + " telephoneClient.getCall().getStatus(): " + telephoneClient.getCall().getStatus());
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
        LogUtil.d(TAG, "localRefuseAnswer: " + telephoneClient.getCall().getRemoteName());
        telephoneClient.refuseAnswer();
    }

    /**
     * 接听电话
     */
    private void localAnswer() {
        LogUtil.e(TAG, "localAnswer: " + telephoneClient.getCall().getRemoteName());
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
