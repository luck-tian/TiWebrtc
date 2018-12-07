package com.hhtc.dialer.main.recent;

import com.hhtc.dialer.data.bean.RecentCallLog;

public class RecentModel {

    private RecentCallLog callLog;

    private int type;

    public int getType() {
        return type;
    }

    public void setCallLog(RecentCallLog callLog) {
        this.callLog = callLog;
    }

    public RecentCallLog getCallLog() {
        return callLog;
    }
}
