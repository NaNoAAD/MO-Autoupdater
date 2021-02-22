/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.Controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXRadioButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import static mo.wizardAnalysis.utils.TypeId.GROUP;
import static mo.wizardAnalysis.utils.TypeId.INDIVIDUAL;
import mo.wizardAnalysis.model.Controller;
import mo.wizardAnalysis.model.OrgWizardAnalysis;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class TypeController implements Initializable, Controller {

  private final String CONTROLLER_KEY = "controller";

  @FXML
  private JFXRadioButton individual;
  @FXML
  private JFXRadioButton grupal;
  @FXML
  private Label warning;
  int selected;
  @Inject
  OrgWizardAnalysis model;
  @Inject
  Injector injector;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  @Override
  public OrgWizardAnalysis getModel() {
    return this.model;
  }

  @FXML
  public void onChangeIndividual() {
    if (grupal.isSelected()) {
      grupal.setSelected(false);
      warning.setVisible(false);
    }
    selected = INDIVIDUAL;

  }

  @FXML
  public void onChangeGrupal() {
    if (individual.isSelected()) {
      individual.setSelected(false);
      warning.setVisible(false);
    }
    selected = GROUP;
  }

  @Override
  public boolean validateNext() {
    if (!grupal.isSelected() && !individual.isSelected()) {
      warning.setText("Debe selecionar una");
      warning.setVisible(true);
      return false;
    }
    warning.setVisible(false);
    model.setType(selected);
    return true;
  }

  @Override
  public void initLanguage() {

  }

  @Override
  public void chargeModel() {
    selected = model.getType();
    if (selected == GROUP) {
      grupal.setSelected(true);
    } else if (selected == INDIVIDUAL) {
      individual.setSelected(true);
    }
  }
}
