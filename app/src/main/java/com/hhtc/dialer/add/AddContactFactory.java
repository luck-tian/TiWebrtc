package com.hhtc.dialer.add;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class AddContactFactory extends ViewModelProvider.NewInstanceFactory {

    private Context context;

    public AddContactFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddContactViewModel(context);
    }
}
