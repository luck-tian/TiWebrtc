package com.hhtc.dialer.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.bean.RecentCallLog;

import java.util.List;

@Dao
public interface RecentCallLogDao {


    /**
     * 查询所有通话记录
     *
     * @return
     */
    @Query("SELECT * FROM recent_call_log ORDER BY recent_time DESC")
    LiveData<List<RecentCallLog>> loadCallLogAll();

    /**
     * 更加名字查询所有通话记录
     *
     * @param name
     * @return
     */
    @Query("SELECT * FROM recent_call_log WHERE recent_name=:name")
    List<RecentCallLog> loadCallLogByName(String name);

    /**
     * 查询传统短话记录是否存在
     * @param time
     * @param type
     * @return
     */
    @Query("SELECT * FROM recent_call_log WHERE recent_time=:time AND recent_type=:type")
    RecentCallLog loadCallLogTradition(long time, int type);

    /**
     * 插入
     *
     * @param callLog
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecent(RecentCallLog callLog);

    /**
     * 更新
     *
     * @param callLog
     * @return
     */
    @Update
    void updateRecent(RecentCallLog callLog);

    /**
     * 删除
     *
     * @param callLog
     */
    @Delete
    void deleteRecent(RecentCallLog callLog);

}
