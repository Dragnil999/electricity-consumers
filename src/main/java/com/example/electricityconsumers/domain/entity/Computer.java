package com.example.electricityconsumers.domain.entity;

import com.example.electricityconsumers.domain.callback.ConsumerCallback;

@SuppressWarnings("ALL")
public class Computer extends Consumer {

    private final Object lockObject = new Object();
    private int batteryLevel = 80;
    private boolean powerOn = true;
    private volatile boolean running = true;
    private boolean shutdown = true;
    private boolean shutdownByUser = true;
    private final Runnable computerRunnable;
    
    
    
    public Computer(boolean active, double power, ConsumerCallback<Integer> callback) {
        super(active, power, callback);
        new Thread(computerRunnable).start();
    }

    {
        computerRunnable = () -> {
            while (running) {
                try {
                    waitActivation();
                    if (batteryLevel == 0  || !active) continue;
                    shutdown = true;
                    shutdownByUser = true;
                    if (powerOn) {
                        while (powerOn) {
                            powerOn = shutdown;
                            if (batteryLevel <= 99) {
                                Thread.sleep(40);
                                batteryLevel += 1;
                                callback.onActivate(batteryLevel);
                            }
                        }
                    } else {
                        while (active) {
                            active = shutdownByUser;
                            if (batteryLevel >= 1) {
                                Thread.sleep(50);
                                callback.onDeactivate(batteryLevel);
                                batteryLevel -= 1;
                            }
                            if (batteryLevel == 0) {
                                active = false;
                            }
                        }
                    }
                } catch (Exception ignored) { }
            }
        };
    }

    @Override
    public void activate() {
        processUsing();
        active = true;
    }

    @Override
    public void deactivate() {
        shutdown = false;
    }

    public void deactivateByUser() {
        shutdownByUser = false;
    }

    public void killThread() {
        running = false;
    }

    private void processUsing() {
        synchronized (lockObject) {
            lockObject.notify();
        }
    }

    private void waitActivation() throws InterruptedException {
        synchronized (lockObject) {
            if (batteryLevel <= 0 || !active) {
                lockObject.wait();
            }
        }
    }
}
