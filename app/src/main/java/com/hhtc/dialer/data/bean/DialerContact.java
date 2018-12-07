package com.hhtc.dialer.data.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import java.util.Objects;

@Entity(tableName = "dialer_contact")
public class DialerContact {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    private long mId;

    /**
     * 名字 传统电话名字为电话号码
     */
    @ColumnInfo(name = "contact_name")
    private String mName;

    /**
     * 电话号码
     */
    @ColumnInfo(name = "contact_tel")
    private String mTel;

    /**
     * 短信
     */
    @ColumnInfo(name = "contact_message")
    private String mMessage;

    /**
     * 邮件
     */
    @ColumnInfo(name = "contact_email")
    private String mEmail;

    /**
     * 视频
     */
    @ColumnInfo(name = "contact_video")
    private String mVideo;


    /**
     * 视频
     */
    @ColumnInfo(name = "contact_photo")
    private String mPhoto;


    /**
     * 联系人类型 传统 and 区域块
     */
    @ColumnInfo(name = "contact_type")
    private int mType;

    public DialerContact(){

    }

    @Ignore
    public DialerContact(String name,
                         String tel,
                         String massage,
                         String email,
                         String video,
                         String photo,
                         int type) {
        this.mName = name;
        this.mTel = tel;
        this.mMessage = massage;
        this.mEmail = email;
        this.mPhoto = photo;
        this.mVideo = video;
        this.mType = type;
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getTel() {
        return mTel;
    }

    public void setTel(String mTel) {
        this.mTel = mTel;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getVideo() {
        return mVideo;
    }

    public void setVideo(String mVideo) {
        this.mVideo = mVideo;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DialerContact task = (DialerContact) o;
        return Objects.equals(mId, task.mId) &&
                Objects.equals(mName, task.mName) &&
                Objects.equals(mType, task.mType) &&
                Objects.equals(mTel, task.mTel);
    }

    @Override
    public String toString() {
        return "{ name:" + mName + " tel:" + mTel + " call type:" + mType + " database id:" + mId + " }";
    }
}
