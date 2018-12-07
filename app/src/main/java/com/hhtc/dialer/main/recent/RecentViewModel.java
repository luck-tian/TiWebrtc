package com.hhtc.dialer.main.recent;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadDataCallback;
import com.hhtc.dialer.data.Repository;

import java.util.List;

public class RecentViewModel extends AndroidViewModel {

    private MutableLiveData<List<RecentModel>> mCallLog = new MutableLiveData<>();

    private Repository repository;

    public RecentViewModel(@NonNull Application application) {
        super(application);
        repository = Injection.provideTasksRepository(application.getApplicationContext());
    }

    public void loadCallLog() {
        repository.getAllCallLog(new LoadDataCallback<RecentModel>() {
            @Override
            public void onTasksLoaded(List<RecentModel> data) {
                mCallLog.postValue(data);
            }

            @Override
            public void onDataNotAvailable() {
                mCallLog.postValue(null);
            }
        });
    }



    public MutableLiveData<List<RecentModel>> getCallLog() {
        return mCallLog;
    }
}
