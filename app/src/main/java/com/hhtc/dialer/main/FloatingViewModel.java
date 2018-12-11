package com.hhtc.dialer.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class FloatingViewModel extends ViewModel {

    private MutableLiveData<Boolean> floating = new MutableLiveData<>();

    public LiveData<Boolean> getFloating() {
        return floating;
    }

    public void setAction(boolean action) {
        floating.postValue(action);
    }
}
