/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.core.v2.model.Organization;
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
        Stage popUp = new Stage();
        Label lbCaptures;
        Button btnCancel, btnAdd;
        btnCancel = new Button("Cancel");
        btnAdd = new Button("Add");
        lbCaptures = new Label("Plugins");
        ComboBox plugins = new ComboBox();
        //plugins.setItems();
        
    }
    
    private void init(){
        if(model.getPlugins().isEmpty()){
            iconCapture.opacityProperty().set(0.50);
            textCapture.opacityProperty().set(0.50);
            row=0;
        }
        else{
            
        }
                
    }
    
    public ObservableList<String> addObservablePlugin(){
        for(int i=0;i<model.getStageModules().size();i++){
            ObservablePlugins.add(model.getStageModules().get(i).getName());
        }
        return ObservablePlugins;
    }
    
    public void initCaptureStage(){
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            if(nodeProvider.getName().equals(model.getCaptureStage().getName())){
                //add stage
                model.setStageModule(nodeProvider); 
                System.out.println(model.getCaptureStage());
                break;
            }
        }
    }

    @FXML
    private void removeConfiguration(MouseEvent event) {
    }
}