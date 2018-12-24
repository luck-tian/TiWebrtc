package com.hhtc.dialer.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadLiveCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.UserInfo;

public class FloatingViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> floating = new MutableLiveData<>();

    private LiveData<UserInfo> userInfo;

    private Repository repository;

    private MutableLiveData<Void> notify = new MutableLiveData<>();

    public FloatingViewModel(@NonNull Application application) {
        super(application);
        repository = Injection.provideTasksRepository(application.getApplicationContext());
    }

    public LiveData<Boolean> getFloating() {

        return floating;
    }

    public void setAction(boolean action) {
        floating.postValue(action);
    }

    public void loadUserInfo() {
        repository.loadUserInfo(data -> {
            userInfo = data;
            notify.postValue(null);
        });
    }

    public LiveData<UserInfo> getUserInfo() {
        return userInfo;
    }

    public MutableLiveData<Void> getNotify() {
        return notify;
    }

    public Repository getRepository() {
        return repository;
    }
}
