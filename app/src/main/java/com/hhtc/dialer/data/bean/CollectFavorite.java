package com.hhtc.dialer.data.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * 收藏实体类
 */
@Entity(tableName = "collect_favorite")
public class CollectFavorite {

    /**
     * 实体id
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favorite_id")
    private long mId;

    /**
     * 只有联系人有收藏功能 所有名字是必须的
     */
    @NonNull
    @ColumnInfo(name = "favorite_name")
    private String mName;

    /**
     * 语音电话
     */
    @NonNull
    @ColumnInfo(name = "favorite_tel")
    private String mTel;

    /**
     * 语音电话
     */
    @ColumnInfo(name = "favorite_video")
    private String mVideo;

    /**
     * 联系人类型
     */
    @NonNull
    @RecentCallLog.TraditionalOrBlock
    @ColumnInfo(name = "favorite_type")
    private int mType;


    public CollectFavorite() {

    }

    @Ignore
    public CollectFavorite(@NonNull String name,
                           @NonNull String tel,
                           @NonNull int type) {
        this(name, tel, null, type);
    }

    @Ignore
    public CollectFavorite(@NonNull String name,
                           @NonNull String tel,
                           @Nullable String video,
                           @NonNull int type) {
        this.mName = name;
        this.mTel = tel;
        this.mVideo = video;
        this.mType = type;
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String mName) {
        this.mName = mName;
    }

    @NonNull
    public String getTel() {
        return mTel;
    }

    public void setTel(@NonNull String mTel) {
        this.mTel = mTel;
    }

    @Nullable
    public String getVideo() {
        return mVideo;
    }

    public void setVideo(@Nullable String mVideo) {
        this.mVideo = mVideo;
    }

    @NonNull
    public int getType() {
        return mType;
    }

    public void setType(@NonNull int mType) {
        this.mType = mType;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectFavorite task = (CollectFavorite) o;
        return Objects.equals(mId, task.mId) &&
                Objects.equals(mName, task.mName) &&
                Objects.equals(mType, task.mType);
    }

    @Override
    public String toString() {
        return "{ name:" + mName + " tel:" + mTel + " type:" + mType + " database id:" + mId + " }";
    }
}
