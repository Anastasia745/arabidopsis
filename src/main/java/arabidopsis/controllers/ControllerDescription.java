package arabidopsis.controllers;

import arabidopsis.controllers.ControllerMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ControllerDescription
{
    @FXML
    private Label stateNameLabel;
    @FXML
    private TextArea stateDescTextArea;
    @FXML
    private Button applyDescButton;
    private ControllerMain controllerMain;
    @FXML
    public void initialize() {
        applyDescButton.setOnMouseClicked(event -> controllerMain.setStateDescription(stateDescTextArea.getText()));
    }

    @FXML
    public void setData(ControllerMain controllerMain, String name, String description) {
        this.controllerMain = controllerMain;
        stateNameLabel.setText(name);
        stateDescTextArea.setText(description);
    }
}