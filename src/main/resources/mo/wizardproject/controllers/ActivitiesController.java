/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardproject.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ActivitiesController implements Initializable {

    @FXML
    private AnchorPane anchorActivity;
    @FXML
    private Label labelTitle;
    @FXML
    private TableView<?> activityTable;
    @FXML
    private TableColumn<?, ?> orderColumn;
    @FXML
    private TableColumn<?, ?> activityNameColumn;
    @FXML
    private Label order;
    @FXML
    private Label activity;
    @FXML
    private Label path;
    @FXML
    private Label startMessage;
    @FXML
    private Label endMessage;
    @FXML
    private Label orderLabel;
    @FXML
    private Label activityNameLabel;
    @FXML
    private Label pathLabel;
    @FXML
    private Label startMessageLabel;
    @FXML
    private Label endMessageLabel;
    @FXML
    private Label executionTime;
    @FXML
    private Label activityTime;
    @FXML
    private Label closeWhenFinished;
    @FXML
    private Label processName;
    @FXML
    private Label closeWhenFinishedLabel;
    @FXML
    private Label processNameLabel;
    @FXML
    private JFXButton addActivityButton;
    @FXML
    private JFXButton editActivityButton;
    @FXML
    private JFXButton removeActivityButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleNewActivity(ActionEvent event) {
    }

    @FXML
    private void handleEditActivity(ActionEvent event) {
    }

    @FXML
    private void handleDeleteActivity(ActionEvent event) {
    }
    
}
