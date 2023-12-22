package com.example.electricityconsumers.domain.entity;


import com.example.electricityconsumers.domain.callback.ConsumerCallback;

public class Bulb extends Consumer {

    public Bulb(boolean active, double power, ConsumerCallback<Boolean> callback) {
        super(active, power, callback);
    }

    @Override
    public void activate() {
        active = true;
        callback.onActivate(true);
    }

    @Override
    public void deactivate() {
        active = false;
        callback.onDeactivate(false);
    }
}
