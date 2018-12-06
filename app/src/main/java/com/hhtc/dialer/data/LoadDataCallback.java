package com.hhtc.dialer.data;


import java.util.List;

public interface LoadDataCallback<T> {

    void onTasksLoaded(List<T> data);

    void onDataNotAvailable();
}