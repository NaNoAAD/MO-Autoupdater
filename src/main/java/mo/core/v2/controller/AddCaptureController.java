/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class AddCaptureController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    
    @FXML
    private ComboBox<String> comboCaptures;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @Inject
    public Injector injector;
    @Inject
    Organization model;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("It's wooooork");
        initComboBox();
    }

    public void initComboBox(){
        System.out.println("Observable " + model.getObservablePlugins());
        comboCaptures.setItems(model.getObservablePlugins());
        System.out.println("Done");
    }
    
}
