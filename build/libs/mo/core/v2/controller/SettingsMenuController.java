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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class SettingsMenuController implements Initializable {
    private final String CONTROLLER_KEY = "controller";

    @FXML
    private BorderPane settingsMenuPane;
    @FXML
    private ImageView closeButton;
    @FXML
    private Text nameProjectText;
    @FXML
    private Button serverButton;
    @FXML
    private Button emptyButton1;
    @FXML
    private Button emptyButton2;
    @FXML
    private Button emptyButton3;
    @FXML
    private AnchorPane centerPane;
    @Inject
    public Injector injector;

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
    private void clickServer(MouseEvent event) {
        try{
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(MainWindowsController.class
                .getResource("/fxml/core/ui/SettingsServer.fxml"), null,
                builderFactory, callback);
            Parent openParent = loaderOpen.load();
            
            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderOpen.getController());
            centerPane.getChildren().add(openParent);
            
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
            .getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
}
