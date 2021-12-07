/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import mo.core.v2.model.Organization;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class NewProjectNameController implements Initializable {
    private final String CONTROLLER_KEY = "controller";

    @FXML
    private GridPane newProjectNameGridPane;
    @FXML
    private Text nameProjectText;
    @FXML
    private Text newProjectLocationLabel;
    @FXML
    private TextField newProjectLocationText;
    @FXML
    private Button browseButton;
    @FXML
    private Button finishButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Text textNewProject;
    @Inject
    public Injector injector;
    @Inject 
    Organization model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }    

    @FXML
    private void writeEvent(KeyEvent event) {
    }

    @FXML
    private void browseClick(MouseEvent event) {
    }

    @FXML
    private void finishClick(MouseEvent event) {
    }

    @FXML
    private void cancelClick(MouseEvent event) {
    }
    
    private void init(){
        System.out.println("3.1");
    }
    
}
