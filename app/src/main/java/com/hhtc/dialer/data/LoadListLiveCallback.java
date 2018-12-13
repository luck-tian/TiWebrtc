package com.hhtc.dialer.data;


import android.arch.lifecycle.LiveData;

import java.util.List;

public interface LoadListLiveCallback<T> {

    void onLiveData(LiveData<List<T>> data);

}