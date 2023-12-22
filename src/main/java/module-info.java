module com.example.electricityconsumers {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.electricityconsumers to javafx.fxml;
    exports com.example.electricityconsumers;
    opens com.example.electricityconsumers.ui to javafx.fxml;
    exports com.example.electricityconsumers.ui;
}