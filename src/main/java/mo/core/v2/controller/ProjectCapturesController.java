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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.core.v2.model.Organization;
import mo.organization.StageAction;
import mo.organization.StageModule;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectCapturesController implements Initializable {
    private final String CONTROLLER_KEY = "controller";

    @FXML
    private ScrollPane scrollPaneCapture;
    @FXML
    private GridPane gridPaneCapture;
    @FXML
    private ImageView iconCapture;
    @FXML
    private Text textCapture;
    @FXML
    private Circle circleAdd;
    @FXML
    private ImageView imageAdd;
    @FXML
    private ImageView deleteButton;
    @Inject
    public Injector injector;
    @Inject
    Organization model;
    private int row;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        init();
        addObservablePlugin();
        initCaptureStage();
    }    

    @FXML
    private void addClick(MouseEvent event) {
        try{
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(MainWindowsController.class
                .getResource("/fxml/core/ui/AddCapture.fxml"), null,
                builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle("Add Capture");
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
            /*FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/core/ui/AddCapture.fxml"));
            Parent parent = (Parent)fxmlloader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.showAndWait();*/
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void init(){
        if(model.getPlugins().isEmpty()){
            iconCapture.opacityProperty().set(0.50);
            textCapture.opacityProperty().set(0.50);
            row=0;
        }                
    }
    
    public ObservableList<String> addObservablePlugin(){
        //System.out.println("Observable 1: "+model.getOrg().getStages().get(1).getName());
        if(ObservablePlugins.isEmpty()){
            ObservablePlugins.add(model.getOrg().getStages().get(1).getName());
        }
        return ObservablePlugins;
    }
    
    public void initCaptureStage(){
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            System.out.println(nodeProvider.getName());
            System.out.println(ObservablePlugins);
            if(nodeProvider.getName().equals("Captura")){
                //add stage
                model.setStageModule(nodeProvider); 
                List<StageAction> actions = nodeProvider.getActions();
                for (StageAction action : actions){
                    ObservablePlugins.add(action.getName());
                }
                System.out.println(ObservablePlugins);
                break;
            }
        }
        model.setObservablePlugins(ObservablePlugins);
        System.out.println("Ob " + ObservablePlugins);
        System.out.println("model " + model.getObservablePlugins());
    }
    

    @FXML
    private void removeConfiguration(MouseEvent event) {
    }
    
    private void addStageMenu(StageModule stage){
        
        
    }
}