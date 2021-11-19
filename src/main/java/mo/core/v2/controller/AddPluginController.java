/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mo.analysis.AnalysisProvider;
import mo.capture.CaptureProvider;
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
public class AddPluginController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    
    @FXML
    private ComboBox<String> comboCaptures;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    private int type;
    private List<CaptureProvider> auxC;
    private List<AnalysisProvider> auxA;
    private List<VisualizationProvider> auxV;
    @Inject
    public Injector injector;
    @Inject
    Organization model;
    @FXML
    private TextField nameConfiguration;
    @FXML
    private Text alertText;
    private List<String> config;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
        initComboBox();
    }

    private void init(){
        //nameConfiguration.visibleProperty().set(false);
        type = model.getType();
        //parse(model.getPlugins());
        auxC = model.getCaptures();
        auxA= model.getAnalysis();
        auxV = model.getVisualization();
        config = new ArrayList<String>();
        /*while(nameConfiguration.getText().isEmpty()){
            alertText.visibleProperty().set(false);
        }*/
    }
    
    public void initComboBox(){
        System.out.println("Observable " + model.getObservablePlugins());
        comboCaptures.setItems(model.getObservablePlugins());
        System.out.println("Done");
    }

    @FXML
    private void addOption(MouseEvent event) { 
        System.out.println("Before: "+model.getObservablePlugins());
        if(type==1){
            //model.getCaptures().add(PluginRegistry.getInstance().getPluginData().getPluginsFor(comboCaptures.getSelectionModel().getSelectedItem())));
            model.getCaptures().add(addSelectedC(auxC,comboCaptures.getSelectionModel().getSelectedItem()));
            System.out.println("Captures added: "+model.getCaptures());
            config.add(nameConfiguration.getText());
            model.setPluginSelected(addSelectedC(auxC,comboCaptures.getSelectionModel().getSelectedItem()));
            
        }
        else if(type==2){
            model.getAnalysis().add(addSelectedA(auxA,comboCaptures.getSelectionModel().getSelectedItem()));
            System.out.println("Analysis added: "+model.getAnalysis());
            config.add(nameConfiguration.getText());
            model.setPluginSelected(addSelectedA(auxA,comboCaptures.getSelectionModel().getSelectedItem()));
        }
        else if(type==3){
            model.getVisualization().add(addSelectedV(auxV, comboCaptures.getSelectionModel().getSelectedItem()));
            System.out.println("Visualization added: "+model.getVisualization());
            config.add(nameConfiguration.getText());
            model.setPluginSelected(addSelectedV(auxV,comboCaptures.getSelectionModel().getSelectedItem()));
        }
        String aux = comboCaptures.getSelectionModel().getSelectedItem();
        model.getObservablePlugins().remove(aux);
        model.setConfig(nameConfiguration.getText());
        System.out.println("Agregado");
        System.out.println("After: "+model.getObservablePlugins());
        close(event);
    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void parse(List<StagePluginV2> plugins){
        if(type==1){
            auxC = new ArrayList<CaptureProvider>();
            plugins.forEach((plugin) -> {
                auxC.add((CaptureProvider)plugin);
            });   
        }
        else if(type==2){
            auxA = new ArrayList<AnalysisProvider>();
            plugins.forEach((plugin) -> {
                auxA.add((AnalysisProvider)plugin);
            });
        }
        else if(type==3){
            auxV = new ArrayList<VisualizationProvider>();
            plugins.forEach((plugin) -> {
                auxV.add((VisualizationProvider)plugin);
            });
        }        
    }
    
    private CaptureProvider addSelectedC(List<CaptureProvider> aux, String selected){
        System.out.println("0: "+aux);
        for(CaptureProvider capture : aux){
            if(capture.getName().equals(selected)){
                return capture;
            }
        }
        return null;  
    }
    
    private AnalysisProvider addSelectedA(List<AnalysisProvider> aux, String selected){
        for(AnalysisProvider analysis : aux){
            if(analysis.getName().equals(selected)){
                return analysis;
            }
        }
        return null;  
    }
    
    private VisualizationProvider addSelectedV(List<VisualizationProvider> aux, String selected){
        for(VisualizationProvider visualization : aux){
            if(visualization.getName().equals(selected)){
                return visualization;
            }
        }
        return null;  
    }

    @FXML
    private void check(MouseEvent event) {
        if(nameConfiguration.getText().isEmpty()){
            addButton.disableProperty().setValue(Boolean.TRUE);
        }
    }

    private void check(ActionEvent event) {
        if(nameConfiguration.getText().isEmpty()){
            addButton.disableProperty().setValue(Boolean.TRUE);
        }
    }

    @FXML
    private void allGood(KeyEvent event) {
        addButton.disableProperty().setValue(Boolean.FALSE);
    }

}
