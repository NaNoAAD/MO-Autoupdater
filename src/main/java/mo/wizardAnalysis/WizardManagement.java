/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis;

import bibliothek.gui.dock.common.CControl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import mo.core.I18n;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.core.ui.menubar.IMenuBarItemProvider;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.util.Callback;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import mo.analysis.AnalyzableConfiguration;
import mo.analysis.NotPlayableAnalyzableConfiguration;
import mo.analysis.NotesAnalysisConfig;
import mo.analysis.NotesVisualization;
import mo.analysis.PlayableAnalyzableConfiguration;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;
import mo.organization.StagePlugin;
import mo.visualization.PlayerControlsPanel;
import mo.visualization.VisualizableConfiguration;
import mo.visualization.VisualizationPlayer;
import mo.wizardAnalysis.Controller.LayoutController;
import mo.wizardAnalysis.Controller.PanelGroupsController;
import mo.wizardAnalysis.model.Group;
import mo.wizardAnalysis.model.OrgWizardAnalysis;
import static mo.wizardAnalysis.utils.TypeId.INDIVIDUAL;

/**
 *
 * @author Jorge
 */
@Extension(xtends = {
  @Extends(extensionPointId = "mo.core.ui.menubar.IMenuBarItemProvider")})
public class WizardManagement implements IMenuBarItemProvider {

  private JMenu projectMenu = new JMenu("Wizard");
  private JMenuItem analisis = new JMenuItem("Analisis");

  public static JFrame frame;
  public static JPanel panel;
  public static JFXPanel fxPanel;
  public FXMLLoader loader;
  public static LayoutController controller;
  public OrgWizardAnalysis model;
  public static int height = 650;
  public static int width = 915;
  private Thread[] threads;
  private JButton cancelButon;
  private JButton okButon;
  private I18n i18n;
  private boolean accepted;
  private DockableElement dockable;
  private PanelGroupContainer panelGC;
  private PanelGroupsController controllerPanel;
  PanelGroupContainer P;

  public WizardManagement() {

    analisis.addActionListener((java.awt.event.ActionEvent e) -> {
      openWizardAnalisis();
    });
    projectMenu.add(analisis);
  }

  @Override
  public JMenuItem getItem() {
    return projectMenu;
  }

  @Override
  public int getRelativePosition() {
    return -4;
  }

  @Override
  public String getRelativeTo() {
    return "options";
  }

