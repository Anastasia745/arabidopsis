package arabidopsis.controllers;

import arabidopsis.db.GlobalGenes;
import arabidopsis.models.Cell;
import arabidopsis.models.Gene;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerGraphic {
    @FXML
    private Pane graphicPane;
    @FXML
    private Button drawGraphicButton;
    @FXML
    private HBox genesListHBox;
    private List<Integer> genesNumbers;

    @FXML
    public void initialize() throws IOException, ParseException {
        Gene[] genes = GlobalGenes.getGenes();

        for (Gene gene: genes) {
            VBox geneItem = new VBox();
            geneItem.setPadding(new Insets(5, 5, 5, 5));

            Label geneType = new Label(gene.getType());
            geneType.setAlignment(Pos.CENTER);
            geneType.setMinWidth(50);

            CheckBox genePresence = new CheckBox();
            genePresence.setMinWidth(50);

            geneItem.getChildren().add(geneType);
            geneItem.getChildren().add(genePresence);
            genesListHBox.getChildren().add(geneItem);
        }
    }

    @FXML
    public void drawGraphic(Cell cell) {

        drawGraphicButton.setOnMouseClicked(event -> {

            genesNumbers = new ArrayList<>();

            for (int i = 0; i < genesListHBox.getChildren().size(); i++) {
                Node checkBox = ((VBox) genesListHBox.getChildren().get(i)).getChildren().get(1);
                if (((CheckBox) checkBox).isSelected()) {
                    genesNumbers.add(i);
                }
            }

            int start = 0; // ????????
            int end = 5; // ????????
            int genesCount = cell.getGenes().length;
            double alpha[] = new double[genesCount];
            double[] y0 = new double[genesCount];
            double[][] betta = new double[genesCount][genesCount];
            int[][] gamma = new int[genesCount][genesCount];
            double[][] K = new double[genesCount][genesCount];
            int[] N = new int[genesCount];
            double[] reg = new double[genesCount];
            for (int i = 0; i < genesCount; i++) {
                alpha[i] = cell.getGenes()[i].getActivity();
                betta[i] = cell.getGenes()[i].getB();
                y0[i] = 0;
                gamma[i] = cell.getGenes()[i].getG();
                K[i] = cell.getGenes()[i].getK();
                N[i] = cell.getGenes()[i].getN();
                reg[i] = cell.getGenes()[i].getR();
            }

            int n = 500;
            double[][] result = cell.solve(start, end, n, y0, alpha, betta, reg, N, K, gamma);
            double[] tt = new double[n];
            for (int j = 0; j< tt.length; j++)
                tt[j] = j*0.01;

            final NumberAxis xAxis = new NumberAxis(start, end, 1);
            final NumberAxis yAxis = new NumberAxis(-100, 100, 1);

            AreaChart areaChart = new AreaChart(xAxis, yAxis);
            ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
            scatterChart.setTitle("Количество продуктов генов");
            xAxis.setLabel("T");
            yAxis.setLabel("P");
            scatterChart.setMinWidth(1500);

            Color[] markColors = new Color[5];
            markColors[0] = Color.RED;
            markColors[1] = Color.GREEN;
            markColors[2] = Color.BLUE;
            markColors[3] = Color.BLACK;
            markColors[4] = Color.MAGENTA;

            XYChart.Data<Number, Number> data = null;
            for (int i = 0; i < genesNumbers.size(); i++) {
                XYChart.Series series = new XYChart.Series();
                for (int j = 0; j < tt.length; j++) {
                    series.getData().add(new XYChart.Data(tt[j], result[genesNumbers.get(i)][j]));
                    series.setName(cell.getGenes()[genesNumbers.get(i)].getType());
                }
                areaChart.getData().addAll(series);
            }

            AnchorPane.setTopAnchor(scatterChart, 0.0);
            AnchorPane.setBottomAnchor(scatterChart, 0.0);
            AnchorPane.setLeftAnchor(scatterChart, 0.0);
            AnchorPane.setRightAnchor(scatterChart, 0.0);

            scatterChart.setLegendVisible(true);
            graphicPane.getChildren().add(areaChart);
        });
    }
}
