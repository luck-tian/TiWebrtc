package com.hhtc.dialer.call.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * call phone to remote bind service
 *
 */
public class MakeCallService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