  private void openWizardAnalisis() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        initAndShowGUI();
      }
    });
  }

  private void initAndShowGUI() {
    // This method is invoked on the EDT thread
    frame = new JFrame("Swing and JavaFX");
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        model = controller.model;
        if (controller.byEnd) {
          closing();
        }
      }
    });
    frame.setResizable(false);
    fxPanel = new JFXPanel();
    frame.setSize(width, height);
    frame.getContentPane().add(fxPanel, BorderLayout.CENTER);
    frame.setVisible(true);
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        initFX(fxPanel);
      }
    });
  }

  private void initFX(JFXPanel fxPanel) {
    final Injector injector = Guice.createInjector(new WizardModule());
    final Parent p;
    try {
      loader = new FXMLLoader(LayoutController.class.getResource("/fxml/analysis/Layout.fxml"), null,
          new JavaFXBuilderFactory(), (ac) -> injector.getInstance(ac));
      p = loader.load();
      p.getProperties().put("controller", loader.getController());
      controller = (LayoutController) loader.getController();
      Scene scene = new Scene(p, width, height);
      fxPanel.setScene(scene);
    } catch (IOException ex) {
      Logger.getLogger(WizardManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void closing() {
    File file;
    File storageFolder;
    P = createPanelGroupSwing();
    for (Group group : model.getGroups()) {
      StagePlugin plugin = model.getPluginSelected();
      if (group.getParticipants().size() > 1 && model.getPluginSelected() instanceof GroupProvider) {
        GroupProvider pluginGroup = (GroupProvider) plugin;
        file = pluginGroup.groupProcess(group, model.getFileProject().getAbsolutePath());
        storageFolder = new File(model.getFileProject().getAbsolutePath() + "/Group-" + group.getLabel() + "/analysis");
        analyzeAction(file, group.getLabel(), "(" + group.getParticipants().size() + ")Participantes", storageFolder);
      } else {
        Participant participant = group.getParticipants().get(0);
        String p = participant.id;
        file = group.getParticipantFiles().get(p);
        storageFolder = new File(model.getOrg().getLocation() + "/" + participant.folder + "/analysis");
        analyzeAction(file, group.getLabel(), "(1)Participante", storageFolder);
      }
    }
    //File file = new File(org.getLocation(), "participant-1/capture/2020-03-04_19.02.25.953_eye.txt");
    //File storageFolder = file.getParentFile();//org.getLocation(), "participant-1/analysis");

  }

  private void analyzeAction(File file, String Grupo, String Participant, File storageFolder) {
    Integer IndexDockable = null;
    StagePlugin Notes = model.getNotesPlugin(); //notesConfiguration
    PlayableAnalyzableConfiguration notesConfiguration = (PlayableAnalyzableConfiguration) Notes.getConfigurations().get(0);
    ProjectOrganization org = model.getOrg();
    storageFolder.mkdirs();
    Configuration c = (Configuration) model.getConfigurationSelected();
    //ESTO SOLUCIONA LA UNICA REFERENCIA DE LA CONFIGURACION
    try {
      c = c.getClass().getConstructor(String.class).newInstance(model.getConfigurationSelected().getId());
    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      Logger.getLogger(WizardManagement.class.getName()).log(Level.SEVERE, null, ex);
    }
    //LA FALLA SE PRODUCE EN EL ORIGINAL
    Participant participant = model.getGroups().get(0).getParticipants().get(0);
    List<PlayableAnalyzableConfiguration> playableConfigurations = new ArrayList<>();
    List<NotPlayableAnalyzableConfiguration> notPlayableConfigurations = new ArrayList<>();
    List<VisualizableConfiguration> visualizableConfiguration = new ArrayList<>();
    PlayableAnalyzableConfiguration pac;
    VisualizableConfiguration vc;
    NotPlayableAnalyzableConfiguration npac;

    if (c instanceof PlayableAnalyzableConfiguration) {
      pac = (PlayableAnalyzableConfiguration) c;
      pac.addFile(file);
      playableConfigurations.add(pac);
      NotesVisualization notesVisualization = new NotesVisualization(file.getAbsolutePath(), pac.getClass().getName());
      ((NotesAnalysisConfig) notesConfiguration).addPlayable(notesVisualization);// #marca
    } else if (c instanceof VisualizableConfiguration) {
      vc = (VisualizableConfiguration) c;
      vc.addFile(file);
      visualizableConfiguration.add(vc);
      NotesVisualization notesVisualization = new NotesVisualization(file.getAbsolutePath(), vc.getClass().getName());
      ((NotesAnalysisConfig) notesConfiguration).addVisualizable(notesVisualization);// #marca
    } else {
      npac = (NotPlayableAnalyzableConfiguration) c;
      npac.addFile(file);
      notPlayableConfigurations.add(npac);
    }
    List<AnalyzableConfiguration> analyzableConfigurations = new ArrayList<>(playableConfigurations);
    analyzableConfigurations.addAll(notPlayableConfigurations);
    List<AnalyzableConfiguration> analyzableList = new ArrayList(analyzableConfigurations);
    analyzableList.add(notesConfiguration);

    JDialog waitDialog = new JDialog();
    JLabel label = new JLabel("Procesando, por favor espere.");
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JPanel buttonsPanel = new JPanel();
    cancelButon = new JButton("Cancelar");
    cancelButon.setEnabled(true);
    cancelButon.addActionListener((java.awt.event.ActionEvent e) -> {
      accepted = false;
      waitDialog.setVisible(false);
      waitDialog.dispose();
      for (Thread thread : threads) {
        thread.interrupt();
      }
      for (AnalyzableConfiguration config : analyzableList) {
        config.cancelAnalysis();
      }
    });

    okButon = new JButton("Aceptar");
    okButon.setEnabled(false);
    okButon.addActionListener((java.awt.event.ActionEvent e) -> {
      accepted = true;
      waitDialog.setVisible(false);
      waitDialog.dispose();
    });

    buttonsPanel.add(okButon);
    buttonsPanel.add(cancelButon);

    waitDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    waitDialog.setLocationRelativeTo(null);
    waitDialog.setTitle("Please Wait...");

    panel.add(label, BorderLayout.NORTH);
    panel.add(buttonsPanel, BorderLayout.SOUTH);
    waitDialog.add(panel);

    waitDialog.pack();
    waitDialog.setVisible(false);
    SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        threads = new Thread[analyzableList.size()];
        int i = 0;
        for (AnalyzableConfiguration config : analyzableList) {
          threads[i] = new Thread(new Runnable() {
            Integer Index;
            Integer IndexDockable;

            @Override
            public void run() {
              config.setupAnalysis(storageFolder, org, participant);
              config.startAnalysis();
            }
          });

          threads[i].start();
          threads[i].join();
          i++;

        }
        label.setText("Analisis listo");
        cancelButon.setEnabled(false);
        okButon.setEnabled(true);
        return null;
      }
    };
    int countA = DockablesRegistry.getInstance().getControl().getCDockableCount();
    CControl controlA = DockablesRegistry.getInstance().getControl();
    mySwingWorker.execute();
    waitDialog.setVisible(true);
    IndexDockable = getIndexNewDockable(countA, controlA);
    JPanel panelA = null;
    if (IndexDockable != null) {
      ((DockableElement) controlA.getCDockable(IndexDockable)).setTitleText("Grupo:" + Grupo + " " + Participant);
      DockableElement docka = (DockableElement) controlA.getCDockable(IndexDockable);
      docka.setVisible(false);
      panelA = (JPanel) docka.getContentPane();
    }
    
    if (accepted) {
      List<VisualizableConfiguration> vlista = (List<VisualizableConfiguration>) (List<?>) new ArrayList<>(playableConfigurations);
      vlista.addAll(visualizableConfiguration);
      VisualizationPlayer player = new VisualizationPlayer(vlista);
      player.getDockable().setTitleText("Player Controls-Grupo:" + Grupo + " " + Participant);
      JPanel panelB = (JPanel) player.getDockable().getContentPane();

      P.addPanels(panelA, panelB, Grupo);
    }
  }

  public DockableElement createPanelGroup() {
    panel = new JPanel();
    JFXPanel jFXPanel = new JFXPanel();
    panel.add(jFXPanel);
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          System.out.println((int) panel.getWidth() + " " + (int) panel.getHeight());
          System.out.println((int) jFXPanel.getWidth() + " " + (int) jFXPanel.getHeight());
          Parent root;
          FXMLLoader rootLoader = new FXMLLoader(PanelGroupsController.class.getResource("/fxml/analysis/PanelGroups.fxml"), null, new JavaFXBuilderFactory(), null);
          root = rootLoader.load();
          root.getProperties().put("controller", rootLoader.getController());
          controllerPanel = (PanelGroupsController) rootLoader.getController();
          Scene scene = new Scene(root);
          jFXPanel.setScene(scene);
          jFXPanel.setVisible(true);

        } catch (MalformedURLException ex) {
          Logger.getLogger(WizardManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          Logger.getLogger(WizardManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    dockable.add(panel);
    dockable.setTitleText("PRUEBA");
    DockablesRegistry.getInstance().addDockableInProjectGroup("PRUEBA", dockable);
    return dockable;
  }

  public PanelGroupContainer createPanelGroupSwing() {
    PanelGroupContainer P = new PanelGroupContainer();
    DockableElement dockable = new DockableElement();
    dockable.add(P);
    dockable.setTitleText("PRUEBA");
    DockablesRegistry.getInstance().addDockableInProjectGroup("PRUEBA", dockable);
    return P;
  }

  public Integer getIndexNewDockable(int cantPrev, CControl controlPrev) {
    Integer IndexDockable = null;
    int countB = DockablesRegistry.getInstance().getControl().getCDockableCount();
    CControl controlB = DockablesRegistry.getInstance().getControl();
    if (countB > cantPrev) {
      ArrayList<String> idsA = new ArrayList<String>();
      ArrayList<String> idsB = new ArrayList<String>();
      int j;
      for (j = 0; j < cantPrev; j++) {
        DockableElement dockA = (DockableElement) controlPrev.getCDockable(j);
        idsA.add(dockA.getId());
        DockableElement dockB = (DockableElement) controlB.getCDockable(j);
        idsB.add(dockB.getId());
      }
      idsB.add(((DockableElement) controlB.getCDockable(j)).getId());
      for (j = 0; j < countB; j++) {
        String idB = idsB.get(j);
        if (!idsA.contains(idB)) {
          IndexDockable = j;
          break;
        }
      }
    }
    return IndexDockable;
  }

}
