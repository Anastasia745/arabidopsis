package arabidopsis.controllers;
import arabidopsis.Main;
import arabidopsis.db.GlobalGenes;
import arabidopsis.db.GlobalStates;
import arabidopsis.db.GlobalSystem;
import arabidopsis.models.Cell;
import arabidopsis.models.Gene;
import arabidopsis.models.Plant;
import arabidopsis.models.State;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.*;
import static arabidopsis.db.GlobalCells.*;
import static arabidopsis.db.GlobalSystem.*;

import org.json.simple.parser.ParseException;
;
public class ControllerMain {
    @FXML
    private Pane out1;
    @FXML
    private VBox genesTable;
    @FXML
    private Button drawGraphicButton;
    @FXML
    private Label cellIdLabel;
    @FXML
    private ChoiceBox cellStatesChoiceBox;
    @FXML
    private ChoiceBox divChoiceBox;
    @FXML
    private Pane matrPane;
    @FXML
    private TextField bettaField;
    @FXML
    private TextField gammaField;
    @FXML
    private TextField KField;
    @FXML
    private TextField activityField;
    @FXML
    private TextField NField;
    @FXML
    private TextField rField;
    @FXML
    private Pane sysImg;
    @FXML
    private TextField RField;
    @FXML
    private TextField DField;
    @FXML
    private TextField d0Field;
    @FXML
    private TextField RmaxField;
    @FXML
    private TextField RminField;
    @FXML
    private ChoiceBox statesChoiceBox;
    @FXML
    private TextField idStateField;
    @FXML
    private TextField nameStateField;
    @FXML
    private TextField descStateField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private VBox proteinStateList;
    @FXML
    private VBox nsStateList;

    private int zone;
    private static final int koef = 1200;
    private Plant plant;
    private Cell chosenCellForTake = null;
    private Rectangle chosenRectangle = null;
    private int currStateId;
    private int chosenGeneX;
    private int chosenGeneY;
    private Set<Integer> statesNS;

    public void setStateDescription(String stateDescription) {
        this.descStateField.setText(stateDescription);
    }

