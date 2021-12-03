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
import mo.capture.CaptureProvider;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.core.v2.model.Activity;
import mo.core.v2.model.ConfigurationV2;
import mo.core.v2.model.Organization;
import mo.core.v2.model.StagePluginV2;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StageAction;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import mo.visualization.VisualizationProvider;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectVisualizationController implements Initializable {
    private final String CONTROLLER_KEY = "controller";    
    
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
    private String pluginType = "mo.visualization.VisualizationProvider";
    @Inject
    public Injector injector;
    @Inject
    Organization model;
    private int row;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    List<VisualizationProvider> visualization = new ArrayList<VisualizationProvider>();
    List<String> visualizationAux = new ArrayList<String>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        //addObservablePlugin();
        initVisualizationStage();
        row=0;
    }    

    @FXML
    private void addClick(MouseEvent event) {
        try{
            model.setType(3);
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
            addConfiguration();
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void removeConfiguration(MouseEvent event) {
        System.out.println("Visualization remove");
    }
    
    private void init(){
        if(visualizationAux.isEmpty()){
            iconVisualization.opacityProperty().set(0.50);
            textVisualization.opacityProperty().set(0.50);
            deleteButton.opacityProperty().set(0.50);
            row=0;
        }
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            System.out.println("VIZU: " + model.getVisualizationStage().getName());
            System.out.println("NODE: " + nodeProvider.getName());
            if(nodeProvider.getName().equals(model.getVisualizationStage().getName())){
                model.setMOCaptureStage(nodeProvider);
                break;
            }
        }
        System.out.println("***************************************************************");
        System.out.println(model.getMOCaptureStage().getActions());
        System.out.println("***************************************************************");
    }
    
    /*public ObservableList<String> addObservablePlugin(){
        if(ObservablePlugins.isEmpty()){
            ObservablePlugins.add(model.getOrg().getStages().get(2).getName());
        }
        return ObservablePlugins;
    }*/
    
    private void initVisualizationStage(){
        ObservablePlugins.clear();
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor(pluginType)){
            for(StagePluginV2 sp: model.getCaptureStage().getPlugins()){
                VisualizationProvider v = (VisualizationProvider) plugin.getNewInstance();
                if(v != null){
                    if(ObservablePlugins.isEmpty()){
                        ObservablePlugins.add(v.getName());
                        visualization.add(v);
                    }
                    if(!ObservablePlugins.get(ObservablePlugins.size()-1).equals(v.getName())){
                        ObservablePlugins.add(v.getName());
                        visualization.add(v);
                    }
                    //System.out.println("Plugin: " + v.getName());
                }
            }
        }
        model.setObservablePlugins(ObservablePlugins);
        model.setVisualization(visualization);
    }
    
    public void addPlugin(String name){
        if(row>gridPaneAnalysis.getRowConstraints().size()){
            gridPaneAnalysis.addRow(row, null);
        }
        if(visualizationAux.size()<1){
            iconVisualization.opacityProperty().set(1);
            textVisualization.opacityProperty().set(1);
            deleteButton.opacityProperty().set(1);
            textVisualization.setText(name);
            row++;
            visualizationAux.add(name);
        }
        else{
            javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView();
            icon2.setImage(iconVisualization.getImage());
            icon2.setFitHeight(20);
            icon2.setFitWidth(20);
            icon2.setTranslateX(25);
            gridPaneAnalysis.add(icon2, 0, row);
            Text text2 = new Text(name);
            text2.setTranslateX(50);
            gridPaneAnalysis.add(text2, 0, row);
            javafx.scene.image.ImageView delete2 = new javafx.scene.image.ImageView();
            delete2.setImage(deleteButton.getImage());
            delete2.setFitHeight(20);
            delete2.setFitWidth(20);
            delete2.setTranslateX(182);
            gridPaneAnalysis.add(text2, 0, row);
            row++;
            visualizationAux.add(name);
        }
    }
    
    private void addConfiguration(){
        System.out.println("1");
        String SelectedPlugin = model.getPluginSelected().getName();
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor(pluginType)){
            System.out.println("2");
            for(StagePlugin sp : model.getMOCaptureStage().getPlugins()){
                System.out.println("3");
                VisualizationProvider v = (VisualizationProvider) plugin.getNewInstance();
                if(v != null){
                    System.out.println("4");
                    if(v.getName().equals(SelectedPlugin) && SelectedPlugin.equals(sp.getName())){
                        System.out.println("5");
                        Configuration config = v.initNewConfiguration(model.getOrg());
                        if(config != null){
                            System.out.println("6");
                            model.getConfigurations().add(config);
                            model.getOrg().store();
                            saveConfiguration(SelectedPlugin, config.getId());
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("7");
    }
    
    public void saveConfiguration(String SelectedPlugin, String configId){
        System.out.println(model.getVisualizationStage().getName());
        System.out.println(model.getVisualizationStage().getPlugins());
        for(StagePluginV2 spv : model.getVisualizationStage().getPlugins()){
            System.out.println(spv.getName());
            System.out.println(SelectedPlugin);
            if(spv.getName().equals(SelectedPlugin)){
                ConfigurationV2 config = new ConfigurationV2(configId);
                spv.addConfiguration(config);
                addPlugin(config.getName()+" ("+spv.getName()+")");
                break;
            }
        }
    }
}
