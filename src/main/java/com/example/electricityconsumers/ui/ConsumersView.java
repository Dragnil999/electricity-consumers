package com.example.electricityconsumers.ui;

import com.example.electricityconsumers.domain.entity.Bulb;
import com.example.electricityconsumers.domain.entity.Computer;
import com.example.electricityconsumers.domain.entity.Consumer;
import com.example.electricityconsumers.domain.entity.Kettle;
import com.example.electricityconsumers.presentation.callback.BulbCallbackImpl;
import com.example.electricityconsumers.presentation.callback.ComputerCallbackImpl;
import com.example.electricityconsumers.presentation.callback.KettleCallbackImpl;
import com.example.electricityconsumers.ui.indicator.Indicator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ConsumersView implements Initializable {

    private static final double KETTLE_POWER = 15.0;
    private static final double BULB_POWER = 5.0;
    private static final double COMPUTER_POWER = 30.0;
    private static final double MAX_POWER = 60.0;

    private final Indicator.IndicatorCallback kettleIndicatorCallback = new Indicator.IndicatorCallback() {
        @Override
        public void onMaxBreakdown(int index) {
            System.out.println("Чайник вскипел");
            deactivateKettle();
        }

        @Override
        public void onMinBreakdown(int index) { }

        @Override
        public void onMaxWarning(int index) { }

        @Override
        public void onMinWarning(int index) {
            System.out.println("Чайник остыл");
        }
    };

    private final Indicator.IndicatorCallback batteryIndicatorCallback = new Indicator.IndicatorCallback() {
        @Override
        public void onMaxBreakdown(int index) {
            System.out.println("Батарея заряжена");
        }

        @Override
        public void onMinBreakdown(int index) {
            System.out.println("Батарея села");
            deactivateComputer();
        }

        @Override
        public void onMaxWarning(int index) {}

        @Override
        public void onMinWarning(int index) {}
    };

    private final Indicator.IndicatorCallback powerIndicatorCallback = new Indicator.IndicatorCallback() {
        @Override
        public void onMaxBreakdown(int index) {
            System.out.println("Перегрузка");
            powerValue = 0.0;
            for (Consumer consumer : consumers) {
                consumer.deactivate();
            }
            powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        }

        @Override
        public void onMinBreakdown(int index) { }

        @Override
        public void onMaxWarning(int index) { }

        @Override
        public void onMinWarning(int index) { }
    };

    @FXML
    private HBox root;

    @FXML
    private Label bulbLabel;

    @FXML
    private Label pcLabel;

    private final List<Consumer> consumers = new ArrayList<>();

    private Double powerValue = 0.0;

    private final Indicator kettleIndicator = new Indicator(kettleIndicatorCallback);

    private final Indicator batteryIndicator = new Indicator(batteryIndicatorCallback);

    private final Indicator powerIndicator = new Indicator(powerIndicatorCallback);

    private final Kettle kettle = new Kettle(false, KETTLE_POWER, new KettleCallbackImpl(kettleIndicator));

    private final Bulb bulb = new Bulb(false, BULB_POWER, new BulbCallbackImpl());

    private final Computer computer = new Computer(false, COMPUTER_POWER, new ComputerCallbackImpl(batteryIndicator));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        consumers.add(kettle);
        consumers.add(bulb);
        consumers.add(computer);
        setPowerIndicatorParameters();
        setKettleIndicatorParameters();
        setBatteryIndicatorParameters();
        root.getChildren().add(0, powerIndicator);
        root.getChildren().add(1, kettleIndicator);
        root.getChildren().add(2, batteryIndicator);
    }

    public void activateKettle() {
        powerValue += kettle.getPower();
        powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        kettle.activate();
    }

    private void deactivateKettle() {
        powerValue -= kettle.getPower();
        powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        kettle.deactivate();
    }

    public void activateBulb() {
        bulbLabel.setText("Вкл.");
        powerValue += bulb.getPower();
        powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        bulb.activate();
    }

    public void deactivateBulb() {
        bulbLabel.setText("Выкл.");
        powerValue -= bulb.getPower();
        powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        bulb.deactivate();
    }

    public void activateComputer() {
        pcLabel.setText("Вкл.");
        powerValue += computer.getPower();
        powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        computer.activate();
    }

    public void deactivateComputer() {
        pcLabel.setText("Вкл.");
        powerValue -= computer.getPower();
        powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        computer.deactivate();
    }

    public void deactivateComputerByUser() {
        pcLabel.setText("Выкл.");
        if (powerValue > computer.getPower()) {
            powerValue -= computer.getPower();
        } else {
            powerValue = 0.0;
        }
        powerIndicator.setValueToIndicatorByIndex(0, String.valueOf(powerValue));
        computer.deactivateByUser();
    }

    private void setPowerIndicatorParameters() {
        powerIndicator.setMaxBreakdown(MAX_POWER);
        powerIndicator.setMinBreakdown(0);
        powerIndicator.setMaxWarning(18);
        powerIndicator.setMinWarning(0);
        powerIndicator.addNewIndicator(String.valueOf(0));
    }

    private void setKettleIndicatorParameters() {
        kettleIndicator.setMaxBreakdown(100);
        kettleIndicator.setMinBreakdown(0);
        kettleIndicator.setMinWarning(5);
        kettleIndicator.setMaxWarning(88);
        kettleIndicator.addNewIndicator(String.valueOf(0));
    }

    private void setBatteryIndicatorParameters() {
        batteryIndicator.setMaxBreakdown(100);
        batteryIndicator.setMinBreakdown(1);
        batteryIndicator.setMaxWarning(0);
        batteryIndicator.setMinWarning(0);
        batteryIndicator.addNewIndicator(String.valueOf(80));
    }

    public void handleExit(Stage stage) {
        stage.setOnCloseRequest(windowEvent -> {
            kettle.killThread();
            computer.killThread();
        });
    }
}
