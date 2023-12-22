package com.example.electricityconsumers.presentation.callback;

import com.example.electricityconsumers.domain.callback.ConsumerCallback;
import com.example.electricityconsumers.ui.indicator.Indicator;
import javafx.concurrent.Task;

public class KettleCallbackImpl implements ConsumerCallback<Integer> {

        private final Indicator kettleIndicator;

        public KettleCallbackImpl(Indicator kettleIndicator) {
            this.kettleIndicator = kettleIndicator;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void onActivate(Integer value) {
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    kettleIndicator.setValueToIndicatorByIndex(0, value.toString());
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
                    kettleIndicator.setValueToIndicatorByIndex(0, value.toString());
                    return null;
                }
            };
            new Thread(task).start();
        }
}
