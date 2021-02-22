/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.Controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.StagePlugin;
import static mo.wizardAnalysis.utils.TypeId.GROUP;
import static mo.wizardAnalysis.utils.TypeId.INDIVIDUAL;
import mo.wizardAnalysis.model.Controller;
import mo.wizardAnalysis.model.OrgWizardAnalysis;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class ParticipantController implements Initializable, Controller {

  @FXML
  private ListView<Participant> participantList;
  @FXML
  private ListView<Participant> useList;
  @FXML
  private JFXButton btnAdd, btnDelete, btnAddAll, btnDeleteAll;

  List<Participant> participantInter = new ArrayList<>();
  String selected;
  @FXML
  private Label warning;
  @Inject
  OrgWizardAnalysis model;
  @Inject
  Injector injector;
  private final IntegerProperty currentStep = new SimpleIntegerProperty(-1);
  private final IntegerProperty click = new SimpleIntegerProperty(0);

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    // initParticipantList();
    initButtons();
    useList.setCellFactory(new ParticipantCellFactory());
    participantList.setCellFactory(new ParticipantCellFactory());
  }

  void initParticipantList() {
    currentStep.set(model.getType());
    participantInter = model.getParticipantsNoUsed();
    participantList.setOnMouseClicked(new EventHandler<MouseEvent>(){
     @Override
    public void handle(MouseEvent click) {
        if (click.getClickCount() == 2) {
           add();
        }
    }
    });
    participantList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Participant>() {
      @Override
      public void changed(ObservableValue<? extends Participant> observableValue, Participant oldValue,
          Participant newValue) {
      }
    });
    participantList.getSelectionModel();
    ObservableList<Participant> nuevaListView = FXCollections.<Participant>observableArrayList();
    for (Participant participant : model.getParticipantsNoUsed()) {
      if (!participant.isLocked) {
        nuevaListView.add(participant);
      }
    }
    participantList.setItems(nuevaListView);
    ObservableList<Participant> nuevaListView2 = FXCollections.<Participant>observableArrayList();
    for (Participant participant : model.getParticipants()) {
      if (!participant.isLocked) {
        nuevaListView2.add(participant);
      }
    }
    useList.setItems(nuevaListView2);
    useList.setOnMouseClicked(new EventHandler<MouseEvent>(){
     @Override
    public void handle(MouseEvent click) {
        if (click.getClickCount() == 2) {
           delete();
        }
      }
    });
  }

  @FXML
  void add() {
    Participant item = participantList.getSelectionModel().getSelectedItem();
    if (item != null) {
      if (model.getType() == INDIVIDUAL) {
        click.set(1);
        if(useList.getItems().size()>=1){
          return;
        }
      }
      useList.getItems().add(item);
      participantList.getItems().remove(item);
      warning.setVisible(false);
    }
  }

  @FXML
  void delete() {
    Participant item = useList.getSelectionModel().getSelectedItem();
    if (item != null) {
      participantList.getItems().add(item);
      useList.getItems().remove(item);
      if (model.getType() == INDIVIDUAL) {
        click.set(0);
      }
    }
  }

  @FXML
  void addAll(ActionEvent event) {
    useList.getItems().addAll(participantList.getItems());
    participantList.setItems(FXCollections.<Participant>observableArrayList());
    warning.setVisible(false);

  }

  @FXML
  void deleteAll(ActionEvent event) {
    participantList.getItems().addAll(useList.getItems());
    useList.setItems(FXCollections.<Participant>observableArrayList());
  }

  @Override
  public boolean validateNext() {
    if (useList.getItems().size() > 0) {
      warning.setVisible(false);
      model.setParticipants(useList.getItems());
      //model.setParticipantsAll(participantList.getItems());
      return true;
    }
    warning.setText("Debe seleccionar al menos un participante");
    warning.setVisible(true);
    return false;
  }

  private void initButtons() {
    btnAddAll.disableProperty().bind(currentStep.lessThanOrEqualTo(INDIVIDUAL));
    btnDeleteAll.disableProperty().bind(currentStep.lessThanOrEqualTo(INDIVIDUAL));
    btnAdd.disableProperty().bind(click.isEqualTo(INDIVIDUAL));
  }

  @Override
  public void initLanguage() {

  }

  @Override
  public void chargeModel() {
    initParticipantList();
  }

  @Override
  public OrgWizardAnalysis getModel() {
    return this.model;
  }

}

class ParticipantCellFactory implements Callback<ListView<Participant>, ListCell<Participant>> {

  @Override
  public ListCell<Participant> call(ListView<Participant> listview) {
    return new ParticipantCell();
  }
}

class ParticipantCell extends ListCell<Participant> {

  @Override
  public void updateItem(Participant item, boolean empty) {
    super.updateItem(item, empty);
    if (!(item == null || empty)) {
      String id = item.id;
      String name = item.name;
      this.setText(id + " - " + name);
    } else {
      this.setText(null);
    }
    setGraphic(null);
  }

}
