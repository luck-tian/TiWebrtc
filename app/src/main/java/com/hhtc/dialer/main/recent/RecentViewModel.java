package com.hhtc.dialer.main.recent;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadDataCallback;
import com.hhtc.dialer.data.LoadListLiveCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.hhtc.dialer.main.recent.RecentAdapter.NORMAL_DATA;

public class RecentViewModel extends AndroidViewModel {

    private MutableLiveData<List<RecentModel>> mCallLog = new MutableLiveData<>();

    private MutableLiveData<Void> notify = new MutableLiveData<>();

    private Repository repository;

    private LiveData<List<RecentCallLog>> recentData;

    public RecentViewModel(@NonNull Application application) {
        super(application);
        repository = Injection.provideTasksRepository(application.getApplicationContext());
    }

    public void loadCallLog() {
        repository.loadCallLogAll(this::onLiveData);
    }

    private void onLiveData(LiveData<List<RecentCallLog>> data) {
        this.recentData = data;
        notify.postValue(null);
    }


    public MutableLiveData<List<RecentModel>> getCallLog() {
        return mCallLog;
    }


    public MutableLiveData<Void> getNotify() {
        return notify;
    }

    public LiveData<List<RecentCallLog>> getRecentData() {
        return recentData;
    }

    public void analysisRecentCallLog(List<RecentCallLog> recentCallLogs) {
        if (Objects.nonNull(recentCallLogs) && !recentCallLogs.isEmpty()) {
            List<RecentModel> recentModels = new ArrayList<>();

            for (int i = 0; i < recentCallLogs.size(); i++) {
                RecentCallLog recentCallLog = recentCallLogs.get(i);
                RecentModel recentModel = new RecentModel();
                recentModel.setType(NORMAL_DATA);
                recentModel.setCallLog(recentCallLog);
                recentModels.add(recentModel);
            }


            mCallLog.postValue(recentModels);
        } else {
            mCallLog.postValue(null);
        }
    }
}
