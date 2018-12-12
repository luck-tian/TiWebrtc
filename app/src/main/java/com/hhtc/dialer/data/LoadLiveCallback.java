package com.hhtc.dialer.data;


import android.arch.lifecycle.LiveData;

public interface LoadLiveCallback<T> {

    void onLiveData(LiveData<T> data);

}