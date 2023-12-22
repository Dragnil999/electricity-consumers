package com.example.electricityconsumers.domain.entity;

import com.example.electricityconsumers.domain.callback.ConsumerCallback;

@SuppressWarnings("rawtypes")
public abstract class Consumer {

    protected boolean active;
    protected double power;
    protected ConsumerCallback callback;

    public Consumer(boolean active, double power, ConsumerCallback callback) {
        this.active = active;
        this.power = power;
        this.callback = callback;
    }

    public abstract void activate();

    public abstract void deactivate();

    public boolean isActive() {
        return active;
    }

    public double getPower() {
        return power;
    }
}
