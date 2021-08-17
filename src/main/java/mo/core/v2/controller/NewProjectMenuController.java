/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import mo.organization.ProjectOrganization;
import mo.wizardAnalysis.Controller.LayoutController;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class NewProjectMenuController implements Initializable {
     private final String CONTROLLER_KEY = "controller";
     

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
    @Inject
    public Injector injector;
    @FXML
    public Pane centerPane;
    private ProjectOrganization projectOrg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public NewProjectMenuController(ProjectOrganization projectOrg){
        this.projectOrg = projectOrg;
    }
    
    @FXML
    private void closeButton(MouseEvent event) {
    }

    @FXML
    private void clickParticipants(MouseEvent event) {
        try{
            
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderNewParticipants = new FXMLLoader(MainWindowsController.class
                .getResource("/fxml/core/ui/NewProjectParticipants.fxml"), null,
                builderFactory, callback);

            Parent openParent = loaderNewParticipants.load();

            openParent.getProperties()
                .put(CONTROLLER_KEY, loaderNewParticipants.getController());
            centerPane.getChildren().add(openParent);
        }
        catch (IOException ex) {
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @FXML
    private void clickCaptures(MouseEvent event) {
        try{
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderNewCaptures = new FXMLLoader(MainWindowsController.class
                    .getResource("/fxml/core/ui/NewProjectCaptures.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderNewCaptures.load();

            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderNewCaptures.getController());
            centerPane.getChildren().add(openParent);
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void clickAnalysis(MouseEvent event) {
    }

    @FXML
    private void clickVisualizations(MouseEvent event) {
    }
    
}
