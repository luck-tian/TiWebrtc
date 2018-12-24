package com.hhtc.dialer.call.emitter;

import android.util.Log;


import com.hhtc.dialer.call.client.TelephoneCall;
import com.hhtc.dialer.utils.LogUtil;

import org.json.JSONObject;

import io.socket.emitter.Emitter;

public abstract class BaseEmitter extends EmitterTemp {

    protected String TAG = "BaseEmitter";

    protected TelephoneCall telephoneCall;

    public BaseEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    private Emitter.Listener listener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                LogUtil.e(TAG, "thread id :" + Thread.currentThread().getId() + " pushData: " + data.toString());
                pushData(data);
            } catch (Exception e) {
                LogUtil.e(TAG, "pushData: Exception" + Log.getStackTraceString(e));
            }
        }
    };

    /**
     * server push data to client
     *
     * @param data
     */
    public abstract void pushData(JSONObject data) throws Exception;

    public Emitter.Listener getListener() {
        return listener;
    }

    public TelephoneCall getTelephoneCall() {
        return telephoneCall;
    }

    @Override
    public void bindCall(TelephoneCall telephoneCall) {
        this.telephoneCall = telephoneCall;
        super.bindCall(telephoneCall);

    }
}
