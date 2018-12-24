package com.hhtc.dialer.call.emitter;


import com.hhtc.dialer.call.client.TelephoneCall;

import io.socket.client.Socket;

public class EmitterTemp {

    EmitterTemp[] emitterTemps;

    public EmitterTemp(EmitterTemp... emitterTemps) {
        this.emitterTemps = emitterTemps;
    }

    public void accept(EmitterVisitor visitor) {
        for (EmitterTemp temp : emitterTemps) {
            temp.accept(visitor);
        }
    }

    public void bindCall(TelephoneCall telephoneCall) {
        for (EmitterTemp temp : emitterTemps) {
            temp.bindCall(telephoneCall);
        }
    }

    public void bindClient(Socket client) {
        for (EmitterTemp temp : emitterTemps) {
            temp.bindClient(client);
        }
    }
}
