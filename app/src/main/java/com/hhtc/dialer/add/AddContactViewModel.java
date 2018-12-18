package com.hhtc.dialer.add;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadLiveCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.LoadUniqueCallback;
import com.hhtc.dialer.data.bean.RecentCallLog;

public class AddContactViewModel extends ViewModel {

    private Repository repository;

    private MutableLiveData<Void> notify = new MutableLiveData<>();

    private LiveData<DialerContact> contact;

    public AddContactViewModel(Context context) {
        repository = Injection.provideTasksRepository(context);
    }

    public void loadContact(long id) {
        repository.loadContactLiveById(id, data -> {
            AddContactViewModel.this.contact = data;
            notify.postValue(null);
        });
    }

    public MutableLiveData<Void> getNotify() {
        return notify;
    }

    public LiveData<DialerContact> getContact() {
        return contact;
    }

    public void save(DialerContact dialerContact) {
        repository.updateContact(dialerContact);
    }
}
