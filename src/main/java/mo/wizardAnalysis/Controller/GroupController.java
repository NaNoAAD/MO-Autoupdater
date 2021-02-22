/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.Controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import mo.organization.Participant;
import mo.wizardAnalysis.model.Group;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import mo.analysis.PlayableAnalyzableConfiguration;
import static mo.core.Utils.getBaseFolder;
import mo.wizardAnalysis.model.Controller;
import mo.wizardAnalysis.model.OrgWizardAnalysis;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class GroupController implements Initializable, Controller {

  private static final Logger logger = Logger.getLogger(GroupController.class.getName());

  private List<Participant> participantInter = new ArrayList<>();
  @FXML
  private TableView<ParticipantGroupIdPair> TableParticipant = new TableView<ParticipantGroupIdPair>(
      FXCollections.<ParticipantGroupIdPair>observableArrayList());
  @FXML
  private TableColumn<ParticipantGroupIdPair, Participant> columnParticipant;
  @FXML
  private TableColumn<ParticipantGroupIdPair, Spinner<Integer>> columnGroup;
  @FXML
  private TableColumn<ParticipantGroupIdPair, Button> columnFile;
  private HashMap<String, File> filesParticipant = new HashMap<String, File>();
  @FXML
  private TableView<Group> TableResume = new TableView<Group>(FXCollections.<Group>observableArrayList());
  @FXML
  private TableColumn<Group, Integer> columnNumber;
  @FXML
  private TableColumn<Group, String> columnResume;
  @FXML
  private TableColumn<Group, String> columnLabel;
  @FXML
  private Spinner<Integer> spinner;
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

    spinner.valueProperty().addListener(new ChangeListener<Integer>() {
      @Override
      public void changed(ObservableValue<? extends Integer> observableValue, Integer oldValue, Integer newValue) {
        setGroupSpinner();
      }
    });
    columnParticipant.setCellValueFactory(cell -> new SimpleObjectProperty<Participant>(cell.getValue().participant));
    columnParticipant.setCellFactory(column -> {
      return new TableCell<ParticipantGroupIdPair, Participant>() {
        @Override
        protected void updateItem(Participant item, boolean empty) {
          super.updateItem(item, empty);

          if (!(empty)) {
            if (item == null) {
              this.setText("no Found");
            } else {
              this.setText(item.id + " - " + item.name);
            }
          }
        }
      };
    });

    columnGroup.setCellValueFactory(cell -> new SimpleObjectProperty<Spinner<Integer>>(cell.getValue().grupoSpinner));
    columnFile.setCellValueFactory(cell -> new SimpleObjectProperty<Button>(cell.getValue().button));
    columnNumber.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());
    columnLabel.setCellFactory(TextFieldTableCell.forTableColumn());
    columnLabel.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLabel()));
    columnResume
        .setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSizeParticipant() + " Participantes"));
  }

  private void initTableParticipant() {
    ObservableList<ParticipantGroupIdPair> data = FXCollections.<ParticipantGroupIdPair>observableArrayList();
    for (Participant participant : model.getParticipants()) {
      Spinner<Integer> spin = newSpinner(spinner.getValue());
      ParticipantGroupIdPair pair = new ParticipantGroupIdPair(participant, spin, newButton(participant));
      filesParticipant.put(participant.id, null); //newButton(pair, participant, index
      data.add(pair);
    }
    TableParticipant.setItems(data);
  }

  private void initSpinner() {
    participantInter = model.getParticipants();
    SpinnerValueFactory<Integer> factorySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
        model.getParticipants().size(), 1);
    spinner.setValueFactory(factorySpinner);
  }

  public void OnChangeGroup(TableColumn.CellEditEvent<ParticipantGroupIdPair, Integer> event) {
    ParticipantGroupIdPair item = TableParticipant.getSelectionModel().getSelectedItem();
  }

  public void onChangeLabel(TableColumn.CellEditEvent<Group, String> event) {
    Group item = TableResume.getSelectionModel().getSelectedItem();
    item.setLabel(event.getNewValue());
  }

  private void initTableResume() {
    ObservableList<Group> data = FXCollections.<Group>observableArrayList();
    for (int i = 0; i < spinner.getValue(); i++) {
      Group group = new Group(i + 1);
      for (ParticipantGroupIdPair pair : TableParticipant.getItems()) {
        if (pair.getGrupoSpinner().getValue() == i + 1) {
          group.addParticipant(pair.participant);
        }
      }
      data.add(group);
    }
    TableResume.setItems(data);
  }

  private void setGroupSpinner() {
    if (spinner.getValue() > TableResume.getItems().size()) {
      ObservableList<Group> data = FXCollections.<Group>observableArrayList();
      for (int i = TableResume.getItems().size(); i < spinner.getValue(); i++) {
        data.add(new Group(i + 1));
      }
      TableResume.getItems().addAll(data);
    } else {
      for (int i = TableResume.getItems().size(); i > spinner.getValue(); i--) {
        TableResume.getItems().remove(i - 1);
      }
    }
    for (ParticipantGroupIdPair item : TableParticipant.getItems()) {
      item.setMaxSpinner(spinner.getValue());
    }

  }

  public Spinner<Integer> newSpinner(Integer Max) {
    Spinner<Integer> spinner = new Spinner<Integer>(1, Max, 1);
    spinner.valueProperty().addListener(new ChangeListener<Integer>() {
      @Override
      public void changed(ObservableValue<? extends Integer> observableValue, Integer oldValue, Integer newValue) {
        setResumeSpinner();
      }
    });
    return spinner;
  }

  public Spinner<Integer> newSpinner(Integer Max, Integer value) {
    Spinner<Integer> spinner = new Spinner<Integer>(1, Max, value);
    spinner.valueProperty().addListener(new ChangeListener<Integer>() {
      @Override
      public void changed(ObservableValue<? extends Integer> observableValue, Integer oldValue, Integer newValue) {
        setResumeSpinner();
      }
    });
    return spinner;
  }

  public JFXButton newButton(Participant participant) {
    JFXButton button = new JFXButton();
    FontAwesomeIconView wrong = new FontAwesomeIconView();
    wrong.setGlyphName("CLOSE");
    wrong.setStyle("-fx-fill:#B40404cc");
    FontAwesomeIconView check = new FontAwesomeIconView();
    check.setGlyphName("CHECK");
    check.setStyle("-fx-fill:#166e22cc");
    button.setGraphic(wrong);
    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        warning.setVisible(false);
        File file = filesParticipant.get(participant.id);
        if (file != null) {
          filesParticipant.put(participant.id, null);
          button.setGraphic(wrong);
          return;
        }
        boolean acepted = false;
        final FileChooser fileChooser = new FileChooser();
        File fileParticipant = new File(model.getFileProject() + "/" + participant.folder);
        if (fileParticipant.exists()) {
          fileChooser.setInitialDirectory(fileParticipant);
        } else {
          fileChooser.setInitialDirectory(new File(model.getFileProject().getAbsolutePath()));
        }
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Configure", "*.desc"));
        File fileGet = fileChooser.showOpenDialog(null);
        Properties prop = new Properties();
        PlayableAnalyzableConfiguration config = (PlayableAnalyzableConfiguration) model.getConfigurationSelected();
        List<String> creators = config.getCompatibleCreators();
        while (!acepted && fileGet != null) {
          try {
            Path t = fileGet.toPath();
            String s = new String(Files.readAllBytes(t));
            prop.load(new StringReader(s.replace("\\", "\\\\")));
            if (prop.containsKey("creator")) {
              for (String creator : creators) {
                if (prop.get("creator").equals(creator)) {
                  if (prop.containsKey("file")) {
                    File f = t.resolve(prop.getProperty("file")).normalize().toFile();
                    if (f.exists()) {
                      acepted = true;
                      button.setGraphic(check);
                      filesParticipant.put(participant.id, f);
                    }
                  }
                }
              }
            }
            if (!acepted) {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Error");
              alert.setHeaderText(null);
              alert.setContentText("Archivo no compatible con el plugin selecionado");
              alert.showAndWait();
              fileGet = fileChooser.showOpenDialog(null);
            }
          } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            break;
          }
        }
      }
    });
    return button;
  }

  public void setResumeSpinner() {
    ArrayList<ArrayList<Participant>> groupCount = new ArrayList<ArrayList<Participant>>();
    ArrayList<HashMap<String, File>> groupFiles = new ArrayList<HashMap<String, File>>();
    for (int i = 0; i < spinner.getValue(); i++) {
      groupCount.add(new ArrayList<Participant>());
      groupFiles.add(new HashMap<String, File>());
    }

    for (ParticipantGroupIdPair participantGroup : TableParticipant.getItems()) {
      int value = participantGroup.grupoSpinner.getValue();
      try {
        groupCount.get(value - 1).add(participantGroup.participant);
        groupFiles.get(value - 1).put(participantGroup.participant.id, filesParticipant.get(participantGroup.participant.id));
      } catch (Exception e) {
        System.out.println(e);
        System.out.println(groupCount.size() + "size, " + (value - 1) + " index ");
      }
    }
    TableResume.getItems().sorted();
    ObservableList<Group> items = FXCollections.<Group>observableArrayList();
    for (Group group : TableResume.getItems()) {
      items.add(group);
    }
    for (int i = 0; i < groupCount.size(); i++) {
      List<Participant> participants = groupCount.get(i);
      HashMap<String, File> FilesHash = groupFiles.get(i);
      for (int j = 0; j < items.size(); j++) {
        if (items.get(j).getId() == i + 1) {
          items.get(j).setParticipants(participants);
          items.get(j).setParticipantFiles(FilesHash);
        }
      }
    }
    TableResume.setItems(items);
    TableResume.refresh();
  }

  @Override
  public boolean validateNext() {
    for (Group item : TableResume.getItems()) {
      if (item.getSizeParticipant() == 0) {
        if (item.getLabel().equals("etiqueta " + item.getId())) {
          warning.setText("grupo " + item.getId() + " no tiene integrantes");
        } else {
          warning.setText("grupo " + item.getLabel() + " no tiene integrantes");
        }
        warning.setVisible(true);
        return false;
      }
      for (Participant participant : item.getParticipants()) {
        File file = filesParticipant.get(participant.id);
        item.getParticipantFiles().put(participant.id, file);
        if (file == null) {
          warning.setText("Participante " + participant.id + " - " + participant.name + " no tiene archivo");
          warning.setVisible(true);
          return false;
        }
      }
      System.out.println("A" + item.getParticipantFiles() + "B");
    }
    warning.setVisible(false);

    model.setGroups(TableResume.getItems());
    return true;
  }

  @Override
  public void initLanguage() {

  }

  @Override
  public void chargeModel() {

    if (model.getRestoreBoolean()) {
      restore();
    } else if (TableResume.getItems().size() == 0) {
      initSpinner();
      initTableParticipant();
      initTableResume();
    }
  }

  public void initData() {
  }

  public void restore() {
    Integer max = model.getGroups().size();
    SpinnerValueFactory<Integer> factorySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
        model.getParticipants().size(), max);
    spinner.setValueFactory(factorySpinner);
    ObservableList<ParticipantGroupIdPair> pair = FXCollections.<ParticipantGroupIdPair>observableArrayList();
    for (Group item : model.getGroups()) {
      for (Participant participant : item.getParticipants()) {
        System.out.println("grupo " + item.getId() + "participante " + participant.name);
        Spinner<Integer> spin = newSpinner(max, item.getId());
        ParticipantGroupIdPair pairInter = new ParticipantGroupIdPair(participant, spin, max, newButton(participant));
        filesParticipant.put(participant.id, item.getParticipantFiles().get(participant.id)); //agregar file.
        newButton(participant);
        pair.add(pairInter);
      }
    }
    System.out.println(pair.size() + " participantes");
    TableParticipant.setItems((ObservableList<ParticipantGroupIdPair>) pair);
    ObservableList<Group> data1 = FXCollections.<Group>observableArrayList();
    for (Group group : model.getGroups()) {
      data1.add(group);
    }
    TableResume.setItems(data1);
    TableResume.refresh();

  }

  @Override
  public OrgWizardAnalysis getModel() {
    return this.model;
  }
}

