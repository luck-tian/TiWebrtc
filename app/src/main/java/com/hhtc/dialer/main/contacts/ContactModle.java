package com.hhtc.dialer.main.contacts;

import com.hhtc.dialer.data.bean.DialerContact;

public class ContactModle {

    /**
     * 数据实体
     */
    private DialerContact dialerContact;

    /**
     * 分类
     */
    private String classify;

    public DialerContact getDialerContact() {
        return dialerContact;
    }

    public void setDialerContact(DialerContact dialerContact) {
        this.dialerContact = dialerContact;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    @Override
    public String toString() {
        return dialerContact.toString();
    }
}
