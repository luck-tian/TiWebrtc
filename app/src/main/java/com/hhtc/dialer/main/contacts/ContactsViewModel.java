package com.hhtc.dialer.main.contacts;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.main.DialerFragment;
import com.hhtc.dialer.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ContactsViewModel extends ViewModel {


    private static final String TAG = "ContactsViewModel";

    private MutableLiveData<List<ContactModel>> contacts = new MutableLiveData<>();


    private Repository repository;

    private DialerFragment fragment;

    public ContactsViewModel(DialerFragment fragment) {
        this.fragment = fragment;
        repository = Injection.provideTasksRepository(Objects.requireNonNull(fragment.getContext()).getApplicationContext());
    }


    public void loadContact() {
        repository.loadContactLiveAll(this::liveData);
    }


    private void liveData(LiveData<List<DialerContact>> data) {
        data.observe(fragment, this::changedData);
    }


    private void changedData(List<DialerContact> dialerContacts) {
        LogUtil.d(TAG, "changedData: dialerContacts:" + dialerContacts.size() + "--" + dialerContacts.toString());
        List<ContactModel> modles = new ArrayList<>(dialerContacts.size());
        for (int i = 0; i < dialerContacts.size(); i++) {
            DialerContact contact = dialerContacts.get(i);
            ContactModel contactModle = new ContactModel();

            contactModle.setDialerContact(contact);

            char firstChar = contact.getName().charAt(0);
            if (Pinyin.isChinese(firstChar)) {
                String classify = Pinyin.toPinyin(firstChar);
                contactModle.setClassify(classify);
            } else if (Character.isLetter(firstChar)) {
                contactModle.setClassify(String.valueOf(Character.toUpperCase(firstChar)));
            } else {
                contactModle.setClassify("#");
            }
            modles.add(contactModle);
        }
        sortData(modles);
    }


    private void sortData(List<ContactModel> modles) {

        Collections.sort(modles, (first, second) -> {
            if (!TextUtils.equals(first.getClassify(), second.getClassify())) {
                return first.getClassify().compareTo(second.getClassify());
            } else {
                return first.getDialerContact().getName().compareTo(second.getDialerContact().getName());
            }
        });
        LogUtil.d(TAG, "sortData: modles:" + modles.size() + "--" + modles.toString());
        contacts.postValue(modles);
    }

    public MutableLiveData<List<ContactModel>> getContacts() {
        return contacts;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        fragment = null;
    }
}
