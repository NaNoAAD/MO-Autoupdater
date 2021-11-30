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
import mo.core.v2.model.ConfigurationV2;
import mo.core.v2.model.Organization;
import mo.core.v2.model.StagePluginV2;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StageModule;
import mo.organization.StagePlugin;

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
    List<CaptureProvider> captures = new ArrayList<CaptureProvider>();
    List<String> capturesAux = new ArrayList<String>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        init();
        //addObservablePlugin();
        initCaptureStage();
        row=0;
    }    

    @FXML
    private void addClick(MouseEvent event) {
        try{
            model.setType(1);
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
            popUp.setTitle("Add Capture");
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
            addConfiguration();
            
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void init(){
        if(model.getCaptures().isEmpty()){
            iconCapture.opacityProperty().set(0.50);
            textCapture.opacityProperty().set(0.50);
            deleteButton.opacityProperty().set(0.50);
            row=0;
        } 
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            if(nodeProvider.getName().equals(model.getCaptureStage().gatName())){
                model.setMOCaptureStage(nodeProvider);
                break;
            }
        }
    }
    
    /*public ObservableList<String> addObservablePlugin(){
        //System.out.println("Observable 1: "+model.getOrg().getStages().get(1).getName());
        if(ObservablePlugins.isEmpty()){
            ObservablePlugins.add(model.getOrg().getStages().get(1).getName());
        }
        return ObservablePlugins;
    }*/
    
    public void initCaptureStage(){
        ObservablePlugins.clear();
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.capture.CaptureProvider")){
            for(StagePluginV2 sp: model.getCaptureStage().getPlugins()){
                CaptureProvider c = (CaptureProvider) plugin.getNewInstance();
                if(c != null){
                    if(ObservablePlugins.isEmpty()){
                        ObservablePlugins.add(c.getName());
                        captures.add(c);
                    }
                    if(!ObservablePlugins.get(ObservablePlugins.size()-1).equals(c.getName())){
                        ObservablePlugins.add(c.getName());
                        captures.add(c);
                    }
                }
            }
        }
        model.setObservablePlugins(ObservablePlugins);
        model.setCaptures(captures);
    }
    

    @FXML
    private void removeConfiguration(MouseEvent event) {
    }
    
    private void addPlugin(String name){
        System.out.println("GridPane: "+gridPaneCapture.getRowConstraints().size());
        System.out.println(name);
        if(row>gridPaneCapture.getRowConstraints().size()){
            gridPaneCapture.addRow(row, null);
        }
        System.out.println("1");
        System.out.println(model.getCaptures().size());
        System.out.println(model.getCaptures());
        if(capturesAux.size()<1){
            iconCapture.opacityProperty().set(1);
            textCapture.opacityProperty().set(1);
            deleteButton.opacityProperty().set(1);
            textCapture.setText(name);
            row++;
            capturesAux.add(name);
        }
        else{
            javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView();
            icon2.setImage(iconCapture.getImage());
            //icon2.setId(iconCapture.getId()+String.valueOf(id));
            System.out.println("ID: "+icon2.idProperty());
            icon2.setFitHeight(20);
            icon2.setFitWidth(20);
            icon2.getStyleClass().add("~/Ejemplo.css");
            icon2.setStyle(".icon");
            System.out.println("XXXX "+iconCapture.getTranslateX());
            icon2.setTranslateX(25);
            gridPaneCapture.add(icon2, 0, row);
            Text text2 = new Text(name);
            text2.setTranslateX(50);
            //text2.setStyle(textCapture.getStyle());
            gridPaneCapture.add(text2, 0, row);
            javafx.scene.image.ImageView delete2 = new javafx.scene.image.ImageView();
            delete2.setImage(deleteButton.getImage());
            delete2.setFitHeight(20);
            delete2.setFitWidth(20);
            delete2.setTranslateX(182);
            gridPaneCapture.add(delete2, 0, row);
            row++;
            capturesAux.add(name);
        }
        
    }
    
    private void addConfiguration(){
        ProjectOrganization PO = new ProjectOrganization("");
        String SelectedPlugin = model.getPluginSelected().getName();
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.capture.CaptureProvider")){
            for(StagePlugin sp: model.getMOCaptureStage().getPlugins()){
                CaptureProvider c = (CaptureProvider) plugin.getNewInstance();
                if(c != null){
                    if(c.getName().equals(SelectedPlugin)&&SelectedPlugin.equals(sp.getName())){
                        Configuration config = c.initNewConfiguration(PO);
                        if(config != null){
                            saveConfiguration(SelectedPlugin, config.getId());
                            model.getConfigurations().add(config);
                            System.out.println(config);
                            //addPlugin(config.getId());
                            sp.getConfigurations().add(config);
                            System.out.println(config.getId());
                            System.out.println(sp.getName());
                            break;
                        }
                    }
                }
               
            }
        }
        
    }
    
    public void saveConfiguration(String SelectedPlugin, String configId){
        for(StagePluginV2 spv: model.getCaptureStage().getPlugins()){
            if(spv.getName().equals(SelectedPlugin)){
                ConfigurationV2 config = new ConfigurationV2(configId);
                spv.addConfiguration(config);
                addPlugin(config.getName()+" ("+spv.getName()+")");
            }
        }
    }
}