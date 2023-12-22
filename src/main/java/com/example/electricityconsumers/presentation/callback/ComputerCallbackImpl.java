package com.example.electricityconsumers.presentation.callback;

import com.example.electricityconsumers.domain.callback.ConsumerCallback;
import com.example.electricityconsumers.ui.indicator.Indicator;
import javafx.concurrent.Task;

public class ComputerCallbackImpl implements ConsumerCallback<Integer> {

    private final Indicator batteryIndicator;

    public ComputerCallbackImpl(Indicator batteryIndicator) {
        this.batteryIndicator = batteryIndicator;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onActivate(Integer value) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                batteryIndicator.setValueToIndicatorByIndex(0, value.toString());
                return null;
            }
        };
        new Thread(task).start();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onDeactivate(Integer value) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                batteryIndicator.setValueToIndicatorByIndex(0, value.toString());
                return null;
            }
        };
        new Thread(task).start();
    }
}
