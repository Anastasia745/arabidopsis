module com.example.arabidopsis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires json.simple;
    requires jfreechart;

    opens arabidopsis to javafx.fxml;
    exports arabidopsis;

    opens arabidopsis.controllers to javafx.fxml;
    exports arabidopsis.controllers;
//    exports arabidopsis.views;
//    opens arabidopsis.views to javafx.fxml;
}