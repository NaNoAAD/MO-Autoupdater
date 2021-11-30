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
    @Inject
    public Injector injector;
    @Inject
    public Organization model;
    private int row;
    private String pluginType = "mo.analysis.AnalysisProvider";
    private ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    private List<AnalysisProvider> analysis = new ArrayList<AnalysisProvider>();
    private List<String> analysisAux = new ArrayList<String>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        //addObservablePlugin();
        initAnalysisStage();
        row=0;
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
            System.out.println("addClick");
            addConfiguration();
            System.out.println("1");
            
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void removeConfiguration(MouseEvent event) {
        System.out.println("Analysis remove");
    }
 
    private void init(){
        if(model.getAnalysis().isEmpty()){
            iconAnalysis.opacityProperty().set(0.50);
            textAnalysis.opacityProperty().set(0.50);
            deleteButton.opacityProperty().set(0.50);
            row=0;
        }
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            if(nodeProvider.getName().equals(model.getCaptureStage().gatName())){
                model.setMOCaptureStage(nodeProvider);
                System.out.println("Init");
                break;
            }
        }
        System.out.println("init");
        System.out.println(model.getMOCaptureStage().getPlugins());
    }
    
    /*public ObservableList<String> addObservablePlugin(){
        if(ObservablePlugins.isEmpty()){
            ObservablePlugins.add(model.getOrg().getStages().get(0).getName());
        }
        return ObservablePlugins;
    }*/
    
    public void initAnalysisStage(){
        ObservablePlugins.clear();
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor(pluginType)){
            for(StagePluginV2 sp: model.getCaptureStage().getPlugins()){
                AnalysisProvider c = (AnalysisProvider) plugin.getNewInstance();
                if(c != null){
                    if(ObservablePlugins.isEmpty()){
                        ObservablePlugins.add(c.getName());
                        analysis.add(c);
                    }
                    if(!ObservablePlugins.get(ObservablePlugins.size()-1).equals(c.getName())){
                        ObservablePlugins.add(c.getName());
                        analysis.add(c);
                    }
                    
                    System.out.println("Plugin: " + c.getName());
                }
            }
        }
        model.setObservablePlugins(ObservablePlugins);
        model.setAnalysis(analysis);
        System.out.println("InitAnalysis");
    }
    
    private void addPlugin(String name){
        if(row>gridPaneAnalysis.getRowConstraints().size()){
            gridPaneAnalysis.addRow(row, null);
        }
        if(analysisAux.size()<1){
            iconAnalysis.opacityProperty().set(1);
            textAnalysis.opacityProperty().set(1);
            deleteButton.opacityProperty().set(1);
            textAnalysis.setText(name);
            row++;
            analysisAux.add(name);
        }
        else{
            javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView();
            icon2.setImage(iconAnalysis.getImage());
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
            delete2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                removeConfiguration(event);
            });
            gridPaneAnalysis.add(text2, 0, row);
            row++;
            analysisAux.add(name);
        }
        System.out.println("AddVisu");
    }
    
    private void addConfiguration(){
        ProjectOrganization PO = new ProjectOrganization("");
        String SelectedPlugin = model.getPluginSelected().getName();
        System.out.println("addConfig");
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor(pluginType)){
            System.out.println("1");
            //CAMBIAR STAGEPLUGIN POR STAGEACTION, VER LO DE POLANCO
            for(StagePlugin sp : model.getMOCaptureStage().getPlugins()){
                System.out.println("2");
                AnalysisProvider a = (AnalysisProvider) plugin.getNewInstance();
                System.out.println("3");
                if(a != null){
                    System.out.println("4");
                    System.out.println(a.getName());
                    System.out.println(SelectedPlugin);
                    System.out.println(sp.getName());
                    if(a.getName().equals(SelectedPlugin)&&SelectedPlugin.equals(sp.getName())){
                        System.out.println("5");
                        Configuration config = a.initNewConfiguration(PO);
                        if(config != null){
                            System.out.println("6");
                            saveConfiguration(SelectedPlugin, config.getId());
                            System.out.println("7");
                        }
                    }
                }
            }
        }
    }
    
    public void saveConfiguration(String SelectedPlugin, String configId){
        for(StagePluginV2 spv : model.getCaptureStage().getPlugins()){
            if(spv.getName().equals(SelectedPlugin)){
                ConfigurationV2 config = new ConfigurationV2(configId);
                spv.addConfiguration(config);
                addPlugin(config.getName()+" ("+spv.getName()+")");
                System.out.println("SaveConfig");
            }            
        }
    }
}
