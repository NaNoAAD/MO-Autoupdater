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
import javafx.util.Pair;
import mo.analysis.AnalysisProvider;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.core.v2.model.ConfigurationV2;
import mo.core.v2.model.Organization;
import mo.core.v2.model.StagePluginV2;
import mo.organization.Configuration;
import mo.organization.StageModule;
import mo.organization.StagePlugin;

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
        //preInit(plugin);
        init();
        initAnalysisStage();
        //addObservablePlugin();
        
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
            addConfiguration();
            
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void removeConfiguration(MouseEvent event) {
        System.out.println("Analysis remove");
    }
 
    private void init(){
        if(model.getAnalysis().isEmpty()){
            iconAnalysis.opacityProperty().set(0.50);
            textAnalysis.opacityProperty().set(0.50);
            row=0;
        }
        else{
            for(Pair<String,String> p : model.getConfigAnalysis()){
                addPlugin(p.getKey()+"("+p.getValue()+")");
            }
        }
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            if(nodeProvider.getName().equals(model.getAnalysisStage().getName())){
                model.setMOCaptureStage(nodeProvider);
                break;
            }
        }
        //System.out.println(model.getMOCaptureStage().getPlugins());
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
            for(StagePluginV2 sp : model.getAnalysisStage().getPlugins()){
                AnalysisProvider a = (AnalysisProvider) plugin.getNewInstance();
                if(a != null){
                    if(ObservablePlugins.isEmpty()){
                        ObservablePlugins.add(a.getName());
                        analysis.add(a);
                    }
                    if(!ObservablePlugins.get(ObservablePlugins.size()-1).equals(a.getName())){
                        ObservablePlugins.add(a.getName());
                        analysis.add(a);
                    }
                }
            }
        }
        model.setObservablePlugins(ObservablePlugins);
        model.setAnalysis(analysis);
    }
    
    private void addPlugin(String name){
        if(row>gridPaneAnalysis.getRowConstraints().size()){
            gridPaneAnalysis.addRow(row, null);
        }
        if(analysisAux.size()<1){
            iconAnalysis.opacityProperty().set(1);
            textAnalysis.opacityProperty().set(1);
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
            gridPaneAnalysis.add(text2, 0, row);
            row++;
            analysisAux.add(name);
        }
    }
    
    private void addConfiguration(){
	String SelectedPlugin = model.getPluginSelected().getName();
	for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor(pluginType)){
            for(StagePlugin sp : model.getMOCaptureStage().getPlugins()){
                AnalysisProvider a = (AnalysisProvider) plugin.getNewInstance();
                if(a != null){
                    if(a.getName().equals(SelectedPlugin)&&SelectedPlugin.equals(sp.getName())){
                        Configuration config = a.initNewConfiguration(model.getOrg());
                        if(config != null){
                            model.getConfigurations().add(config);
                            sp.getConfigurations().add(config);
                            model.getOrg().store();
                            saveConfiguration(SelectedPlugin, config.getId());
                            break;
                        }
                    }
                }
            }
	}
    }
    
    public void saveConfiguration(String SelectedPlugin, String configId){
	for(StagePluginV2 spv : model.getAnalysisStage().getPlugins()){
            if(spv.getName().equals(SelectedPlugin)){
                ConfigurationV2 config = new ConfigurationV2(configId);
                spv.addConfiguration(config);
                addPlugin(config.getName()+" ("+spv.getName()+")");
            }            
	}
    }

}