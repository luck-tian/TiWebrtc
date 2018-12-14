package com.hhtc.dialer.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.data.bean.DialerContact;

import java.util.List;

@Dao
public interface DialerContactDao {


    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM dialer_contact")
    LiveData<List<DialerContact>> loadContactLiveAll();


    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM dialer_contact")
    List<DialerContact> loadContactAll();

    /**
     * 根据id🍵
     *
     * @param id
     * @return
     */
    @Query("SELECT * FROM dialer_contact WHERE contact_id=:id")
    LiveData<DialerContact> loadContactById(long id);


    /**
     * 根据Name
     *
     * @param name
     * @return
     */
    @Query("SELECT * FROM dialer_contact WHERE contact_name=:name")
    DialerContact loadContactByName(String name);

    /**
     * 插入
     *
     * @param contact
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(DialerContact contact);

    /**
     * 更新
     *
     * @param contact
     * @return
     */
    @Update
    void updateContact(DialerContact contact);

    /**
     * 删除
     *
     * @param contact
     */
    @Delete
    void deleteContact(DialerContact contact);

}
