/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.Controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import static mo.core.Utils.getBaseFolder;
import mo.organization.Participant;
import mo.wizardAnalysis.model.Controller;
import mo.wizardAnalysis.model.Group;
import mo.wizardAnalysis.model.OrgWizardAnalysis;
import static mo.wizardAnalysis.utils.Persistence.SaveConfigure;
import static mo.wizardAnalysis.utils.TypeId.GROUP;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class ResumeController implements Initializable, Controller {

  @FXML
  private Label warning;
  @FXML
  private Label lblproject;
  @FXML
  private Label lbltype;
  @FXML
  private Label lblplugin;
  @FXML
  private Label lblconfiuracion;
  @FXML
  private TabPane TabPane;
  @FXML
  private Button btnSave;

  @Inject
  OrgWizardAnalysis model;
  @Inject
  Injector injector;
  private final double lbllabelstrongX = 30.0;
  private final double lbllabelstrongY = 15.0;
  private final double lbllabelX = 105.0;
  private final double lbllabelY = 15.0;
  private final double listGroupX = 14.0;
  private final double listGroupY = 43.0;
  private final double listGroupHeight = 173.0;
  private final double listGroupWidth = 550.0;
  private final Font simple = new Font("System", 13);
  private final Font bold = new Font("System Bold", 13);

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  @Override
  public boolean validateNext() {
    return true;
  }

  @Override
  public void initLanguage() {
  }

  @Override
  public OrgWizardAnalysis getModel() {
    return this.model;
  }

  @Override
  public void chargeModel() {
    String FilePath = model.getFileProject().getAbsolutePath();
    if (FilePath.contains(getBaseFolder())) {
      FilePath = "..."+FilePath.substring(getBaseFolder().length(), FilePath.length());
    }
    lblproject.setText(FilePath);
    if (model.getType() == GROUP) {
      lbltype.setText("Grupal");

    } else {
      lbltype.setText("Individual");
    }
    lblplugin.setText(model.getPluginSelected().getName());
    lblconfiuracion.setText(model.getConfigurationSelected().getId());
    TabPane aux = new TabPane();
    for (Group group : model.getGroups()) {
      Tab tab = new Tab();
      tab.setText("Grupo " + group.getId());
      Label lblStrong = new Label("Etiqueta");
      lblStrong.setLayoutX(lbllabelstrongX);
      lblStrong.setLayoutY(lbllabelstrongY);
      lblStrong.setFont(bold);
      Label lbl = new Label(group.getLabel());
      lbl.setLayoutX(lbllabelX);
      lbl.setLayoutY(lbllabelY);
      lbl.setFont(simple);
      ListView<ParticipantFilePair> listView = new ListView<ParticipantFilePair>();
      listView.setLayoutX(listGroupX);
      listView.setLayoutY(listGroupY);
      listView.setPrefHeight(listGroupHeight);
      listView.setPrefWidth(listGroupWidth);
      listView.setCellFactory(new ParticipantFileCellFactory());

      ObservableList<ParticipantFilePair> nuevaListView = FXCollections.<ParticipantFilePair>observableArrayList();
      for (Participant participant : group.getParticipants()) {
        nuevaListView.add(new ParticipantFilePair(participant, group.getParticipantFiles().get(participant.id)));
      }
      listView.setItems(nuevaListView);
      AnchorPane anchorPane = new AnchorPane(lblStrong, lbl, listView);
      anchorPane.setPrefSize(180.0, 200.0);
      tab.setContent(anchorPane);
      aux.getTabs().add(tab);
    }
    TabPane.getTabs().setAll(aux.getTabs());
  }

  @FXML
  private void save(ActionEvent event) {
    Alert alert;

    if (SaveConfigure(model)) {
      alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Success");
      alert.setHeaderText(null);
      alert.setContentText("Resumen guardado con exito.");
    } else {
      alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText("Se Produjo un error al intentar guardar el resumen.\nIntentelo nuevamente.");
    }
    alert.showAndWait();
  }

  class ParticipantFilePair {

    Participant participant;
    File file;

    public ParticipantFilePair(Participant participant, File file) {
      this.participant = participant;
      this.file = file;
    }

    public Participant getParticipant() {
      return participant;
    }

    public File getFile() {
      return file;
    }
  }

  class ParticipantFileCellFactory implements Callback<ListView<ParticipantFilePair>, ListCell<ParticipantFilePair>> {

    @Override
    public ListCell<ParticipantFilePair> call(ListView<ParticipantFilePair> listview) {
      return new ParticipantFileCell();
    }
  }

  class ParticipantFileCell extends ListCell<ParticipantFilePair> {

    @Override
    public void updateItem(ParticipantFilePair item, boolean empty) {
      super.updateItem(item, empty);
      if (!(item == null || empty)) {
        String id = item.getParticipant().id;
        String name = item.getParticipant().name;
        String FilePath = item.getFile().getAbsolutePath();
        if (FilePath.contains(getBaseFolder())) {
          FilePath = FilePath.substring(getBaseFolder().length(), FilePath.length());
        }
        this.setText(id + " - " + name + "  File:..." + FilePath);
      } else {
        this.setText(null);
      }
      setGraphic(null);
    }
  }
}
