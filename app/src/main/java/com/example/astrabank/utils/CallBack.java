package com.example.astrabank.utils;

public interface CallBack<T> {
    void onResult(T result);
    void onFailure(Exception e);
}
