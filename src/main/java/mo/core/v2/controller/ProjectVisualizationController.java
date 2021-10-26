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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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
public class ProjectVisualizationController implements Initializable {

    @FXML
    private ScrollPane scrollPaneAnalysis;
    @FXML
    private GridPane gridPaneAnalysis;
    @FXML
    private ImageView iconVisualization;
    @FXML
    private Text textVisualization;
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
        init();
        addObservablePlugin();
        initVisualizationStage();
    }    

    @FXML
    private void addClick(MouseEvent event) {
    }

    @FXML
    private void removeConfiguration(MouseEvent event) {
    }
    
    private void init(){
        if(model.getPlugins().isEmpty()){
            iconVisualization.opacityProperty().set(0.50);
            textVisualization.opacityProperty().set(0.50);
            deleteButton.opacityProperty().set(0.50);
            row=0;
        }
    }
    
    public ObservableList<String> addObservablePlugin(){
        System.out.println("Observable 3: "+model.getOrg().getStages().get(2).getName());
        if(ObservablePlugins.isEmpty()){
            ObservablePlugins.add(model.getOrg().getStages().get(2).getName());
        }
        return ObservablePlugins;
    }
    
    private void initVisualizationStage(){
        ObservablePlugins.clear();
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        System.out.println(stagePlugins);
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            System.out.println(nodeProvider.getName());
            if(!nodeProvider.getName().equals("Captura")){
                //add stages
                System.out.println("Dentro del if");
                model.setStageModule(nodeProvider); 
                List<StageAction> actions = nodeProvider.getActions();
                System.out.println("Acciones visualizacion: "+actions);
                for (StageAction action : actions){
                    ObservablePlugins.add(action.getName());
                }
                System.out.println(ObservablePlugins);
                break;
            }
        }
    }
}
