/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhtc.dialer.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.data.bean.UserInfo;
import com.hhtc.dialer.data.dao.CollectFavoriteDao;
import com.hhtc.dialer.data.dao.DialerContactDao;
import com.hhtc.dialer.data.dao.RecentCallLogDao;
import com.hhtc.dialer.data.dao.UserInfoDao;


@Database(entities = {CollectFavorite.class, DialerContact.class, RecentCallLog.class, UserInfo.class}, version = 3)
public abstract class ToDoDatabase extends RoomDatabase {

    private static ToDoDatabase INSTANCE;

    public abstract CollectFavoriteDao getCollectFavoriteDao();

    public abstract DialerContactDao getDialerContactDao();

    public abstract RecentCallLogDao getRecentCallLogDao();

    public abstract UserInfoDao getUserInfoDao();

    private static final Object sLock = new Object();

    public static ToDoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDatabase.class, "Dialer_Pundxi.db")
                        .addMigrations(migration1_2)
                        .build();
            }
            return INSTANCE;
        }
    }

    private static Migration migration1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE dialer_contact ADD COLUMN contact_favorite INTEGER NOT NULL DEFAULT 0");
        }
    };

}
