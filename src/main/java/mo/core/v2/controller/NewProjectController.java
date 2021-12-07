/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import mo.core.MultimodalObserver;
import static mo.core.Utils.getBaseFolder;
import mo.core.filemanagement.FileRegistry;
import mo.core.filemanagement.project.Project;
import mo.core.preferences.AppPreferencesWrapper;
import mo.core.preferences.PreferencesManager;
import mo.core.v2.model.Organization;
import mo.organization.ProjectOrganization;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class NewProjectController implements Initializable {

    @FXML
    private GridPane NewProjectNameGridPane;
    @FXML
    private Text newProjectLabel;
    @FXML
    private TextField nameText;
    @FXML
    private TextField locationLabel;
    @FXML
    private Text projectNameLabel;
    @FXML
    private Text projectLocationLabel;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Text alertLabel;
    @FXML
    private Button searchButton;
    @Inject
    public Injector injector;
    @Inject 
    Organization model;
    private boolean exist;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        init();
    }    

    @FXML
    private void writeName(KeyEvent event) {
        File proj = new File(nameText.getText());
        exist = proj.exists();
        if(exist){
            alertLabel.setText("A project with this name already exists");
            createButton.disableProperty().set(exist);
        }
        else{
            alertLabel.setText("");
            createButton.disableProperty().set(exist);
        }
    }

    @FXML
    private void createProject(MouseEvent event) {
        String path = locationLabel.getText();
        String name = nameText.getText();
        File proj = new File(path+"/"+name);
        if(proj.mkdir()){
            Project p = new Project(path+"/"+name);
            saveProjectInAppPreferences(p);
            FileRegistry.getInstance().addOpenedProject(p);
            ProjectOrganization org = new ProjectOrganization(name);
            model.setOrg(org);
            model.setFileProject(proj);
            model.getOrg().store();
            cancel(event);
        }
    }

    @FXML
    private void cancel(MouseEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void DirectoryChooser(MouseEvent event) {
        final DirectoryChooser dirchooser = new DirectoryChooser();
        File file = dirchooser.showDialog(null);
        if(file!=null){
            locationLabel.setText(file.getAbsolutePath());
        }
    }

    private void init(){
        locationLabel.setText(getBaseFolder());
        nameText.setText("newproject");
        File proj = new File(locationLabel.getText()+nameText.getText());
        exist = proj.exists();
        if(exist){
            alertLabel.setText("A project with this name already exists");
            createButton.disableProperty().set(exist);
        }
    }
    
    private void saveProjectInAppPreferences(Project project){
        PreferencesManager pm = new PreferencesManager();
        AppPreferencesWrapper app = (AppPreferencesWrapper) pm.loadOrCreate(AppPreferencesWrapper.class,new File(MultimodalObserver.APP_PREFERENCES_FILE));
        app.addOpenedProject(project.getFolder().getAbsolutePath());
        pm.save(app, new File(MultimodalObserver.APP_PREFERENCES_FILE));
    } 
}
