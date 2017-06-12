package com.learn2crack.androidmvp.base;

public interface BasePresenter {

    void subscribe(boolean isLoading);

    void unsubscribe();
}