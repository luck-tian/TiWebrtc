// TelephoneCall.aidl
package com.hhtc.dialer;

// Declare any non-default types here with import statements
import com.hhtc.dialer.TelephoneCommunicateTelephone;
import com.hhtc.dialer.TelephoneIncomingTelegram;
interface TelephoneCall {
     //打电话
        void makeCall(String remoteName);
        //挂电话
        void hangUp();
        //拒绝接听
        void refuseAnswer();
        //接听电话
        void answer();

        //打开通话窗口
        void openCommunicateTelephoneWindow(TelephoneCommunicateTelephone telephone,String remoteName);

        //打开来电窗口
        void openTelephoneIncomingTelegramWindow(TelephoneIncomingTelegram telephone,String remoteName);
}
