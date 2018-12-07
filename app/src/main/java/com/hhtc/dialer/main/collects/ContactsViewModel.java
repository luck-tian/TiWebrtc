package com.hhtc.dialer.main.collects;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadDataCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.CollectFavorite;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    /**
     * 收藏数据
     */
    private MutableLiveData<List<CollectFavorite>> mFavorite = new MutableLiveData<>();

    private Repository repository;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        repository = Injection.provideTasksRepository(application.getApplicationContext());
    }

    public void loadFavorite() {
        repository.getAllFavorite(new LoadDataCallback<CollectFavorite>() {
            @Override
            public void onTasksLoaded(List<CollectFavorite> data) {
                mFavorite.postValue(data);
            }

            @Override
            public void onDataNotAvailable() {
                mFavorite.postValue(null);
                testAddData();
            }
        });
    }

    private void testAddData() {
        TelephoneThreadDispatcher.getInstance().executeDelay(new Runnable() {
            @Override
            public void run() {
                List<CollectFavorite> datas = new ArrayList<>();
                datas.add(new CollectFavorite("name", "tel", 1));
                datas.add(new CollectFavorite("name1", "tel", 1));
                datas.add(new CollectFavorite("name2", "tel", 1));
                datas.add(new CollectFavorite("name3", "tel", 1));
                mFavorite.postValue(datas);
            }
        }, 4000);
    }

    /**
     * 获取
     *
     * @return
     */
    public MutableLiveData<List<CollectFavorite>> getFavorite() {
        return mFavorite;
    }


}
