/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import static mo.core.Utils.getBaseFolder;
import mo.core.filemanagement.project.Project;
import mo.core.v2.model.Organization;
import mo.core.v2.model.OrganizationV2;
import mo.organization.ProjectOrganization;
import mo.wizardAnalysis.Controller.LayoutController;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class MainWindowsController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    private Project project;
    public ProjectOrganization projectOrg;
    
    @FXML
    private BorderPane mainPane;
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
    @FXML
    private Text textWelcome;
    @Inject
    public Injector injector;
    @Inject 
    Organization org;
    
    public ArrayList<Object> controllers;
  
    private String name;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void newProject(ActionEvent event) {
        try{
            Stage popUp = new Stage();
            textWelcome.setVisible(false);
            centerPane.getChildren().clear();
            newProjectCreation(popUp);
            
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(MainWindowsController.class
                .getResource("/fxml/core/ui/ProjectMenu.fxml"), null,
                builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                .put(CONTROLLER_KEY, loaderOpen.getController());
            System.out.println(org.getFileProject());
            centerPane.getChildren().add(openParent);
            
            
        }
        catch (IOException ex) {
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void myProjects(ActionEvent event) {
        try{
            textWelcome.setVisible(false);
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen= new FXMLLoader(MainWindowsController.class.
                    getResource("/fxml/core/ui/ProjectMenu.fxml"), null,
                    builderFactory, callback);
            
            Parent openParent = loaderOpen.load();
            
            openParent.getProperties().put(CONTROLLER_KEY, loaderOpen.getController());
            centerPane.getChildren().add(openParent);
        }
        catch (IOException ex){
            Logger.getLogger(LayoutController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void plugins(ActionEvent event) {
    }

    @FXML
    private void settings(ActionEvent event) {
        try{
            textWelcome.setVisible(false);
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen= new FXMLLoader(MainWindowsController.class.
                    getResource("/fxml/core/ui/SettingsMenu.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderOpen.load();
            
            openParent.getProperties().put(CONTROLLER_KEY, loaderOpen.getController());
            centerPane.getChildren().add(openParent);
        }
        catch (IOException ex){
            Logger.getLogger(LayoutController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }
    
    private void newProjectCreation(Stage popUp){
        Button btnCancel, btnCreate;
        Label lbl;
        btnCancel = new Button("Cancel");
        btnCreate = new Button("Create");
        TextField text = new TextField(getBaseFolder()+ "\\" +"newproject");
        btnCancel.setOnAction(e-> buttonModal(e, btnCreate, text.getText(), popUp));
        btnCreate.setOnAction(e-> buttonModal(e, btnCreate, text.getText(), popUp));
        lbl = new Label("Name of project");
        popUp.initModality(Modality.APPLICATION_MODAL);
        VBox pop = new VBox(20);
        pop.setStyle("-fx-padding:10px;");
        pop.getChildren().addAll(new Text("New project"), text, btnCreate, btnCancel);
        Scene popUp1 = new Scene(pop, 300, 200);
        popUp.setTitle("New project");
        popUp.setScene(popUp1);
        popUp.showAndWait();
        System.out.println(name);
        popUp.close();
        File proj = new File(name);
        proj.mkdir();
        projectOrg = new ProjectOrganization(name);
        org.setOrg(projectOrg);
        org.setFileProject(proj);
        
    }
    
    private void buttonModal(ActionEvent e, Button btn, String text, Stage popUp){
        if(e.getSource()==btn){
            System.out.println("Creado :D");
            name = text;
            popUp.close();
        }
        else{
            popUp.close();
        }
            
    }
    
}
