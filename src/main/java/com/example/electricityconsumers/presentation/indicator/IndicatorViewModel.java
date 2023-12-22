package com.example.electricityconsumers.presentation.indicator;

import javafx.beans.property.SimpleDoubleProperty;

public class IndicatorViewModel {

    public final SimpleDoubleProperty maxValue = new SimpleDoubleProperty(100.0);
    public final SimpleDoubleProperty minValue = new SimpleDoubleProperty(0.0);
    public final SimpleDoubleProperty minWarning = new SimpleDoubleProperty();
    public final SimpleDoubleProperty maxWarning = new SimpleDoubleProperty();
    public final SimpleDoubleProperty minBreakdown = new SimpleDoubleProperty();
    public final SimpleDoubleProperty maxBreakdown = new SimpleDoubleProperty();
}
