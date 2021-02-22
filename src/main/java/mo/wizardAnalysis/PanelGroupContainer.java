/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Jorge
 */
public class PanelGroupContainer extends javax.swing.JPanel {

  int Grid = 2;
  GridLayout gridLayout = new GridLayout(1, Grid);
  ArrayList<JCheckBox> ListNames = new ArrayList<JCheckBox>();
  ArrayList<JInternalFrame> ListPanels = new ArrayList<JInternalFrame>();
  int Rows = 0;
  int Cols = 0;
  Dimension dim = new Dimension();
  int selected = 0;

  /**
   * Creates new form PanelGroupContainer
   */
  public PanelGroupContainer() {
    initComponents();
    spin.setValue(Grid);
    spin.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        JSpinner a = (JSpinner) e.getSource();
        Grid = (Integer) a.getValue();
        recalc();
        reload();
      }
    });
    ListGroup.setLayout(new BoxLayout(ListGroup, BoxLayout.Y_AXIS));
    PanelVisualGroup.setPreferredSize(PanelVisualGroup.getMaximumSize());
    PanelVisualGroup.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        reload();
      }

      @Override
      public void componentMoved(ComponentEvent e) {
      }
    });

  }

  public void addPanels(JPanel panelGeneral, JPanel panelReproductor, String name) {
    //Panel Lista
    JCheckBox item = new JCheckBox();
    item.setName(name);
    item.setText(name);
    item.setSelected(true);
    item.addItemListener(changeSelected(item));
    selected++;
    ListNames.add(item);
    ListGroup.add(item);
    //Panel Vistas
    JPanel PanelExtra = new JPanel();
    PanelExtra.setLayout(new BoxLayout(PanelExtra, BoxLayout.Y_AXIS));

    //Grid Set
    recalc();
    //
    Dimension size = PanelVisualGroup.getSize();
    int width = size.width / Grid;
    int height = size.height;
    JScrollPane scrollpanel = new JScrollPane();
    JScrollPane scrollReproductor = new JScrollPane();

    scrollpanel.setBounds(5, 10, width + 20, height + 20);
    scrollReproductor.setBounds(5, 10, width + 20, 67 + 20); // Reproductor
    dim = new Dimension(panelGeneral.getMinimumSize().width, panelGeneral.getMinimumSize().height + 30);
    int rule3 = (760 * panelGeneral.getHeight()) / panelGeneral.getWidth();
    panelGeneral.setPreferredSize(new Dimension(760, rule3));

    // 67 height minimo del resproductor
    panelReproductor.setMaximumSize(new Dimension(panelReproductor.getMaximumSize().width, 67));
    panelReproductor.setMinimumSize(new Dimension(panelReproductor.getMinimumSize().width, 67));
    panelReproductor.setPreferredSize(new Dimension(panelReproductor.getPreferredSize().width, 67));
    scrollReproductor.setMaximumSize(new Dimension(scrollReproductor.getMaximumSize().width, 67));

    scrollpanel.setViewportView(panelGeneral);
    scrollReproductor.setViewportView(panelReproductor);

    PanelExtra.add(scrollpanel);
    PanelExtra.add(scrollReproductor);

    JInternalFrame internal = new JInternalFrame(name);
    internal.setSize(size);
    internal.setIconifiable(true);
    internal.setMaximizable(true);
    internal.setResizable(true);
    internal.add(PanelExtra);
    if (ListNames.size() > 1) {
      spin.setModel(new javax.swing.SpinnerNumberModel(2, 1, ListNames.size(), 1));
    } else {
      spin.setModel(new javax.swing.SpinnerNumberModel(2, 1, 2, 1));

    }
    ListPanels.add(internal);
    PanelVisualGroup.add(internal);
    internal.setVisible(true);
    internal.show();
  }

  ItemListener changeSelected(JCheckBox item) {

    return new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (((JCheckBox) e.getItem()).isSelected()) {
          selected++;
        } else {
          selected--;
        }
        recalc();
        reload();
      }
    };
  }

  void reload() {
    PanelVisualGroup.removeAll();
    Rectangle size = PanelVisualGroup.getBounds();
    int heigth = Rows != 0 ? size.height / Rows : size.height;
    int width = Cols != 0 ? size.width / Cols : size.width;
    int localRow = 0;
    int localCol = 0;
    for (int i = 0; i < ListNames.size(); i++) {
      JCheckBox item = ListNames.get(i);
      JInternalFrame panel = ListPanels.get(i);
      if (item.isSelected()) {
        System.out.println("ITEM " + i + " AGREGADO");
        System.out.println(localCol + " * " + width + " , " + localRow + " * " + heigth);
        panel.setSize(new Dimension(width, heigth));
        panel.setLocation(localCol * width, localRow * heigth);
        PanelVisualGroup.add(panel);
        panel.show();

        if (localCol < Cols - 1) {
          localCol++;
        } else {
          localCol = 0;
          localRow++;
        }
      }
    }
    this.repaint();
  }

  public void recalc() {
    int Cols = 0, Rows = 0;
    for (int i = 0; i < selected; i++) {
      if (Cols == 0 && Rows == 0) {
        Cols = 1;
        Rows = 1;
      } else if (Cols < Grid) {
        Cols++;
      } else {
        Cols = 1;
        Rows++;
      }
    }
    
     this.Cols = (Rows>1)?Grid:Cols;
    this.Rows = Rows;
    System.out.println(Rows+"  "+Cols);
  }

  /**
   * This method is called from within the constructor to initialize
   * the form. WARNING: Do NOT modify this code. The content of this
   * method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jInternalFrame1 = new javax.swing.JInternalFrame();
    jSplitPane1 = new javax.swing.JSplitPane();
    jPanel2 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    spin = new javax.swing.JSpinner();
    ListGroup = new javax.swing.JPanel();
    PanelVisualGroup = new javax.swing.JDesktopPane();

    jInternalFrame1.setVisible(true);

    javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
    jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
    jInternalFrame1Layout.setHorizontalGroup(
      jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jInternalFrame1Layout.setVerticalGroup(
      jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );

    setAutoscrolls(true);

    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Grupos");

    jLabel2.setText("Columnas");

    spin.setModel(new javax.swing.SpinnerNumberModel(2, 1, null, 1));

    javax.swing.GroupLayout ListGroupLayout = new javax.swing.GroupLayout(ListGroup);
    ListGroup.setLayout(ListGroupLayout);
    ListGroupLayout.setHorizontalGroup(
      ListGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    ListGroupLayout.setVerticalGroup(
      ListGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 213, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addComponent(ListGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(spin, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(spin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(ListGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    jSplitPane1.setLeftComponent(jPanel2);

    javax.swing.GroupLayout PanelVisualGroupLayout = new javax.swing.GroupLayout(PanelVisualGroup);
    PanelVisualGroup.setLayout(PanelVisualGroupLayout);
    PanelVisualGroupLayout.setHorizontalGroup(
      PanelVisualGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 452, Short.MAX_VALUE)
    );
    PanelVisualGroupLayout.setVerticalGroup(
      PanelVisualGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 273, Short.MAX_VALUE)
    );

    jSplitPane1.setRightComponent(PanelVisualGroup);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jSplitPane1)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jSplitPane1)
    );
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel ListGroup;
  private javax.swing.JDesktopPane PanelVisualGroup;
  private javax.swing.JInternalFrame jInternalFrame1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSpinner spin;
  // End of variables declaration//GEN-END:variables
}
