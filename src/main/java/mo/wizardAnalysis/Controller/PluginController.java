/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.Controller;

import bibliothek.util.xml.XAttribute;
import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import static mo.core.Utils.getBaseFolder;
import mo.core.plugin.PluginRegistry;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import mo.wizardAnalysis.GroupProvider;
import mo.wizardAnalysis.model.Controller;
import mo.wizardAnalysis.model.OrgWizardAnalysis;
import static mo.wizardAnalysis.utils.TypeId.INDIVIDUAL;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class PluginController implements Initializable, Controller {

  @FXML
  private ListView<StagePlugin> pluginList;
  @FXML
  private ListView<Configuration> configList;
  @FXML
  private JFXButton btnNewPlugin;
  @FXML
  private JFXButton btnNewConfig;
  @FXML
  private Label pluginSelected;
  @FXML
  private Label configSelected;

  List<StagePlugin> plugins = new ArrayList<StagePlugin>();
  @FXML
  private Label warning;

  @Inject
  OrgWizardAnalysis model;
  @Inject
  Injector injector;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    pluginList.setCellFactory(new PluginCellFactory());
    pluginList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StagePlugin>() {
      @Override
      public void changed(ObservableValue<? extends StagePlugin> observableValue, StagePlugin oldValue,
          StagePlugin newValue) {
        setPluginSelected(newValue);
        chargedConfig();
      }
    });
    configList.setCellFactory(new ConfigurationCellFactory());
    configList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Configuration>() {
      @Override
      public void changed(ObservableValue<? extends Configuration> observableValue, Configuration oldValue,
          Configuration newValue) {
        setConfigSelected(newValue);
      }
    });
  }

  @FXML
  void newPlugin(ActionEvent event) {
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(getBaseFolder()));
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PluginJar", "*.jar"),
        new ExtensionFilter("PluginClass", "*.class"));
    List<File> files = fileChooser.showOpenMultipleDialog(null);

    if (confirmPluginAdd(files)) {
      int success = 0;
      int fails = 0;

      for (File f : files) {
        try {
          PluginRegistry.getInstance().copyPluginToFolder(f);
          success++;
        } catch (IOException e) {
          fails++;
          e.printStackTrace();
        }
      }
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Results");
      alert.setHeaderText(null);
      alert.setContentText(success + " plugins added/updated, " + fails + " errors.");
      alert.showAndWait();
    }
  }

  @FXML
  void newConfig(ActionEvent event) { //This
    StagePlugin plugin = pluginList.getSelectionModel().getSelectedItem();
    if (plugin == null) {
      return;
    }
    int index = pluginList.getSelectionModel().getSelectedIndex();
    File projectFolder = new File(model.getFileProject().toString()); // cambiar por url de poryecto
    ProjectOrganization organization = new ProjectOrganization(projectFolder.getAbsolutePath());
    Configuration c = plugin.initNewConfiguration(organization);
    if (c != null) {
      chargedConfig();
      try {
        FileInputStream fileInputStream = new FileInputStream(model.getFileProject().toString() + "/analysis.xml");
        XElement analysis = XIO.readUTF(fileInputStream);
        fileInputStream.close();

        XElement exist = null;
        for (XElement element : analysis.getElements("plugin")) {
          if (element.getAttribute("class").getString().equals(plugin.getClass().getName())) {
            exist = element;
          }
        }
        if (exist == null) {
          XElement xElement=new XElement("plugin");
          XAttribute clazz = new XAttribute("class");
          clazz.setString(plugin.getClass().getName());
          xElement.addAttribute(clazz);
          File f = plugin.toFile(new File(model.getFileProject(), "analysis"));
          Path filePath = projectFolder.toPath();
          Path selfPath = f.toPath();
          Path relative = filePath.relativize(selfPath);
          XElement path = new XElement("path");
          path.setString(relative.toString());
          xElement.addElement(path);
          analysis.addElement(xElement);
          FileOutputStream FileOutputStream = new FileOutputStream(model.getFileProject().toString() + "/analysis.xml");
          XIO.writeUTF(analysis, FileOutputStream);
          FileOutputStream.close();
          organization.restore();
        }
        //path.addElement()
      } catch (FileNotFoundException ex) {
        Logger.getLogger(PluginController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(PluginController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception ex) {
        Logger.getLogger(PluginController.class.getName()).log(Level.SEVERE, null, ex);
      }
      plugin.toFile(new File(projectFolder.getAbsoluteFile() + "/analysis"));
    }
  }

  void initPluginList() {
    /*pluginList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StagePlugin>() {
      @Override
      public void changed(ObservableValue<? extends StagePlugin> observableValue, StagePlugin oldValue,
          StagePlugin newValue) {
        setPluginSelected(newValue);
        chargedConfig();
      }
    });*/
    ObservableList<StagePlugin> nuevaListView = FXCollections.<StagePlugin>observableArrayList();
    for (StagePlugin plugin : model.getPlugins()) {
      if (model.getType() == INDIVIDUAL) {
        nuevaListView.add(plugin);
      } else {
        if (plugin instanceof GroupProvider) {
          nuevaListView.add(plugin);
        }
      }
    }
    pluginList.getItems().setAll(nuevaListView);

  }

  void initConfigList() {
    /*configList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Configuration>() {
      @Override
      public void changed(ObservableValue<? extends Configuration> observableValue, Configuration oldValue,
          Configuration newValue) {
        setConfigSelected(newValue);
      }
    });*/
  }

  private void initPluginByModel() {
    plugins = new ArrayList<StagePlugin>();
    for (StagePlugin plugin : model.getPlugins()) {
      plugins.add(plugin);
    }
  }

  private boolean confirmPluginAdd(List<File> files) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.setContentText("Add " + files.size() + " plugins?");
    Optional<ButtonType> result = alert.showAndWait();
    return (boolean) (ButtonType.OK == result.get());
  }

  public void setPluginSelected(StagePlugin pluginSelected) {
    if (null == pluginSelected) {
      this.pluginSelected.setText("<Plugin>");
    } else {
      this.pluginSelected.setText(pluginSelected.getName());
    }
    model.setPluginSelected(pluginSelected);
  }

  public void setConfigSelected(Configuration configSelected) {
    if (null == configSelected) {
      this.configSelected.setText("<Configuracion>");
    } else {
      this.configSelected.setText(configSelected.getId());
      warning.setVisible(false);
    }
    model.setConfigurationSelected(configSelected);
  }

  void chargedConfig() {
    //int index = pluginList.getSelectionModel().getSelectedIndex();
    ObservableList<Configuration> nuevaListView = FXCollections.<Configuration>observableArrayList();
    for (Configuration configuration : model.getPluginSelected().getConfigurations()) {
      nuevaListView.add(configuration);
    }
    configList.setItems(nuevaListView);
  }

  @Override
  public boolean validateNext() {
    if (pluginSelected.getText().equals("<Plugin>") || configSelected.getText().equals("<Configuracion>")) {
      warning.setText("Debe selecionar una configuracion");
      warning.setVisible(true);
      return false;
    }
    warning.setVisible(false);
    return true;

  }

  @Override
  public OrgWizardAnalysis getModel() {
    return this.model;
  }

  @Override
  public void initLanguage() {

  }

  @Override
  public void chargeModel() {
    if (plugins.size() == 0) {
      initPluginList();
      initPluginByModel();
    }
    if (model.getRestoreBoolean()) {
      restore();
    }

  }

  public void restore() {

    StagePlugin plugin = model.getPluginSelected();
    Configuration configuration = model.getConfigurationSelected();
    int i = 0;
    while (i < pluginList.getItems().size()) {
      if (pluginList.getItems().get(i).getName().equals(plugin.getName())) {
        break;
      }
      i++;
    }
    if (i < model.getPlugins().size()) {
      pluginList.getSelectionModel().select(plugin);
      configList.getSelectionModel().select(configuration);
    }

  }

  private class PluginCellFactory implements Callback<ListView<StagePlugin>, ListCell<StagePlugin>> {

    @Override
    public ListCell<StagePlugin> call(ListView<StagePlugin> listview) {
      return new PluginCell();
    }
  }

  private class PluginCell extends ListCell<StagePlugin> {

    @Override
    public void updateItem(StagePlugin item, boolean empty) {
      super.updateItem(item, empty);
      if (!(item == null || empty)) {
        this.setText(item.getName());
      } else {
        this.setText(null);
      }
      setGraphic(null);
    }
  }

  private class ConfigurationCellFactory implements Callback<ListView<Configuration>, ListCell<Configuration>> {

    @Override
    public ListCell<Configuration> call(ListView<Configuration> listview) {
      return new ConfigurationCell();
    }
  }

  private class ConfigurationCell extends ListCell<Configuration> {

    @Override
    public void updateItem(Configuration item, boolean empty) {
      super.updateItem(item, empty);
      if (!(item == null || empty)) {
        this.setText(item.getId());
      } else {
        this.setText(null);
      }
      setGraphic(null);
    }
  }
}
