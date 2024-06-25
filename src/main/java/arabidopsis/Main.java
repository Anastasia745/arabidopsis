package arabidopsis;
//
import arabidopsis.controllers.ControllerMain;
import arabidopsis.db.GlobalGenes;
import arabidopsis.db.GlobalStates;
import arabidopsis.db.GlobalSystem;
import arabidopsis.models.Gene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainUi.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 800);
        stage.setTitle("ARABIDOPSIS");
        stage.setScene(scene);
        //stage.setMaximized(true);
        stage.show();

        ControllerMain controllerMain = fxmlLoader.getController();
        controllerMain.startSimulation();
    }

    public static void main(String[] args) {
        launch();
    }
}


//
//import javafx.application.Application;
//import javafx.geometry.Side;
//import javafx.scene.Scene;
//import javafx.scene.chart.AreaChart;
//import javafx.scene.chart.BarChart;
//import javafx.scene.chart.BubbleChart;
//import javafx.scene.chart.CategoryAxis;
//import javafx.scene.chart.LineChart;
//import javafx.stage.Stage;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.ScatterChart;
//import javafx.scene.chart.XYChart;
//import javafx.scene.layout.FlowPane;
//
//public class Main extends Application {
//    public void start(Stage stage) {
//
//        CategoryAxis xAxis = new CategoryAxis();
//        NumberAxis yAxis = new NumberAxis();
//
//        xAxis.setLabel("Months");
//        yAxis.setLabel("Rainfall (mm)");
//        AreaChart areaChart = new AreaChart(xAxis, yAxis);
//        XYChart.Series series1 = new XYChart.Series();
//        series1.getData().add(new XYChart.Data("July", 169.9));
//        series1.getData().add(new XYChart.Data("Aug", 178.7));
//        series1.getData().add(new XYChart.Data("Sep", 158.3));
//        series1.getData().add(new XYChart.Data("Oct", 97.2));
//        series1.getData().add(new XYChart.Data("Nov", 22.4));
//        series1.getData().add(new XYChart.Data("Dec", 5.9));
//        series1.setName("Rainfall In Hyderabad");
//
//
//        XYChart.Series series2 = new XYChart.Series();
//        series2.getData().add(new XYChart.Data("July", 189.9));
//        series2.getData().add(new XYChart.Data("Aug", 188.7));
//        series2.getData().add(new XYChart.Data("Sep", 188.3));
//        series2.getData().add(new XYChart.Data("Oct", 37.2));
//        series2.getData().add(new XYChart.Data("Nov", 62.4));
//        series2.getData().add(new XYChart.Data("Dec", 4.9));
//        series2.setName("Rainfall In Hyderabad");
//
//
//        areaChart.getData().addAll(series1);
//        areaChart.getData().addAll(series2);
//        areaChart.setPrefSize(400, 400);
//
//        FlowPane pane = new FlowPane();
//        pane.getChildren().addAll(areaChart);
//
//        Scene scene = new Scene(pane, 600, 430);
//        stage.setTitle("X-Y charts");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String args[]) {
//        launch(args);
//    }
//}


