/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.core.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class NewProjectMenuController implements Initializable {

    @FXML
    private BorderPane newProjectMenuPane;
    @FXML
    private ImageView closeButton;
    @FXML
    private Button participantsButton;
    @FXML
    private Button captureButton;
    @FXML
    private Button analysisButton;
    @FXML
    private Button visualizationButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeButton(MouseEvent event) {
    }

    @FXML
    private void clickParticipants(MouseEvent event) {
    }

    @FXML
    private void clickCaptures(MouseEvent event) {
    }

    @FXML
    private void clickAnalysis(MouseEvent event) {
    }

    @FXML
    private void clickVisualizations(MouseEvent event) {
    }
    
}
