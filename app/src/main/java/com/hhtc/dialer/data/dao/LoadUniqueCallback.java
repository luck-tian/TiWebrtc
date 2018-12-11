package com.hhtc.dialer.data.dao;


public interface LoadUniqueCallback<T> {

    void onTasksLoaded(T data);

    void onDataNotAvailable();
}