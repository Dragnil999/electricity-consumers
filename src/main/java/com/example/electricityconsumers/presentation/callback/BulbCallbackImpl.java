package com.example.electricityconsumers.presentation.callback;

import com.example.electricityconsumers.domain.callback.ConsumerCallback;

public class BulbCallbackImpl implements ConsumerCallback<Boolean> {

    @Override
    public void onActivate(Boolean value) {
        System.out.println("Лампочка включена");
    }

    @Override
    public void onDeactivate(Boolean value) {
        System.out.println("Лампочка выключена");
    }
}
