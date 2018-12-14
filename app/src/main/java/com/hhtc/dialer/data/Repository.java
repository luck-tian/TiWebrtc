package com.hhtc.dialer.data;

import android.arch.lifecycle.LiveData;

import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.main.recent.RecentModel;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.List;
import java.util.Objects;

public class Repository {

    private static Repository INSTANCE = null;

    private ToDoDatabase database;

    private Repository(ToDoDatabase database) {
        this.database = database;
    }

    public static Repository getInstance(ToDoDatabase database) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(database);
        }
        return INSTANCE;
    }


    public void loadFavoriteAll(final LoadListLiveCallback<CollectFavorite> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            LiveData<List<CollectFavorite>> listLiveData = database.getCollectFavoriteDao().loadFavoriteAll();
            callback.onLiveData(listLiveData);
        }, TelephoneThreadDispatcher.DispatcherType.WORK);
    }


    public void insertFavorite(CollectFavorite favorite) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getCollectFavoriteDao().insertFavorite(favorite), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void updateFavorite(CollectFavorite collectFavorite) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getCollectFavoriteDao().updateFavorite(collectFavorite), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void deleteFavorite(CollectFavorite collectFavorite) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getCollectFavoriteDao().deleteFavorite(collectFavorite), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void loadContactLiveAll(final LoadLiveCallback<List<DialerContact>> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            if (callback != null) {
                LiveData<List<DialerContact>> dialerContactLiveData = database.getDialerContactDao().loadContactLiveAll();
                callback.onLiveData(dialerContactLiveData);
            }
        }, TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void insertContact(DialerContact contact) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getDialerContactDao().insertContact(contact), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void updateContact(DialerContact contact) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            DialerContact contactByName = database.getDialerContactDao().loadContactByName(contact.getName());
            if (Objects.isNull(contactByName)) {
                database.getDialerContactDao().insertContact(contact);
            } else {
                contact.setId(contactByName.getId());
                database.getDialerContactDao().insertContact(contact);
            }

            CollectFavorite favorite = loadFavoriteById(contact.getId());

            if (Objects.isNull(favorite) && contact.isFavorite()) {
                favorite = new CollectFavorite();
                favorite.setId(contact.getId());
                favorite.setName(contact.getName());
                favorite.setTel(contact.getTel());
                favorite.setVideo(contact.getVideo());
                favorite.setType(contact.getType());
                database.getCollectFavoriteDao().insertFavorite(favorite);
            } else if (!Objects.isNull(favorite) && !contact.isFavorite()) {
                database.getCollectFavoriteDao().deleteFavorite(favorite);
            } else if (!Objects.isNull(favorite) && contact.isFavorite()) {
                favorite.setId(contact.getId());
                favorite.setName(contact.getName());
                favorite.setTel(contact.getTel());
                favorite.setVideo(contact.getVideo());
                favorite.setType(contact.getType());
                database.getCollectFavoriteDao().updateFavorite(favorite);
            }

        }, TelephoneThreadDispatcher.DispatcherType.WORK);

    }

    private CollectFavorite loadFavoriteById(long id) {
        return database.getCollectFavoriteDao().loadFavoriteById(id);
    }


    /**
     * 删除联系人
     *
     * @param contact
     */
    public void deleteContact(DialerContact contact) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            database.getDialerContactDao().deleteContact(contact);
            CollectFavorite favorite = loadFavoriteById(contact.getId());
            if (!Objects.isNull(favorite)) {
                deleteFavorite(favorite);
            }
        }, TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void getAllCallLog(final LoadDataCallback<RecentModel> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            List<RecentCallLog> allCallLog = database.getRecentCallLogDao().getAllCallLog();
            //排序 分类
            if (allCallLog.isEmpty()) {
                callback.onDataNotAvailable();
            } else {
                callback.onTasksLoaded(null);
            }
        }, TelephoneThreadDispatcher.DispatcherType.WORK);
    }


    public void getAllCallLogByName(String name, final LoadDataCallback<RecentCallLog> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            List<RecentCallLog> allCallLogByName = database.getRecentCallLogDao().getAllCallLogByName(name);
            if (allCallLogByName.isEmpty()) {
                callback.onDataNotAvailable();
            } else {
                callback.onTasksLoaded(allCallLogByName);
            }
        }, TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void insertRecent(RecentCallLog callLog) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getRecentCallLogDao().insertRecent(callLog), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void updateRecent(RecentCallLog callLog) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getRecentCallLogDao().updateRecent(callLog), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void deleteRecent(RecentCallLog callLog) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getRecentCallLogDao().deleteRecent(callLog), TelephoneThreadDispatcher.DispatcherType.WORK);

    }

    public void loadContactLiveById(long contactId, LoadLiveCallback<DialerContact> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            LiveData<DialerContact> dialerContactLiveData = database.getDialerContactDao().loadContactById(contactId);
            callback.onLiveData(dialerContactLiveData);
        }, TelephoneThreadDispatcher.DispatcherType.WORK);
    }
}
