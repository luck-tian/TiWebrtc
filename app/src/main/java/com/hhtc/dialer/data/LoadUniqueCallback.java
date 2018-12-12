package com.hhtc.dialer.data;


public interface LoadUniqueCallback<T> {

    void onTasksLoaded(T data);

    void onDataNotAvailable();
}