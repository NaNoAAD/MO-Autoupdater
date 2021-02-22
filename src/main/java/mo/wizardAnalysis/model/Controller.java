/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.model;

/**
 *
 * @author Jorge
 */

public interface Controller {
  boolean validateNext();
  void initLanguage();
  void chargeModel();
  OrgWizardAnalysis getModel();
}
