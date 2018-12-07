package com.hhtc.dialer.data.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import java.util.Objects;

@Entity(tableName = "recent_call_log")
public class RecentCallLog {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recent_id")
    private long mId;

    /**
     * 名字 传统电话名字为电话号码
     */
    @ColumnInfo(name = "recent_name")
    private String mName;

    /**
     * 电话号码
     */
    @ColumnInfo(name = "recent_tel")
    private String mTel;

    /**
     * 是电话还是视频
     */
    @ColumnInfo(name = "recent_telOrVideo")
    private String mTelOrVideo;

    /**
     * 通话时间
     */
    @ColumnInfo(name = "recent_time")
    private long mCallTime;

    /**
     * 头像
     */
    @ColumnInfo(name = "recent_photo")
    private String mPhoto;

    /**
     * 通话时间 接电话 未接电话 拨打电话 3中类型
     */
    @ColumnInfo(name = "recent_type")
    private int mCallType;

    public RecentCallLog(){

    }

    @Ignore
    public RecentCallLog(String name,
                         String telOrVideo,
                         String tel,
                         String photo,
                         long callTime,
                         int mCallType) {
        this.mName = name;
        this.mTel = tel;
        this.mCallTime = callTime;
        this.mCallType = mCallType;
        this.mPhoto = photo;
        this.mTelOrVideo = telOrVideo;
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

    public String getTelOrVideo() {
        return mTelOrVideo;
    }

    public void setTelOrVideo(String mTelOrVideo) {
        this.mTelOrVideo = mTelOrVideo;
    }

    public long getCallTime() {
        return mCallTime;
    }

    public void setCallTime(long mCallTime) {
        this.mCallTime = mCallTime;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public int getCallType() {
        return mCallType;
    }

    public void setCallType(int mCallType) {
        this.mCallType = mCallType;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecentCallLog task = (RecentCallLog) o;
        return Objects.equals(mId, task.mId) &&
                Objects.equals(mName, task.mName) &&
                Objects.equals(mCallType, task.mCallTime) &&
                Objects.equals(mCallTime, task.mCallTime);
    }

    @Override
    public String toString() {
        return "{ name:" + mName + " tel:" + mTel + " call type:" + mCallType + " database id:" + mId + " }";
    }
}
