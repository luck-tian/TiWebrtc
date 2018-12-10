package com.hhtc.dialer.main.recent;

import com.hhtc.dialer.data.bean.RecentCallLog;

public class RecentModel {

    private String time;

    private RecentCallLog callLog;

    private int type;

    public int getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCallLog(RecentCallLog callLog) {
        this.callLog = callLog;
    }

    public RecentCallLog getCallLog() {
        return callLog;
    }
}
