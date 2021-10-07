/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import mo.core.v2.model.Organization;
import mo.core.filemanagement.project.Project;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class MyProjectsController implements Initializable {

    @FXML
    private GridPane projectsGrid;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Text nameProject;
    @FXML
    private Text participantsNumber;
    @FXML
    private Text capturesNumber;
    @FXML
    private Text analysisNumber;
    @FXML
    private Button seeButton;
    @FXML
    private ImageView iconProject;
    @FXML
    private Text visualizationsNumber;
    @Inject
    public Injector injector;
    @Inject 
    Organization org;
    public ArrayList<Project> projects;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void seeProject(MouseEvent event) {
        
    }
    
    
}
