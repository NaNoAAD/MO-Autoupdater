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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class NewProjectNameController implements Initializable {

    @FXML
    private GridPane newProjectNameGridPane;
    @FXML
    private TextField nameProjectText;
    @FXML
    private TextField newProjectLocationText;
    @FXML
    private Button browseButton;
    @FXML
    private Button finishButton;
    @FXML
    private Button cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void browseClick(MouseEvent event) {
    }

    @FXML
    private boolean finishClick(MouseEvent event) {
        return true;
    }

    @FXML
    private void cancelClick(MouseEvent event) {
    }
    
}
