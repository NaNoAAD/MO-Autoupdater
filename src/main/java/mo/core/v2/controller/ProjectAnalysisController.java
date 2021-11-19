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
import java.util.ArrayList;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mo.analysis.AnalysisProvider;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.core.v2.model.Organization;
import mo.core.v2.model.StagePluginV2;
import mo.organization.StageAction;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import mo.visualization.VisualizationProvider;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectAnalysisController implements Initializable {
    private final String CONTROLLER_KEY = "controller";

    @FXML
    private ScrollPane scrollPaneAnalysis;
    @FXML
    private GridPane gridPaneAnalysis;
    @FXML
    private ImageView iconAnalysis;
    @FXML
    private Text textAnalysis;
    @FXML
    private Circle circleAdd;
    @FXML
    private ImageView imageAdd;
    @FXML
    private ImageView deleteButton;
    private int row;
    private String pluginType = "mo.analysis.AnalysisProvider";
    @Inject
    public Injector injector;
    @Inject
    Organization model;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    List<StagePluginV2> analysis = new ArrayList<StagePluginV2>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        addObservablePlugin();
        initAnalysisStage();
    }    

    @FXML
    private void addClick(MouseEvent event) {
        try{
            model.setType(2);
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(MainWindowsController.class
                .getResource("/fxml/core/ui/AddPlugin.fxml"), null,
                builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle("Add Analysis");
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void removeConfiguration(MouseEvent event) {
    }
 
    private void init(){
        if(model.getAnalysis().isEmpty()){
            iconAnalysis.opacityProperty().set(0.50);
            textAnalysis.opacityProperty().set(0.50);
            row=0;
        }
    }
    
    public ObservableList<String> addObservablePlugin(){
        if(ObservablePlugins.isEmpty()){
            ObservablePlugins.add(model.getOrg().getStages().get(0).getName());
        }
        return ObservablePlugins;
    }
    
    public void initAnalysisStage(){
        ObservablePlugins.clear();
        /*List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            if(nodeProvider.getName().equals(model.getOrg().getStages().get(0).getName())){
                //add stage
                model.setStageModule(nodeProvider); 
                List<StageAction> actions = nodeProvider.getActions();
                for (StageAction action : actions){
                    ObservablePlugins.add(action.getName());
                }
                break;
            }
        }*/
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor(pluginType)){
            for(StagePluginV2 sp: model.getCaptureStage().getPlugins()){
                AnalysisProvider c = (AnalysisProvider) plugin.getNewInstance();
                if(c != null){
                    if(ObservablePlugins.isEmpty()){
                        ObservablePlugins.add(c.getName());
                        analysis.add((StagePluginV2) c);
                    }
                    if(!ObservablePlugins.get(ObservablePlugins.size()-1).equals(c.getName())){
                        ObservablePlugins.add(c.getName());
                        analysis.add((StagePluginV2) c);
                    }
                    
                    System.out.println("Plugin: " + c.getName());
                }
            }
        }
        model.setObservablePlugins(ObservablePlugins);
        model.setPlugins(analysis);
    }
}
