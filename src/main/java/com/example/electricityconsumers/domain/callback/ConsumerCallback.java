package com.example.electricityconsumers.domain.callback;

public interface ConsumerCallback<T> {

    void onActivate(T value);

    void onDeactivate(T value);
}
