package com.hhtc.dialer.main.contacts;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.hhtc.dialer.data.Injection;
import com.hhtc.dialer.data.LoadDataCallback;
import com.hhtc.dialer.data.LoadLiveCallback;
import com.hhtc.dialer.data.Repository;
import com.hhtc.dialer.data.bean.DialerContact;
import com.hhtc.dialer.main.DialerFragment;
import com.hhtc.dialer.thread.TelephoneThreadDispatcher;
import com.hhtc.dialer.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ContactsViewModel extends ViewModel {


    private static final String TAG = "ContactsViewModel";

    private Repository repository;

    private MutableLiveData<Void> notify = new MutableLiveData<>();

    private LiveData<List<DialerContact>> contacts;

    private MutableLiveData<List<ContactModel>> models = new MutableLiveData<>();

    public ContactsViewModel(DialerFragment fragment) {
        repository = Injection.provideTasksRepository(Objects.requireNonNull(fragment.getContext()).getApplicationContext());
    }


    public void loadContact() {
        repository.loadContactLiveAll(this::onLiveData);
    }

    public void onLiveData(LiveData<List<DialerContact>> data) {
        this.contacts = data;
        notify.postValue(null);
    }


    public void changedData(List<DialerContact> dialerContacts) {
        TelephoneThreadDispatcher.getInstance().execute(() -> postModel(dialerContacts),TelephoneThreadDispatcher.DispatcherType.WORK);

    }

    private void postModel(List<DialerContact> dialerContacts) {
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


    public void sortData(List<ContactModel> modles) {

        //第一轮排序
        Collections.sort(modles, (first, second) -> {
            if (!TextUtils.equals(first.getClassify(), second.getClassify())) {
                return first.getClassify().compareTo(second.getClassify());
            } else {
                return first.getDialerContact().getName().compareTo(second.getDialerContact().getName());
            }
        });

        //第二轮排序 # 和非 # 排序
        Collections.sort(modles, (first, second) -> {
            if (TextUtils.equals(first.getClassify(), second.getClassify()) && TextUtils.equals(first.getClassify(), "#")) {
                return first.getDialerContact().getName().compareTo(second.getDialerContact().getName());
            } else if (!TextUtils.equals(first.getClassify(), second.getClassify()) &&
                    TextUtils.equals(first.getClassify(), "#")) {
                return 1;
            } else if (!TextUtils.equals(first.getClassify(), second.getClassify()) &&
                    TextUtils.equals(second.getClassify(), "#")) {
                return -1;
            } else {
                return 0;
            }
        });

        this.models.postValue(modles);
    }


    public MutableLiveData<Void> getNotify() {
        return notify;
    }

    public LiveData<List<DialerContact>> getContacts() {
        return contacts;
    }

    public MutableLiveData<List<ContactModel>> getModels() {
        return models;
    }
}
