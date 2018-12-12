package com.hhtc.dialer.show;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.DialerContact;

public class ShowContactInViewModel extends AndroidViewModel {


    private MutableLiveData<Void> notify = new MutableLiveData<>();

    private LiveData<DialerContact> contactLiveData;

    private Repository repository;

    public ShowContactInViewModel(@NonNull Application application) {
        super(application);
        repository = Injection.provideTasksRepository(application);
    }

    public void loadContact(long contactId) {
        repository.loadContactLiveById(contactId, data -> {
            contactLiveData = data;
            notify.postValue(null);
        });
    }

    public MutableLiveData<Void> getNotify() {
        return notify;
    }

    public LiveData<DialerContact> getContactLiveData() {
        return contactLiveData;
    }
}
