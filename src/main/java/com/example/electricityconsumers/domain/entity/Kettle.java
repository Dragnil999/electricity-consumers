package com.example.electricityconsumers.domain.entity;

import com.example.electricityconsumers.domain.callback.ConsumerCallback;

@SuppressWarnings("ALL")
public class Kettle extends Consumer {

    private final Object lockObject = new Object();
    private int heatLevel = 0;
    private volatile boolean running = true;
    private boolean shutdown = true;
    private final Runnable kettleRunnable;

    public Kettle(boolean active, double power, ConsumerCallback<Integer> callback) {
        super(active, power, callback);
        new Thread(kettleRunnable).start();
    }

    {
        kettleRunnable = () -> {
            while (running) {
                try {
                    waitActivation();
                    if (heatLevel == 0  && !active) continue;
                    shutdown = true;
                    while (active) {
                        active = shutdown;
                        Thread.sleep(40);
                        heatLevel += 1;
                        callback.onActivate(heatLevel);
                        if (heatLevel >= 99) {
                            shutdown = false;
                        }
                    }
                    if (heatLevel > -1) {
                        Thread.sleep(50);
                        callback.onDeactivate(heatLevel);
                        heatLevel -= 1;
                    }
                } catch (Exception ignored) { }
            }
        };
    }

    @Override
    public void activate() {
        active = true;
        processHeating();
    }

    @Override
    public void deactivate() {
        shutdown = false;
    }

    public void killThread() {
        running = false;
    }

    private void processHeating() {
        synchronized (lockObject) {
            lockObject.notify();
        }
    }

    private void waitActivation() throws InterruptedException {
        synchronized (lockObject) {
            if (heatLevel <= 0 && !active) {
                lockObject.wait();
            }
        }
    }
}
