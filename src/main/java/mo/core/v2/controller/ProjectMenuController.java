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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.core.v2.model.Organization;
import mo.organization.ProjectOrganization;
import mo.organization.StageModule;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectMenuController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    
    public ProjectOrganization projectOrg;
    @FXML
    private BorderPane projectMenuPane;
    @FXML
    private ImageView closeButton;
    @FXML
    private Text nameProjectText;
    @FXML
    private Button participantsButton;
    @FXML
    private Button captureButton;
    @FXML
    private Button analysisButton;
    @FXML
    private Button visualizationButton;
    @FXML
    private AnchorPane centerPane;
    @FXML
    private Pane tutorialPane;
    @Inject
    public Injector injector;
    @Inject 
    Organization model; 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        init();
        //addOneStage("name");
        //addAllStages();
    }    
    
    @FXML
    private void closeButton(MouseEvent event) {
    }

    @FXML
    private void clickParticipants(MouseEvent event) {
        try{
            System.out.println(model.getFileProject());
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderParticipants = new FXMLLoader(MainWindowsController.class
                .getResource("/fxml/core/ui/ProjectParticipants.fxml"), null,
                builderFactory, callback);

            Parent participantParent = loaderParticipants.load();

            participantParent.getProperties()
                .put(CONTROLLER_KEY, loaderParticipants.getController());
            centerPane.getChildren().add(participantParent);
        }
        catch (IOException ex) {
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @FXML
    private void clickCaptures(MouseEvent event) {
        try{
            addOneStage("CaptureStage");
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderCaptures = new FXMLLoader(MainWindowsController.class
                    .getResource("/fxml/core/ui/ProjectCaptures.fxml"), null,
                    builderFactory, callback);
            Parent captureParent = loaderCaptures.load();

            captureParent.getProperties()
                    .put(CONTROLLER_KEY, loaderCaptures.getController());
            centerPane.getChildren().add(captureParent);
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void clickAnalysis(MouseEvent event) {
        try{
            addOneStage("AnalysisStage");
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderAnalysis = new FXMLLoader(MainWindowsController.class
                    .getResource("/fxml/core/ui/ProjectAnalysis.fxml"), null,
                    builderFactory, callback);
            Parent analysisParent = loaderAnalysis.load();
            
            analysisParent.getProperties()
                    .put(CONTROLLER_KEY, loaderAnalysis.getController());
            centerPane.getChildren().add(analysisParent);
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clickVisualizations(MouseEvent event) {
        try{
            addOneStage("VisualizationStage");
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderVisualization = new FXMLLoader(MainWindowsController.class
                    .getResource("/fxml/core/ui/ProjectVisualization.fxml"), null,
                    builderFactory, callback);
            Parent visualizationParent = loaderVisualization.load();
            
            visualizationParent.getProperties()
                    .put(CONTROLLER_KEY, loaderVisualization.getController());
            centerPane.getChildren().add(visualizationParent);
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadPlugins(){
        
    }

    public void init(){
        if(model.newProyect==0){
            tutorialPane.getChildren().clear();
        }
        
        /*List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            addStageNodeIfNotExists(nodeProvider);
        }
        model.setStageModules(model.getOrg().getStages());*/
    }
    
    private void addStageNodeIfNotExists(StageModule stage){
        String newNodeName = stage.getName();
        System.out.println(newNodeName);
        if(model.getOrg().getStages().isEmpty()||model.getOrg().getStages().size()<3){
            stage.setOrganization(model.getOrg());
            model.getOrg().addStage(stage);
            model.getOrg().store();
        }
    }
    
    private void addAllStages(){
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            System.out.println(nodeProvider);
            addStageNodeIfNotExists(nodeProvider);
        }
    }
    private void addOneStage(String name){
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            if(stagePlugin.getName().equals(name)){
                StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
                int aux = model.getOrg().getStages().size();
                if(aux == 0){
                    addStageNodeIfNotExists(nodeProvider);
                }
                else{
                    if(aux == 1){
                        if(!nodeProvider.equals(model.getOrg().getStages().get(0))){
                            addStageNodeIfNotExists(nodeProvider);
                        }
                    }
                    else if(aux == 2){
                        if(!nodeProvider.getName().equals(model.getOrg().getStages().get(0).getName()) && !nodeProvider.getName().equals(model.getOrg().getStages().get(1).getName())){
                            addStageNodeIfNotExists(nodeProvider);
                        }
                    }
                }
            }
        }
    }
}
