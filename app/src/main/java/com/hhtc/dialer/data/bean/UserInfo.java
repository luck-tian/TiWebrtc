package com.hhtc.dialer.data.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_name")
public class UserInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private long id;

    @ColumnInfo(name = "user_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
