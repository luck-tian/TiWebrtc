package com.hhtc.dialer.call.notifiy;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;


import com.hhtc.dialer.NotificationReceiver;
import com.hhtc.dialer.R;
import com.hhtc.dialer.data.tradition.TraditionSynchronise;
import com.hhtc.dialer.utils.IntentProvider;
import com.hhtc.dialer.utils.intentUnits;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.media.AudioManager.MODE_IN_COMMUNICATION;
import static android.media.AudioManager.MODE_NORMAL;

public class NotificationMgr {

    private static final String TAG = NotificationMgr.class.getSimpleName();

    private static int CALL_NOTIFICATION_ID = 80;
    private static final String CALL_CHANNEL_ID = "CALL_NOTIFY_ID";
    private static final String CALL_CHANNEL_NAME = "CALL_NOTIFY_NAME";
    private static final Object LOCK = new Object();
    private static NotificationMgr instance;

    Uri alert;
    MediaPlayer mMediaPlayer;

    private HandlerThread keyguardSecure = new HandlerThread("keyguardSecure");
    private Handler handler;

    private String remoteName;

    private NotificationMgr() {
        keyguardSecure.start();
        handler = new Handler(keyguardSecure.getLooper());
        initTinkleBells();
    }


    public static NotificationMgr getInstance() {
        synchronized (TAG) {
            if (instance == null) {
                instance = new NotificationMgr();
            }
        }
        return instance;

    }


    public void incomingTelegram(String room) {
        lightenScreen();
        disableKeyguard();
        startTinkleBells();
        boolean keyguardSecure = isKeyguardSecure();
        if (keyguardSecure) {
            //有密码 则显示通知
            showCallNotification(room);
            this.remoteName = room;
            startRun.run();
        } else {
            intentUnits.startCall(IntentProvider.getTelegramProvider(room).getIntent(TraditionSynchronise.getContext()));
        }
    }


    private Runnable startRun = new Runnable() {
        @Override
        public void run() {
            synchronized (LOCK) {
                if (isKeyguardSecure()) {
                    handler.removeCallbacks(startRun);
                    cancelNotification();
                    intentUnits.startCall(IntentProvider.getTelegramProvider(remoteName).getIntent(TraditionSynchronise.getContext()));
                } else {
                    handler.removeCallbacks(startRun);
                    handler.postDelayed(startRun, 500);
                }
            }
        }
    };


    /**
     * 通知通知
     */
    public void stopNotification() {
        handler.removeCallbacks(startRun);
        cancelNotification();
        stopTinkleBells();
    }

    /**
     * 点亮屏幕
     */
    private void lightenScreen() {
        PowerManager mPowerManager = (PowerManager) TraditionSynchronise.getContext().getSystemService(POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire(5 * 1000);
        mWakeLock.release();
    }

    /**
     * 解锁  没有指纹和密码的情况下
     */
    private void disableKeyguard() {
        KeyguardManager km = (KeyguardManager) TraditionSynchronise.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        kl.disableKeyguard();
    }

    /**
     * 是否是锁频
     */
    private boolean isKeyguardSecure() {
        KeyguardManager km = (KeyguardManager) TraditionSynchronise.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        boolean keyguardSecure = km.isKeyguardSecure();
        boolean flag = km.inKeyguardRestrictedInputMode();
        return keyguardSecure && flag;
    }


    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) TraditionSynchronise.getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(CALL_NOTIFICATION_ID);
    }

    public synchronized void showCallNotification(String room) {
        NotificationManager notificationManager = (NotificationManager) TraditionSynchronise.getContext().getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CALL_CHANNEL_ID, CALL_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(TraditionSynchronise.getContext(), CALL_CHANNEL_ID);
        Intent intent = new Intent(TraditionSynchronise.getContext().getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TraditionSynchronise.getContext(), 0, intent, 0);
        builder.setContentTitle("Do you have a telephone " + room)
                .setContentIntent(pendingIntent)
                .setTicker("Do you have a telephone " + room)
                .setWhen(System.currentTimeMillis())
                .setChannelId(CALL_CHANNEL_ID)
                .setSmallIcon(R.drawable.noti_icon)
                .setDefaults(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (notificationManager != null) {
            notificationManager.notify(CALL_NOTIFICATION_ID, notification);
        }

    }


    /**
     * 初始化铃声
     */
    private void initTinkleBells() {
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(TraditionSynchronise.getContext(), alert);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始播放铃声
     */
    private synchronized void startTinkleBells() {
        setAudioMode(MODE_NORMAL);
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
    }

    /**
     * 停止播放铃声
     */
    public synchronized void stopTinkleBells() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void setAudioMode(int mode) {
        if (mode != MODE_NORMAL && mode != MODE_IN_COMMUNICATION) {
            return;
        }
        AudioManager audioManager = (AudioManager) TraditionSynchronise.getContext().getSystemService(Context.AUDIO_SERVICE);
        if (mode == MODE_NORMAL) {
            audioManager.setSpeakerphoneOn(true);//打开扬声器
        } else if (mode == MODE_IN_COMMUNICATION) {
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
        }
        audioManager.setMode(mode);

    }


    public String getRemoteName() {
        return remoteName;
    }
}
