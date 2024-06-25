package arabidopsis.controllers;

import arabidopsis.db.GlobalGenes;
import arabidopsis.db.GlobalStates;
import arabidopsis.models.Gene;
import arabidopsis.models.State;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class ControllerAdd {
    @FXML
    private TextField idState;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField nameState;
    @FXML
    private TextArea descState;
    @FXML
    private VBox proteinStateList;
    @FXML
    private VBox nsStateList;
    @FXML
    private HBox influenceMatr;
    ///////// ??????
    @FXML
    public void initialize() throws IOException, ParseException {
        Gene[] genes = GlobalGenes.getGenes();

        for (Gene gene: genes) {
            VBox influenceMatrItem = new VBox();
            influenceMatrItem.setPadding(new Insets(5, 5, 5, 5));
            Label proteinType = new Label(gene.getType());
            proteinType.setAlignment(Pos.CENTER);
            proteinType.setMinWidth(50);
            TextField proteinInfluence = new TextField();
            proteinInfluence.setMinWidth(50);
            influenceMatrItem.getChildren().add(proteinType);
            influenceMatrItem.getChildren().add(proteinInfluence);
            influenceMatr.getChildren().add(influenceMatrItem);
        }

        for (int i = 0; i < genes.length; i++) {
            Label proteinTypeText = new Label(genes[i].getType());
            proteinTypeText.setMinWidth(100);
            HBox proteinItem = new HBox();
            proteinItem.setPadding(new Insets(5, 5, 5, 5));
            proteinItem.getChildren().add(proteinTypeText);
            TextField proteinCountText = new TextField();
            proteinCountText.setMinWidth(100);
            proteinItem.getChildren().add(proteinCountText);
            proteinStateList.getChildren().add(proteinItem);
        }

        List<State> globalStates = GlobalStates.getStates();
        for (int i = 0; i < globalStates.size(); i++) {
            Label stateNameText = new Label(globalStates.get(i).getName());
            stateNameText.setMinWidth(100);
            HBox stateItem = new HBox();
            stateItem.setPadding(new Insets(5, 5, 5, 5));
            stateItem.getChildren().add(stateNameText);

            CheckBox stateCheckBox = new CheckBox();
            stateCheckBox.setMinWidth(100);

            stateItem.getChildren().add(stateCheckBox);
            nsStateList.getChildren().add(stateItem);
        }
    }
    @FXML
    public void saveState() throws IOException, ParseException {
        int id = Integer.parseInt(idState.getText());
        String name = nameState.getText();
        String description = descState.getText();
        Color color = colorPicker.getValue();

        Map<String, Integer> proteins = new HashMap<>();
        Set<Integer> statesNS = new HashSet<>();
        for (int i = 0; i < proteinStateList.getChildren().size(); i++) {
            String proteinType = GlobalGenes.getGenes()[i].getType();
            Node columnCounts = ((HBox) proteinStateList.getChildren().get(i)).getChildren().get(1);
            String columnCountsStr = ((TextField) columnCounts).getText();
            if (!columnCountsStr.isEmpty()) {
                int proteinCount = Integer.parseInt(columnCountsStr);
                proteins.put(proteinType, proteinCount);
            }
        }

        for (int i = 0; i < nsStateList.getChildren().size(); i++) {
            int stateId = GlobalStates.getState(i).getId();
            Node columnCheckBox = ((HBox) nsStateList.getChildren().get(i)).getChildren().get(1);
            if (((CheckBox) columnCheckBox).isSelected()) {
                statesNS.add(stateId);
            }
        }

        Gene[] genes = GlobalGenes.getGenes();
        double[] influence = new double[genes.length];
        for (int i = 0; i < influenceMatr.getChildren().size(); i++) {
            Node infValText = ((VBox) influenceMatr.getChildren().get(i)).getChildren().get(1);
            String proteinCountsStr = ((TextField) infValText).getText();
            double infVal = 0;
            if (!proteinCountsStr.isEmpty())
                infVal = Double.parseDouble(proteinCountsStr);
            influence[i] = infVal;
        }

        State state = new State(id, proteins, name, description, influence, color, statesNS);
        GlobalStates.addState(state);
    }
}
