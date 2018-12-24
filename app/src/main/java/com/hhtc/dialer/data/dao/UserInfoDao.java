package com.hhtc.dialer.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hhtc.dialer.data.bean.UserInfo;

@Dao
public interface UserInfoDao {

    /**
     *
     * @return
     */
    @Query("SELECT * FROM user_name")
    LiveData<UserInfo> loadUserInfo();

    /**
     *
     * @return
     */
    @Query("SELECT * FROM user_name")
    UserInfo loadUserInfoWDF();

    /**
     *
     * @param info
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserInfo(UserInfo info);

    /**
     *
     * @param info
     */
    @Update
    void updateUserInfo(UserInfo info);

    /**
     *
     * @param info
     */
    @Delete
    void deleteUserInfo(UserInfo info);

}