    private List<Circle> circlesNs = new ArrayList<>();
    public void initialize() throws IOException, ParseException {
        zone = 0;
        chosenGeneX = -1;
        chosenGeneY = -1;
        plant = new Plant();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Platform.runLater(() -> {
                if (timeline.getStatus() == Animation.Status.RUNNING) {
                    System.out.println("--------------");
                    try {
                        plant.growth();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    setMeristemView(plant);
                }
            });
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        /* РАБОТА С СОСТОЯНИЯМИ */
        outStates();

        /* МАТРИЦА ГЕНОВ */
        drawGenesMatr();

        /* ИЗОБРАЖЕНИЕ СИСТЕМЫ */
        drawSystem();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////    1 ВКЛАДКА    ////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    /* ИЗОБРАЖЕНИЕ КЛЕТОК (ГРАФ) */
    protected void setMeristemView(Plant plant)
    {
        System.out.println(plant.toString());
        this.out1.getChildren().clear();
        genesTable.getChildren().clear();
        chosenCellForTake = null;
        for (Cell cell: plant.getCells())
        {
            double x = Math.random() * 350;
            double y = Math.random() * 350;
            Circle circle = new Circle(cell.getWidth()/10);
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setFill(Color.BLUE);
            circle.setId(String.valueOf(cell.getId()));

            circle.setOnMouseClicked(event -> {
                if (timeline.getStatus() != Animation.Status.RUNNING) {
                    if (event.getClickCount() == 2) {
                        if (chosenCellForTake != null)
                            chosenCellForTake.getCircle().setStroke(null);
                        chosenCellForTake = cell;
                        chosenCellForTake.setCircle(circle);
                        circle.setStroke(Color.RED);
                        try {
                            takeCell(cell);
                            outCellProperties(cell);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            });
            circle.setOnMouseDragged(event -> {
                if (timeline.getStatus() != Animation.Status.RUNNING) {
                    circle.setCenterX(event.getX());
                    circle.setCenterY(event.getY());
                    moveCircle();
                }
            });
            drawGraphicButton.setOnMouseClicked(e -> {
                openGraphic(chosenCellForTake);
            });
            Text text = new Text(String.valueOf(cell.getId()));
            text.setTranslateX(x);
            text.setTranslateY(y);
            this.out1.getChildren().add(circle);
            //this.out1.getChildren().add(text);
            cell.setCircle(circle);
        }
    }

    @FXML
    /* ВЫДЕЛЕНИЕ КЛЕТКИ */
    protected void takeCell(Cell cell) throws IOException, ParseException {
        Group lines = (Group) this.out1.lookup("#lines");
        if (lines != null)
            this.out1.getChildren().remove(lines);

        List<Integer> ns = cell.getNs().get("Left");
        ns.addAll(cell.getNs().get("Right"));
        ns.addAll(cell.getNs().get("Up"));
        ns.addAll(cell.getNs().get("Down"));

        Group linesGroup = new Group();
        linesGroup.setId("lines");
        circlesNs.clear();
        for (int id : ns) {
            Circle cn = (Circle)this.out1.lookup("#" + id);
            circlesNs.add(cn);
            double x2 = cn.getCenterX();
            double y2 = cn.getCenterY();
            double r2 = cn.getRadius();
            double[] xy = chosenCellForTake.calcXY(x2, y2, r2);
            Line line = new Line(xy[0], xy[1], xy[2], xy[3]);
            linesGroup.getChildren().add(line);
        }
        this.out1.getChildren().add(linesGroup);
        //openGraphic(cell);
        //drawChart(cell);
    }

    @FXML
    /* ПЕРЕМЕЩЕНИЕ КЛЕТКИ */
    protected void moveCircle() {
        Group lines = (Group) this.out1.lookup("#lines");
        if (lines != null) {
            for (int i = 0; i < circlesNs.size(); i++) {
                double x2 = circlesNs.get(i).getCenterX();
                double y2 = circlesNs.get(i).getCenterY();
                double r2 = circlesNs.get(i).getRadius();
                double[] xy = chosenCellForTake.calcXY(x2, y2, r2);
                ((Line)lines.getChildren().get(i)).setStartX(xy[0]);
                ((Line)lines.getChildren().get(i)).setStartY(xy[1]);
                ((Line)lines.getChildren().get(i)).setEndX(xy[2]);
                ((Line)lines.getChildren().get(i)).setEndY(xy[3]);
            }
        }
    }

    @FXML
    /* ВЫВОД НАСТРОЕК КЛЕТКИ */
    protected void outCellProperties(Cell cell) throws IOException, ParseException {
        cellIdLabel.setText(String.valueOf(cell.getId()));
        int stateId = cell.getState().getId();
        int div = cell.getDiv();
        for (State state: GlobalStates.getStates())
            cellStatesChoiceBox.getItems().add(state.getName());
        cellStatesChoiceBox.setValue(cellStatesChoiceBox.getItems().get(stateId-1));
        divChoiceBox.getItems().add("H");
        divChoiceBox.getItems().add("V");
        divChoiceBox.setValue(divChoiceBox.getItems().get(div));

        for (int i = 0; i < cell.getGenes().length; i++) {
            Label type = new Label(cell.getGenes()[i].getType());
            type.setAlignment(Pos.CENTER_LEFT);
            type.setMinWidth(100);

            TextField activity = new TextField(String.valueOf(cell.getGenes()[i].getActivity()));
            StackPane.setAlignment(activity, Pos.CENTER_LEFT);
            HBox.setMargin(activity, new Insets(0, 5, 0, 5));
            activity.setMinWidth(100);

            TextField count = new TextField(String.valueOf(cell.getProducts()[i].getCount()));
            StackPane.setAlignment(count, Pos.CENTER_LEFT);
            HBox.setMargin(count, new Insets(0, 5, 0, 5));
            count.setMinWidth(100);

            HBox row = new HBox();
            row.getChildren().add(type);
            row.getChildren().add(activity);
            row.getChildren().add(count);
            row.setPadding(new Insets(5, 5, 5, 5));
            genesTable.getChildren().add(row);
        }
    }

    /* ИЗМЕНИТЬ ПАРАМЕТРЫ КЛЕТКИ */
    @FXML
    protected void applyCellProperties() throws IOException, ParseException {
        int stateId = cellStatesChoiceBox.getSelectionModel().getSelectedIndex();
        State state = GlobalStates.getState(stateId);
        int div = divChoiceBox.getSelectionModel().getSelectedIndex();
        chosenCellForTake.setState(state);
        chosenCellForTake.setDiv(div);

        for (int i = 0; i < genesTable.getChildren().size(); i++) {
            HBox geneRow = (HBox) genesTable.getChildren().get(i);
            double geneActivity = Double.parseDouble(((TextField) geneRow.getChildren().get(1)).getText());
            double productCount = Double.parseDouble(((TextField) geneRow.getChildren().get(2)).getText());
            chosenCellForTake.getGenes()[i].setActivity(geneActivity);
            chosenCellForTake.getProducts()[i].setCount(productCount);
        }
    }

    @FXML
    /* ОТКРЫТЬ ОКНО С ГРАФИКОМ */
    protected void openGraphic(Cell cell) {

        Stage graphicStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("graphicUi.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            graphicStage.setScene(scene);
            graphicStage.setTitle("ГРАФИК ВЗАИМОДЕЙСТВИЯ ГЕНОВ");
            graphicStage.show();
            ControllerGraphic controllerGraphic = fxmlLoader.getController();
            controllerGraphic.drawGraphic(cell);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    /* ОТОБРАЗИТЬ СПИСОК СОСТОЯНИЙ */
    protected void outStates() throws IOException, ParseException {
        statesChoiceBox.getItems().clear();
        List<State> globalStates = GlobalStates.getStates();
        for (State state: globalStates)
            statesChoiceBox.getItems().add(state.getName());
        statesChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int j = 0;
            if (Integer.parseInt(newValue.toString()) != -1)
                j = Integer.parseInt(newValue.toString());
            try {
                outStateProperties(GlobalStates.getState(j));
                currStateId = Integer.parseInt(newValue.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    /* ДОБАВИТЬ СОСТОЯНИЕ */
    protected void addState() {
        Stage addStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addUi.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            addStage.setScene(scene);
            addStage.setTitle("ДОБАВИТЬ СОСТОЯНИЕ");
            addStage.show();
            fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    /* ВЫВОД НАСТРОЕК СОСТОЯНИЯ */
    protected void outStateProperties(State state) throws IOException, ParseException {
        proteinStateList.getChildren().clear();
        nsStateList.getChildren().clear();

        idStateField.setText(String.valueOf(state.getId()));
        nameStateField.setText(String.valueOf(state.getName()));
        descStateField.setText(String.valueOf(state.getDescription()));
        colorPicker.setValue(state.getColor());

        for (Gene gene: GlobalGenes.getGenes()) {
            String proteinType = gene.getType();
            int proteinCount = 0;
            if (state.getProteins().containsKey(proteinType))
                proteinCount = state.getProteins().get(proteinType);
            HBox proteinItem = new HBox();
            proteinItem.setPadding(new Insets(5, 5, 5, 5));
            Label proteinTypeText = new Label(proteinType);
            proteinTypeText.setMinWidth(100);
            proteinItem.getChildren().add(proteinTypeText);
            TextField proteinCountText = new TextField(String.valueOf(proteinCount));
            proteinItem.getChildren().add(proteinCountText);
            proteinCountText.setMinWidth(100);
            proteinStateList.getChildren().add(proteinItem);
        }

        this.statesNS = new HashSet<>();
        List<State> globalStates = GlobalStates.getStates();
        for (int i = 0; i < globalStates.size(); i++) {
            HBox stateNsItem = new HBox();
            stateNsItem.setPadding(new Insets(5, 5, 5, 5));
            Label stateNameText = new Label(String.valueOf(globalStates.get(i).getName()));
            stateNameText.setMinWidth(100);
            int currStateNum = i;
            CheckBox stateCheckBox = new CheckBox();
            stateCheckBox.setMinWidth(100);
            if (state.getStatesNS().contains(globalStates.get(i)))
                stateCheckBox.setSelected(true);
            stateCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected)
                    statesNS.add(globalStates.get(currStateNum).getId());
                else
                    statesNS.remove(globalStates.get(currStateNum).getId());
            });
            stateNsItem.getChildren().add(stateNameText);
            stateNsItem.getChildren().add(stateCheckBox);
            nsStateList.getChildren().add(stateNsItem);
        }
    }

    /* КНОПКИ, РЕГУЛИРУЮЩИЕ СПИСОК СОСТОЯНИЙ */

    @FXML
    /* РАЗВЕРНУТЬ ОПИСАНИЕ ТЕКУЩЕГО СОСТОЯНИЯ В ОТДЕЛЬНОМ ОКНЕ */
    protected void openDescriptionState() throws IOException, ParseException {
        State state = GlobalStates.getState(currStateId);
        Stage descStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("stateDescriptionUi.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 300);
            descStage.setScene(scene);
            descStage.setTitle("ОПИСАНИЕ");
            descStage.show();
            ControllerDescription controllerStateDesc = fxmlLoader.getController();
            controllerStateDesc.setData(this, state.getName(), state.getDescription());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    /* ПРИМЕНЕНИЕ ИЗМЕНЕНИЕ К ПАРАМЕТРАМ ТЕКУЩЕГО СОСТОЯНИЯ */
    protected void applyStateProperties() throws IOException, ParseException {
        State state = GlobalStates.getState(currStateId);
        int id = Integer.parseInt(idStateField.getText());
        String name = nameStateField.getText();
        String description = descStateField.getText();
        Color color = colorPicker.getValue();
        Map<String, Integer> proteins = new HashMap<>();
        GlobalStates.setState(id, proteins, name, description, state.getInfluence(), color, statesNS);
    }

    @FXML
    /* УДАЛЕНИЕ ТЕКУЩЕГО СОСТОЯНИЯ */
    protected void removeState() throws IOException, ParseException {
        GlobalStates.deleteState(currStateId);
        statesChoiceBox.getItems().clear();
        List<State> globalStates = GlobalStates.getStates();
        for (State state: globalStates)
            statesChoiceBox.getItems().add(state.getName());
        proteinStateList.getChildren().clear();
        nsStateList.getChildren().clear();
        idStateField.clear();
        nameStateField.clear();
        descStateField.clear();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////    2 ВКЛАДКА    ////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    /* МАТРИЦА ГЕНОВ */
    protected void drawGenesMatr() throws IOException, ParseException {
        Gene[] genes = GlobalGenes.getGenes();
        DoubleBinding bindingX = new DoubleBinding() {
            @Override
            protected double computeValue() {
                return 0;
            }
        };
        DoubleBinding bindingY = new DoubleBinding() {
            @Override
            protected double computeValue() {
                return 0;
            }
        };
        for (int i = 0; i < genes.length; i++) {
            for (int j = 0; j < genes.length; j++) {
                Rectangle rectangle = new Rectangle(0, 0, matrPane.getPrefWidth(), matrPane.getPrefHeight());
                rectangle.setFill(Color.BLACK);
                rectangle.setStroke(Color.GREEN);
                rectangle.setStrokeWidth(1);
                rectangle.widthProperty().bind(matrPane.widthProperty().divide(genes.length));
                rectangle.heightProperty().bind(matrPane.heightProperty().divide(genes.length));

                rectangle.translateXProperty().bind(bindingX);
                rectangle.translateYProperty().bind(bindingY);
                int geneX = i;
                int geneY = j;
                rectangle.setOnMouseClicked(event -> {
                    chosenGeneX = geneX;
                    chosenGeneY = geneY;
                    if (event.getClickCount() == 2) {
                        if (chosenRectangle != null)
                            chosenRectangle.setStrokeWidth(1);
                        rectangle.setStrokeWidth(3);
                        rectangle.toFront();
                        chosenRectangle = rectangle;
                        try {
                            takeGene(geneX, geneY);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                matrPane.getChildren().add(rectangle);
                genes[i].setRectangle(rectangle, j);
                bindingX = bindingX.add(rectangle.widthProperty());
                if (j == genes.length - 1) {
                    bindingX = new DoubleBinding() {
                        @Override
                        protected double computeValue() {
                            return 0;
                        }
                    };
                    bindingY = bindingY.add(rectangle.heightProperty());
                }
            }
        }
    }

    @FXML
    /* СОХРАНЕНИЕ ПАРАМЕТРОВ ГЕНА */
    protected void applyGeneParameters() throws IOException, ParseException {
        if (chosenGeneX != -1 && chosenGeneY != -1) {
            int i = chosenGeneX;
            int j = chosenGeneY;
            double a = Double.parseDouble(activityField.getText());
            double b = Double.parseDouble(bettaField.getText());
            int g = Integer.parseInt(gammaField.getText());
            double k = Double.parseDouble(KField.getText());
            int N = Integer.parseInt(NField.getText());
            double r = Double.parseDouble(rField.getText());

            GlobalGenes.setGene(i, j, a, b, g, k, N, r);

//            GlobalGenes.getGenes()[i].setActivity(a);
//            GlobalGenes.getGenes()[i].setB(b, j);
//            GlobalGenes.getGenes()[i].setG(g, j);
//            GlobalGenes.getGenes()[i].setK(k, j);
//            GlobalGenes.getGenes()[i].setN(N);
//            GlobalGenes.getGenes()[i].setR(r);
        }
    }

    @FXML
    /* ВЫДЕЛЕНИЕ ГЕНА В МАТРИЦЕ */
    protected void takeGene(int geneX, int geneY) throws IOException, ParseException {
        if (timeline.getStatus() == Animation.Status.RUNNING)
            return;
        outGeneProperties(geneX, geneY);
    }

    @FXML
    /* ВЫВОД НАСТРОЕК ГЕНА */
    protected void outGeneProperties(int geneX, int geneY) throws IOException, ParseException {
        Gene[] genes = GlobalGenes.getGenes();
        bettaField.setText(String.valueOf(genes[geneX].getB()[geneY]));
        gammaField.setText(String.valueOf(genes[geneX].getG()[geneY]));
        KField.setText(String.valueOf(genes[geneX].getK()[geneY]));
        activityField.setText(String.valueOf(genes[geneX].getActivity()));
        NField.setText(String.valueOf(genes[geneX].getN()));
        rField.setText(String.valueOf(genes[geneX].getR()));
    }

    @FXML
    /* ИЗОБРАЖЕНИЕ СИСТЕМЫ */
    protected void drawSystem() {
        /* РИСУНОК СИСТЕМЫ */
        Circle circleR = new Circle();
        circleR.setStroke(Color.WHITE);
        circleR.setStrokeWidth(2);
        circleR.setFill(Color.TRANSPARENT);
        circleR.centerXProperty().bind(sysImg.widthProperty().divide(2));
        circleR.centerYProperty().bind(sysImg.heightProperty().divide(2));
        //circleR.radiusProperty().bind(sysImg.widthProperty().divide(koef).multiply(R));
        circleR.setRadius(RSystem);

        Circle circleCenter = new Circle();
        circleCenter.setFill(Color.RED);
        circleCenter.centerXProperty().bind(sysImg.widthProperty().divide(2));
        circleCenter.centerYProperty().bind(sysImg.heightProperty().divide(2));
        circleCenter.setRadius(5);

        sysImg.getChildren().add(circleR);
        sysImg.getChildren().add(circleCenter);
    }

    @FXML
    /* ИЗОБРАЖЕНИЕ ЧАШЕЛИСТИКОВ */
    protected void drawSepals() {
        zone = 1;
        sysImg.getChildren().clear();
        drawSystem();
        drawBumps(RmaxSepals, RminSepals, DSepals, d0Sepals, Color.LIGHTBLUE, Color.MAGENTA);
    }

    @FXML
    /* ИЗОБРАЖЕНИЕ ЛЕПЕСТКОВ */
    protected void drawPetals() {
        zone = 2;
        sysImg.getChildren().clear();
        drawSystem();
        drawBumps(RminSepals, RminPetals, DPetals, d0Petals, Color.MAGENTA, Color.YELLOW);
    }

    @FXML
    /* ИЗОБРАЖЕНИЕ ТЫЧИНОК */
    protected void drawsStamens() {
        zone = 3;
        sysImg.getChildren().clear();
        drawSystem();
        drawBumps(RminPetals, RminStamens, DStamens, d0Stamens, Color.YELLOW, Color.LIGHTGREEN);
    }

    @FXML
    /* СОХРАНЕНИЕ РАДИУСОВ И ДИАМЕТРОВ СИСТЕМЫ */
    protected void applySystemParameters() throws IOException, ParseException {
        RSystem = Double.parseDouble(RField.getText());
        GlobalSystem.setSystemParameter("R", RSystem);
        switch (zone) {
            case 1:
                RmaxSepals = Double.parseDouble(RmaxField.getText());
                RminSepals = Double.parseDouble(RminField.getText());
                DSepals = Double.parseDouble(DField.getText());
                d0Sepals = Double.parseDouble(d0Field.getText());
                GlobalSystem.setSystemParameter("RmaxSepals", RmaxSepals);
                GlobalSystem.setSystemParameter("RminSepals", RminSepals);
                GlobalSystem.setSystemParameter("DSepals", DSepals);
                GlobalSystem.setSystemParameter("d0Sepals", d0Sepals);
                break;
            case 2:
                RminSepals = Double.parseDouble(RmaxField.getText());
                RminPetals = Double.parseDouble(RminField.getText());
                DPetals = Double.parseDouble(DField.getText());
                d0Petals = Double.parseDouble(d0Field.getText());
                GlobalSystem.setSystemParameter("RminSepals", RminSepals);
                GlobalSystem.setSystemParameter("RminPetals", RminPetals);
                GlobalSystem.setSystemParameter("DPetals", DPetals);
                GlobalSystem.setSystemParameter("d0Petals", d0Petals);
                break;
            case 3:
                RminPetals = Double.parseDouble(RmaxField.getText());
                RminStamens = Double.parseDouble(RminField.getText());
                DStamens = Double.parseDouble(DField.getText());
                d0Stamens = Double.parseDouble(d0Field.getText());
                GlobalSystem.setSystemParameter("RminPetals", RminPetals);
                GlobalSystem.setSystemParameter("RminStamens", RminStamens);
                GlobalSystem.setSystemParameter("DStamens", DStamens);
                GlobalSystem.setSystemParameter("d0Stamens", d0Stamens);
                break;
        }
    }

    @FXML
    /* ПОСТРОЕНИЕ МЕРИСТЕМАТИЧЕСКИХ БУГОРКОВ */
    protected void drawBumps(double Rmax, double Rmin, double D, double d0, Color ColorMax, Color ColorMin) {
        RField.setText(String.valueOf(RSystem));
        DField.setText(String.valueOf(D));
        d0Field.setText(String.valueOf(d0));
        RmaxField.setText(String.valueOf(Rmax));
        RminField.setText(String.valueOf(Rmin));
        Circle circleRmax = new Circle();
        circleRmax.setStroke(ColorMax);
        circleRmax.setFill(Color.TRANSPARENT);
        circleRmax.centerXProperty().bind(sysImg.widthProperty().divide(2));
        circleRmax.centerYProperty().bind(sysImg.heightProperty().divide(2));
        //circleRmax.radiusProperty().bind(sysImg.widthProperty().divide(koef).multiply(Rmax));
        circleRmax.setRadius(Rmax);

        Circle circleRmin = new Circle();
        circleRmin.setStroke(ColorMin);
        circleRmin.setFill(Color.TRANSPARENT);
        circleRmin.centerXProperty().bind(sysImg.widthProperty().divide(2));
        circleRmin.centerYProperty().bind(sysImg.heightProperty().divide(2));
        //circleRmin.radiusProperty().bind(sysImg.widthProperty().divide(koef).multiply(Rmin));
        circleRmin.setRadius(Rmin);

        sysImg.getChildren().add(circleRmax);
        sysImg.getChildren().add(circleRmin);

        Random random = new Random();
        double randAngle = Math.toRadians(random.nextInt(360));

        double radius = Rmin + (Rmax - Rmin) / 2;
        double x = radius * Math.cos(randAngle);
        double y = radius * Math.sin(randAngle);

        Circle bump = new Circle();
        bump.centerXProperty().bind(sysImg.widthProperty().divide(2).add(x));
        bump.centerYProperty().bind(sysImg.heightProperty().divide(2).add(y));
        bump.setRadius(d0);
        bump.setFill(ColorMax);

        Circle inhibZone = new Circle();
        inhibZone.centerXProperty().bind(sysImg.widthProperty().divide(2).add(x));
        inhibZone.centerYProperty().bind(sysImg.heightProperty().divide(2).add(y));
        inhibZone.setRadius(D);
        inhibZone.setStroke(Color.LIGHTBLUE);
        inhibZone.setFill(Color.TRANSPARENT);

        sysImg.getChildren().add(bump);
        sysImg.getChildren().add(inhibZone);

        double sinG = D / (2 * radius);
        double angleB = Math.asin(sinG) * 2;
        int countBumps = (int) (360 / (180 * angleB / Math.PI)) - 1;
        double angle = randAngle + angleB;

        for (int i = 0; i < countBumps; i++) {
            x = radius * Math.cos(angle);
            y = radius * Math.sin(angle);

            bump = new Circle();
            bump.centerXProperty().bind(sysImg.widthProperty().divide(2).add(x));
            bump.centerYProperty().bind(sysImg.heightProperty().divide(2).add(y));
            bump.setRadius(d0);
            bump.setFill(ColorMax);

            inhibZone = new Circle();
            inhibZone.centerXProperty().bind(sysImg.widthProperty().divide(2).add(x));
            inhibZone.centerYProperty().bind(sysImg.heightProperty().divide(2).add(y));
            inhibZone.setRadius(D);
            inhibZone.setStroke(Color.LIGHTBLUE);
            inhibZone.setFill(Color.TRANSPARENT);

            sysImg.getChildren().add(bump);
            sysImg.getChildren().add(inhibZone);
            angle += angleB;
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    /* ЗАПУСК ТАЙМЕРА */ public void startSimulation() {
        if (timeline.getStatus() == Animation.Status.RUNNING)
            return;
        timeline.play();
    }

    @FXML
    /* ОСТАНОВКА ТАЙМЕРА */
    protected void stopSimulation() {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.pause();
        }
    }
}