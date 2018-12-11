package com.hhtc.dialer.main.contacts;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.hhtc.dialer.main.DialerFragment;

public class ContactFactory extends ViewModelProvider.NewInstanceFactory {

    private DialerFragment fragment;

    public ContactFactory(DialerFragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ContactsViewModel(fragment);
    }
}
