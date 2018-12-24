package com.hhtc.dialer.call.util;


import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.data.tradition.TraditionSynchronise;

import static com.hhtc.dialer.data.bean.RecentCallLog.TEL_TYPE;

public class DataUtils {

    public static String userName;

    public static String getTelName() {
        return userName;
    }

    public static void insertCallLog(RecentCallLog callLog, int type, String remoteName) {
        if (null != callLog) {
            RecentCallLog log = callLog;
            log.setCallType(type);
            log.setName(remoteName);
            log.setCallTime(System.currentTimeMillis());
            log.setTelOrVideo(TEL_TYPE);
            Injection.provideTasksRepository(TraditionSynchronise.getContext()).insertRecent(log);
        }

    }
}
