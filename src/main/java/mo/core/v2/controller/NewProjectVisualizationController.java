/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class NewProjectVisualizationController implements Initializable {

    @FXML
    private ScrollPane scrollPaneAnalysis;
    @FXML
    private GridPane gridPaneAnalysis;
    @FXML
    private ImageView iconVisualization;
    @FXML
    private Text textVisualization;
    @FXML
    private Circle addCircle;
    @FXML
    private ImageView addPlus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
