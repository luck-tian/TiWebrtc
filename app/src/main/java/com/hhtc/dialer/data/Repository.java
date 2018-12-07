package com.hhtc.dialer.data;

import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.List;

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


    public void getAllFavorite(final LoadDataCallback<CollectFavorite> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            List<CollectFavorite> allFavorite = database.getCollectFavoriteDao().getAllFavorite();
            if (allFavorite.isEmpty()) {
                callback.onDataNotAvailable();
            } else {
                callback.onTasksLoaded(allFavorite);
            }
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


    public void getAllContact(final LoadDataCallback<DialerContact> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            List<DialerContact> allContact = database.getDialerContactDao().getAllContact();
            if (allContact.isEmpty()) {
                callback.onDataNotAvailable();
            } else {
                callback.onTasksLoaded(allContact);
            }
        }, TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void insertContact(DialerContact contact) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getDialerContactDao().insertContact(contact), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void updateContact(DialerContact contact) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getDialerContactDao().updateContact(contact), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void deleteContact(DialerContact contact) {
        TelephoneThreadDispatcher.getInstance().execute(() -> database.getDialerContactDao().deleteContact(contact), TelephoneThreadDispatcher.DispatcherType.WORK);

    }


    public void getAllCallLog(final LoadDataCallback<RecentCallLog> callback) {
        TelephoneThreadDispatcher.getInstance().execute(() -> {
            List<RecentCallLog> allCallLog = database.getRecentCallLogDao().getAllCallLog();
            if (allCallLog.isEmpty()) {
                callback.onDataNotAvailable();
            } else {
                callback.onTasksLoaded(allCallLog);
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
}
