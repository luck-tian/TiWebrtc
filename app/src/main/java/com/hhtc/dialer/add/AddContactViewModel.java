package com.hhtc.dialer.add;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.LoadUniqueCallback;

public class AddContactViewModel extends ViewModel {

    private Repository repository;

    private MutableLiveData<DialerContact> contact = new MutableLiveData<>();

    public AddContactViewModel(Context context) {
        repository = Injection.provideTasksRepository(context);
    }

    public void loadContact(long id) {
        repository.getContactById(id, new LoadUniqueCallback<DialerContact>() {
            @Override
            public void onTasksLoaded(DialerContact data) {
                contact.postValue(data);
            }

            @Override
            public void onDataNotAvailable() {
                contact.postValue(null);
            }
        });
    }

    public MutableLiveData<DialerContact> getContact() {
        return contact;
    }

    public void save(DialerContact dialerContact) {
        repository.updateContact(dialerContact);
    }
}
