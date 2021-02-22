/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.Controller;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static mo.wizardAnalysis.WizardManagement.panel;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class PanelGroupsController implements Initializable {

  @FXML
  public SplitPane paneG;
  @FXML
  private VBox VboxList;
  @FXML
  private AnchorPane paneles;
  @FXML
  private GridPane GridVisual;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    paneG.setPrefSize(1362, 318);
    panel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
      }

      @Override
      public void componentMoved(ComponentEvent e) {
      }
    });
  }

  public void addElement(JPanel jpanel, JPanel jpanel2) {
    GridVisual.getRowConstraints().size();
    GridVisual.getColumnConstraints().size();
    SwingNode swingNode1 = new SwingNode();
    addPanelToFx(swingNode1, jpanel);
    SwingNode swingNode2 = new SwingNode();
    addPanelToFx(swingNode2, jpanel2);

    GridVisual.add(swingNode1, 0, 0);
    GridVisual.add(swingNode2, 0, 1);
  }

  public void addPanelToFx(SwingNode node, JPanel jpanel) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        node.setContent(jpanel);
      }
    });

  }

}
