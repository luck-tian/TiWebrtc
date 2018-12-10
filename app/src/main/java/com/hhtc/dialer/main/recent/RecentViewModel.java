package com.hhtc.dialer.main.recent;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadDataCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.ArrayList;
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
        testAddData();
    }

    private void testAddData() {
        TelephoneThreadDispatcher.getInstance().executeDelay(() -> {
            List<RecentModel> datas = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                RecentModel recentModel = new RecentModel();
                recentModel.setTime("Earlier Today");
                RecentCallLog callLog = new RecentCallLog("name",
                        "telOrVideo",
                        "tel",
                        "photo",
                        System.currentTimeMillis(),
                        RecentCallLog.INCOMING_TYPE);
                recentModel.setCallLog(callLog);
                recentModel.setType(RecentAdapter.NORMAL_DATA);
                datas.add(recentModel);
            }
            mCallLog.postValue(datas);
        }, 4000);
    }


    public MutableLiveData<List<RecentModel>> getCallLog() {
        return mCallLog;
    }
}
