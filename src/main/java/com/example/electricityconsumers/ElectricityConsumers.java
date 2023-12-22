package com.example.electricityconsumers;

import com.example.electricityconsumers.ui.ConsumersView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ElectricityConsumers extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ElectricityConsumers.class.getResource("electricity-consumers.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 480);
        stage.setTitle("Electricity consumers");
        stage.setScene(scene);
        ConsumersView consumersView = fxmlLoader.getController();
        consumersView.handleExit(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}