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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import mo.core.MultimodalObserver;
import mo.core.filemanagement.project.Project;
import mo.core.preferences.AppPreferencesWrapper;
import mo.core.preferences.PreferencesManager;
import mo.core.v2.model.Organization;
import mo.organization.ProjectOrganization;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class MainWindowsController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    
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
    Organization model;
    private String name;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Class c = AppPreferencesWrapper.class;
        File prefFile = new File(MultimodalObserver.APP_PREFERENCES_FILE);
        AppPreferencesWrapper preferences = (AppPreferencesWrapper) PreferencesManager.loadOrCreate(c, prefFile);

        List<String> projectsNotFound = new ArrayList<>();
        preferences.getOpenedProjects().stream().forEach((openedProject) -> {
            File f = new File(openedProject.getLocation());
            System.out.println("File "+f);
            if (f.exists()) {
                //addFile(openedProject.getLocation()));
                
            } else {
                projectsNotFound.add(openedProject.getLocation());
            }

        });
        for (String projectPath : projectsNotFound) {
            preferences.removeOpenedProject(projectPath);
        }
        PreferencesManager.save(preferences, prefFile);
    }    

    @FXML
    private void newProject(ActionEvent event) {
        try{
            textWelcome.setVisible(false);
            centerPane.getChildren().clear();
            model.newProyect = 1;
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(MainWindowsController.class
                .getResource("/fxml/core/ui/ProjectMenu.fxml"), null,
                builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                .put(CONTROLLER_KEY, loaderOpen.getController());
            System.out.println(model.getFileProject());
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
                    getResource("/fxml/core/ui/MyProjects.fxml"), null,
                    builderFactory, callback);
            
            Parent openParent = loaderOpen.load();
            openParent.getProperties().put(CONTROLLER_KEY, loaderOpen.getController());
            centerPane.getChildren().add(openParent);
        }
        catch (IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void plugins(ActionEvent event) {
        try {
            textWelcome.setVisible(false);
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen= new FXMLLoader(MainWindowsController.class.
                    getResource("/fxml/core/ui/Plugins.fxml"), null,
                    builderFactory, callback);
            
            Parent openParent = loaderOpen.load();
            openParent.getProperties().put(CONTROLLER_KEY, loaderOpen.getController());
            centerPane.getChildren().add(openParent);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowsController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }
    
    public void buildSteps(){
        try{
            final JavaFXBuilderFactory bf = new JavaFXBuilderFactory();
            final Callback<Class<?>,Object> cb = (clazz) -> injector.getInstance(clazz);
            FXMLLoader fxmlLoaderPath = new FXMLLoader( MainWindowsController.class.getResource("/fxml/core/ui/MyProjects.fxml"), null, bf, cb);
            Parent myProjects = fxmlLoaderPath.load();
            myProjects.getProperties().put(CONTROLLER_KEY, fxmlLoaderPath.getController());
            
            FXMLLoader fxmlLoaderProject = new FXMLLoader( MainWindowsController.class.getResource("/fxml/core/ui/ProjectMenu.fxml"), null, bf, cb);
            Parent project = fxmlLoaderPath.load();
            project.getProperties().put(CONTROLLER_KEY, fxmlLoaderPath.getController());
            
        }
        catch(IOException ex){
            
        }
    }
}
