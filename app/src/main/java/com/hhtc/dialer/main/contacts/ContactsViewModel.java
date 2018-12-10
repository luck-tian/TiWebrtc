package com.hhtc.dialer.main.contacts;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadDataCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.data.bean.RecentCallLog;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewModel extends AndroidViewModel {


    private MutableLiveData<List<ContactModle>> contacts = new MutableLiveData<>();

    private Repository repository;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        repository = Injection.provideTasksRepository(application.getApplicationContext());
    }


    public void loadContact() {
        TelephoneThreadDispatcher.getInstance().execute(() -> repository.getAllContact(new LoadDataCallback<ContactModle>() {
            @Override
            public void onTasksLoaded(List<ContactModle> data) {
                contacts.postValue(data);
            }

            @Override
            public void onDataNotAvailable() {
                contacts.postValue(null);
            }
        }), TelephoneThreadDispatcher.DispatcherType.WORK);

        addTestData();
    }

    private void addTestData() {
        TelephoneThreadDispatcher.getInstance().executeDelay(() -> {
            List<ContactModle> data = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                DialerContact contact = new DialerContact("name"+i,
                        "tel",
                        "massage",
                        "email",
                        "video",
                        "photo",
                        RecentCallLog.BLOCK_CHAIN);
                ContactModle contactModle = new ContactModle();
                contactModle.setDialerContact(contact);
                if (i % 2 == 1) {
                    contact.setType(RecentCallLog.BLOCK_CHAIN);
                } else {
                    contact.setType(RecentCallLog.TRADITIONAL);
                }
                char ch = (char) ('A' + i);
                contactModle.setClassify(String.valueOf(ch));

                data.add(contactModle);
            }
            contacts.postValue(data);
        }, 4000);
    }

    public MutableLiveData<List<ContactModle>> getContacts() {
        return contacts;
    }
}
