/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class MainWindowsController implements Initializable {

    @FXML
    private Pane menuBar;
    @FXML
    private ImageView moIcon;
    @FXML
    private Button buttonNewProject;
    @FXML
    private Button buttonMyProjects;
    @FXML
    private Button buttonPlugins;
    @FXML
    private Button buttonSettings;
    @FXML
    private Button buttonExit;
    @FXML
    private Pane centerPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void newProject(ActionEvent event) {
    }

    @FXML
    private void myProjects(ActionEvent event) {
    }

    @FXML
    private void plugins(ActionEvent event) {
    }

    @FXML
    private void settings(ActionEvent event) {
    }

    @FXML
    private void exit(ActionEvent event) {
    }
    
}
