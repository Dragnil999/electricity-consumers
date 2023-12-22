package com.example.electricityconsumers.ui.indicator;

import com.example.electricityconsumers.presentation.indicator.IndicatorViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Indicator extends HBox {

    private static final double PREF_HEIGHT = 250.0;
    private static final double PREF_WIDTH = 50.0;
    private static final String MAX_BREAKDOWN_LABEL = "Макс. авария";
    private static final String MAX_WARNING_LABEL = "Макс. пред.";
    private static final String MIN_BREAKDOWN_LABEL = "Мин. авария";
    private static final String MIN_WARNING_LABEL = "Мин. пред.";

    private final IndicatorViewModel viewModel = new IndicatorViewModel();
    private final VBox infoAboutBorders = new VBox();

    private final List<Label> listOfLabels = List.of(
            new Label(MAX_BREAKDOWN_LABEL),
            new Label(MAX_WARNING_LABEL),
            new Label(MIN_WARNING_LABEL),
            new Label(MIN_BREAKDOWN_LABEL)
    );

    private final List<TextField> inputs = new ArrayList<>();
    private final IndicatorCallback callback;

    public interface IndicatorCallback {
        void onMaxBreakdown(int index);
        void onMinBreakdown(int index);
        void onMaxWarning(int index);
        void onMinWarning(int index);
    }

    public Indicator(IndicatorCallback callback) {
        this.callback = callback;
        this.setPrefSize(USE_COMPUTED_SIZE, 480);
        this.setAlignment(Pos.CENTER);

        setLabelsToVBox();
        HBox.setMargin(infoAboutBorders, new Insets(40, 20, 0, 0));
        this.getChildren().add(infoAboutBorders);
        setListeners();
    }

    private void setListeners() {
        viewModel.maxWarning.addListener(((observableValue, number, t1) ->
                listOfLabels.get(1).setText(MAX_WARNING_LABEL + " " + t1)
        ));

        viewModel.minWarning.addListener(((observableValue, number, t1) ->
                listOfLabels.get(2).setText(MIN_WARNING_LABEL + " " + t1))
        );

        viewModel.maxBreakdown.addListener(((observableValue, number, t1) ->
                listOfLabels.get(0).setText(MAX_BREAKDOWN_LABEL + " " + t1)
        ));

        viewModel.minBreakdown.addListener(((observableValue, number, t1) ->
                listOfLabels.get(3).setText(MIN_BREAKDOWN_LABEL + " " + t1)
        ));
    }

    public void addNewIndicator(String value) {
        VBox indicatorContainer = new VBox();
        indicatorContainer.setAlignment(Pos.BOTTOM_CENTER);

        Rectangle indicator = new Rectangle(PREF_WIDTH, 0, Color.GREEN);
        indicatorContainer.getChildren().add(indicator);
        VBox.setMargin(indicator, new Insets(0, 0, 80, 10));

        TextField inputField = new TextField();
        inputField.setPrefWidth(10.0);
        VBox.setMargin(inputField, new Insets(0, 0, 20, 0));

        inputs.add(inputField);

        inputField.textProperty().addListener((observableValue, s, t1) -> {
            double parameter = 0.0;
            try {
                parameter = Double.parseDouble(t1);
            } catch (Exception exception) {
                indicator.setHeight(parameter);
                return;
            }
            if (parameter - viewModel.minValue.get() <= viewModel.maxValue.get() - viewModel.minValue.get()) {
                indicator.setHeight(PREF_HEIGHT * (parameter - viewModel.minValue.get()) / (viewModel.maxValue.get() - viewModel.minValue.get()));
            } else {
                indicator.setHeight(PREF_HEIGHT);
            }

            handleChanges(parameter, inputs.indexOf(inputField));
            chooseColor(parameter, indicator);
        });

        inputField.setText(value);

        indicatorContainer.getChildren().add(0, inputField);

        HBox.setMargin(indicatorContainer, new Insets(0, 0, 0, 10));
        this.getChildren().add(indicatorContainer);
    }

    public void setValueToIndicatorByIndex(int index, String value) {
        inputs.get(index).setText(value);
    }

    public String getValueByIndex(int index) { return inputs.get(index).getText(); }

    private void setLabelsToVBox() {
        infoAboutBorders.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

        listOfLabels.get(0).setTextFill(Color.RED);
        listOfLabels.get(1).setTextFill(Color.GREEN);
        listOfLabels.get(2).setTextFill(Color.GREEN);
        listOfLabels.get(3).setTextFill(Color.RED);

        listOfLabels.get(0).setPadding(new Insets(40, 0, 0, 0));
        for (Label label : listOfLabels) {
            VBox.setMargin(label, new Insets(0, 0, 40, 0));
        }

        infoAboutBorders.getChildren().addAll(listOfLabels);
    }

    private void handleChanges(double value, int index) {
        if (value >= viewModel.maxBreakdown.get()) {
            callback.onMaxBreakdown(index);
        } else if (value <= viewModel.minBreakdown.get()) {
            callback.onMinBreakdown(index);
        } else if (value >= viewModel.maxWarning.get()) {
            callback.onMaxWarning(index);
        } else if (value <= viewModel.minWarning.get()) {
            callback.onMinWarning(index);
        }
    }

    private void chooseColor(double value, Rectangle indicator) {
        if (value >= viewModel.maxBreakdown.get()
                || value <= viewModel.minBreakdown.get()) {
            indicator.setFill(Color.RED);
        } else if (value >= viewModel.maxWarning.get()
                || value <= viewModel.minWarning.get()) {
            indicator.setFill(Color.YELLOW);
        } else if (value <= viewModel.minValue.get()) {
            indicator.setFill(Color.GHOSTWHITE);
        } else {
            indicator.setFill(Color.GREEN);
        }
    }

    public void setMaxValue(double value) {
        viewModel.maxValue.set(value);
    }

    public void setMinValue(double value) {
        viewModel.minValue.set(value);
    }

    public void setMaxWarning(double value) {
        viewModel.maxWarning.set(value);
    }

    public void setMinWarning(double value) {
        viewModel.minWarning.set(value);
    }

    public void setMaxBreakdown(double value) {
        viewModel.maxBreakdown.set(value);
    }

    public void setMinBreakdown(double value) {
        viewModel.minBreakdown.set(value);
    }
}