class ParticipantGroupIdPair {

  Participant participant;
  Spinner<Integer> grupoSpinner;
  JFXButton button = new JFXButton();
  Integer max;
  private static final Logger logger = Logger.getLogger(ParticipantGroupIdPair.class.getName());

  public ParticipantGroupIdPair(Participant participant, Spinner<Integer> grupoSpinner, Integer max, JFXButton button) {
    this.participant = participant;
    this.grupoSpinner = grupoSpinner;
    this.max = max;
    this.button = button;
  }

  public ParticipantGroupIdPair(Participant participant, Spinner<Integer> grupoSpinner, JFXButton button) {
    this.participant = participant;
    this.grupoSpinner = grupoSpinner;
    this.button = button;
  }

  public Spinner<Integer> getGrupoSpinner() {
    return grupoSpinner;
  }

  public void setValueGrupoSpinner(Integer value) {
    SpinnerValueFactory<Integer> factorySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, max, value);
    grupoSpinner.setValueFactory(factorySpinner);
  }

  public void setMaxSpinner(Integer Max) {
    Integer value = grupoSpinner.getValue();
    if (value > Max) {
      value = 0;
    }
    SpinnerValueFactory<Integer> factorySpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Max, value);
    grupoSpinner.setValueFactory(factorySpinner);
  }

}
