package com.hhtc.dialer.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hhtc.dialer.data.bean.CollectFavorite;

import java.util.List;

@Dao
public interface CollectFavoriteDao {

    /**
     * 查询所有收藏
     *
     * @return
     */
    @Query("SELECT * FROM collect_favorite")
    LiveData<List<CollectFavorite>> loadFavoriteAll();

    /**
     *
     * @param name
     * @param tel
     * @return
     */
    @Query("SELECT * FROM collect_favorite WHERE favorite_name=:name AND favorite_tel=:tel AND favorite_type=:type")
    CollectFavorite loadFavoriteTradition(String name, String tel,int type);

    /**
     * @param id
     * @return
     */
    @Query("SELECT * FROM collect_favorite WHERE favorite_id=:id")
    CollectFavorite loadFavoriteById(long id);

    /**
     * 插入
     *
     * @param favorite
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(CollectFavorite favorite);

    /**
     * 更新
     *
     * @param collectFavorite
     * @return
     */
    @Update
    int updateFavorite(CollectFavorite collectFavorite);

    /**
     * 删除
     *
     * @param collectFavorite
     */
    @Delete
    void deleteFavorite(CollectFavorite collectFavorite);

}
