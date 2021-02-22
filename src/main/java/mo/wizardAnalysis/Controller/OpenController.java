/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.Controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import static mo.core.Utils.getBaseFolder;
import static mo.wizardAnalysis.WizardManagement.frame;
import mo.wizardAnalysis.model.Controller;
import mo.wizardAnalysis.model.OrgWizardAnalysis;
import static mo.wizardAnalysis.utils.Persistence.LoadConfigure;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class OpenController implements Controller, Initializable {

  @FXML
  private TextField tfFolderProject;
  @FXML
  private Label labelPath;
  @FXML
  private Button searchButton;
  @FXML
  private Label labelName;
  @FXML
  private TextField tfNameProject;
  @FXML
  private Label labelTitle;
  @FXML
  private Label warning;

  File FileProyect;
  @Inject
  OrgWizardAnalysis model;
  @Inject
  Injector injector;
  @FXML
  private Button searchButton1;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    tfFolderProject.setText("");
  }

  @FXML
  private void DirectoryChooser(ActionEvent event) {
    warning.setVisible(false);
    final DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setInitialDirectory(new File(getBaseFolder()));
    File file = directoryChooser.showDialog(null);
    if (file != null) {
      File[] proyect = file.listFiles((File dir, String name) -> { // definicion filenamefilter
        return name.equals("moproject.xml");
      });
      if (proyect.length == 0) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No se encontro proyecto MO en esta carpeta");
        alert.showAndWait();
      } else {
        tfFolderProject.setText(file.getAbsolutePath());
        FileProyect = file;
        model.setFileProject(FileProyect);
      }
    }
  }

  @Override
  public boolean validateNext() {
    if (tfFolderProject.getText() == null || tfFolderProject.getText() == "") {
      warning.setText("Complete el campo obligatorio (*)");
      warning.setVisible(true);
      return false;
    }
    File file = new File(tfFolderProject.getText());
    if (!file.exists()) {
      warning.setText("Carpeta no encontrada");
      warning.setVisible(true);
      return false;
    }
    File[] proyect = file.listFiles((File dir, String name) -> { // definicion filenamefilter
      return name.equals("moproject.xml");
    });
    if (proyect.length == 0) {
      warning.setText("No se encontro proyecto MO en esta carpeta");
      warning.setVisible(true);
      return false;
    }
    FileProyect = file;
    //model.setFileProject(FileProyect);
    warning.setVisible(false);
    return true;
  }

  @Override
  public void initLanguage() {

  }

  @Override
  public void chargeModel() {

  }

  @FXML
  private void load(ActionEvent event) {
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(getBaseFolder()+"/proyecto/analysis/groupConfigure"));
    fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("xml", "*.xml"));
    File file = fileChooser.showOpenDialog(null);
    if (file != null) {
      OrgWizardAnalysis org = LoadConfigure(file);
      if (org != null) {
        model.restore(org);
        tfFolderProject.setText(model.getFileProject().getAbsolutePath());
        FileProyect = file;
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Completado");
        alert.setHeaderText(null);
        alert.setContentText("Se cargaron correctamente las configuraciones");
        alert.showAndWait();
      } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No se encontraron las configuraciones en el archivo");
        alert.showAndWait();
      }
    }
    

  }
@Override
  public OrgWizardAnalysis getModel() {
    return this.model;
  }
}
