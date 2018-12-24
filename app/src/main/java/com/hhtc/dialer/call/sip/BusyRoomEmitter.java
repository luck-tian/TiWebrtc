package com.hhtc.dialer.call.sip;

import android.widget.Toast;


import com.hhtc.dialer.R;
import com.hhtc.dialer.call.emitter.BaseEmitter;
import com.hhtc.dialer.call.emitter.EmitterTemp;
import com.hhtc.dialer.data.tradition.TraditionSynchronise;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

/**
 * 对方正在通话中 显示占线
 */
public class BusyRoomEmitter extends BaseEmitter {

    public BusyRoomEmitter(EmitterTemp... emitterTemps) {
        super(emitterTemps);
    }

    @Override
    public void pushData(JSONObject data) throws JSONException {
        TelephoneThreadDispatcher.getInstance().execute(() -> Toast.makeText(TraditionSynchronise.getContext(), R.string.other_person_phone, Toast.LENGTH_LONG).show(), TelephoneThreadDispatcher.DispatcherType.UI);
        TelephoneThreadDispatcher.getInstance().execute(getTelephoneCall()::busyRoom, TelephoneThreadDispatcher.DispatcherType.WORK);
    }

    @Override
    public void bindClient(Socket client) {
        client.on("busyRoom", getListener());
        TAG = "busyRoom";
        super.bindClient(client);
    }
}
