package com.hhtc.dialer.main.collects;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadDataCallback;
import com.hhtc.dialer.data.LoadListLiveCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.ArrayList;
import java.util.List;

public class CollectViewModel extends AndroidViewModel {

    /**
     * 收藏数据
     */
    private MutableLiveData<Void> notify = new MutableLiveData<>();

    private Repository repository;

    private LiveData<List<CollectFavorite>> mFavorite;

    public CollectViewModel(@NonNull Application application) {
        super(application);
        repository = Injection.provideTasksRepository(application.getApplicationContext());
    }

    public void loadFavorite() {
        repository.loadFavoriteAll(this::loadFavoriteSuccess);
    }


    private void loadFavoriteSuccess(LiveData<List<CollectFavorite>> data){
        this.mFavorite=data;
        notify.postValue(null);
    }


    public LiveData<List<CollectFavorite>> getFavorite() {
        return mFavorite;
    }

    public MutableLiveData<Void> getNotify() {
        return notify;
    }
}
