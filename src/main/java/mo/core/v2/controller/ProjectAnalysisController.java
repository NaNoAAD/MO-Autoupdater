/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import mo.core.v2.model.Organization;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectAnalysisController implements Initializable {

    @FXML
    private ScrollPane scrollPaneAnalysis;
    @FXML
    private GridPane gridPaneAnalysis;
    @FXML
    private ImageView iconAnalysis;
    @FXML
    private Text textAnalysis;
    @FXML
    private Circle circleAdd;
    @FXML
    private ImageView imageAdd;
    @FXML
    private ImageView deleteButton;
    private int row;
    @Inject
    Organization model;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addClick(MouseEvent event) {
    }

    @FXML
    private void removeConfiguration(MouseEvent event) {
    }
 
    private void init(){
        if(model.getAnalysis().isEmpty()){
            model.setCaptures(model.getStageModules());
            model.getStageModules().clear();
            iconAnalysis.opacityProperty().set(0.50);
            textAnalysis.opacityProperty().set(0.50);
            row=0;
        }
        
    }
}
