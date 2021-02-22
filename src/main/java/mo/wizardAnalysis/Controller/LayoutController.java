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
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javax.swing.SwingUtilities;
import mo.core.I18n;
import mo.visualization.VisualizableConfiguration;
import static mo.wizardAnalysis.WizardManagement.frame;
import mo.wizardAnalysis.model.Controller;
import mo.wizardAnalysis.model.OrgWizardAnalysis;

/**
 * FXML Controller class
 *
 * @author Jorge
 */
public class LayoutController implements Initializable {

  private final String CONTROLLER_KEY = "controller";

  @FXML
  private AnchorPane father;
  @FXML
  private FontAwesomeIconView selOpen, selTipo, selPlugin, selParti, selGroup, selResume;
  @FXML
  private AnchorPane contentPanel;
  @FXML
  private Button btnFin, btnCan, btnBack, btnNext;
  @FXML
  private JFXButton btnOpen, btnType, btnPlugin, btnParticipant, btnGroup, btnResume;
  @Inject
  public Injector injector;
  @Inject
  public OrgWizardAnalysis model;
  private I18n i18n;
  private final List<Parent> steps = new ArrayList<>();
  private final List<Controller> stepsControllers = new ArrayList<>();

  private final IntegerProperty currentStep = new SimpleIntegerProperty(-1);
  public int index = 0;
  OpenController openController;
  private Task[] tasks;
  private Thread[] threads;
  private ButtonType cancelButon;
  private ButtonType okButon;
  public boolean byEnd=false;
  private long current, start, end;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    buildSteps();
    initButtons();
    setInitialContent();

  }

  @FXML
  public void setPaneTipo() throws IOException {
  }

  @FXML
  public void setPanePlugin() throws IOException {
  }

  @FXML
  public void setPaneParticipante() throws IOException {
  }

  @FXML
  public void setPaneGrupo() throws IOException {
  }

  @FXML
  public void setPaneOpen() throws IOException {
    contentPanel.getChildren().remove(steps.get(currentStep.get()));
    currentStep.set(0);
    contentPanel.getChildren().add(steps.get(currentStep.get()));
    stepsControllers.get(currentStep.get()).chargeModel();
    disableBtns();
  }

  private void disableBtns() {
    FontAwesomeIconView[] select = {selOpen, selTipo, selPlugin, selParti, selGroup, selResume};
    JFXButton[] buttons = {btnOpen, btnType, btnPlugin, btnParticipant, btnGroup, btnResume};
    for (int i = 0; i < select.length; i++) {
      select[i].setVisible(false);
      buttons[i].setFocusTraversable(false);
    }
    select[currentStep.get()].setVisible(true);
    buttons[currentStep.get()].setFocusTraversable(false);
  }

  @FXML
  private void end(ActionEvent event) {
    // Pasos previos
    
    Controller controller = stepsControllers.get(currentStep.get());
    controller.validateNext();
    
    Platform.setImplicitExit(true);
    byEnd=true;
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        frame.setVisible(false);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      }
    });
    
    model = controller.getModel();
    /*StagePlugin Notes = model.getNotesPlugin(); //notesConfiguration
    PlayableAnalyzableConfiguration notesConfiguration = (PlayableAnalyzableConfiguration) Notes.getConfigurations().get(0);

    ProjectOrganization org = model.getOrg();
    File storageFolder = new File(org.getLocation(), "participant-1/analysis");
    File file = new File(org.getLocation(), "participant-1/capture/2020-03-04_19.02.25.953_eye.txt");
    storageFolder.mkdirs();
    Configuration c = (Configuration) model.getConfigurationSelected();
    Participant participant = model.getGroups().get(0).getParticipants().get(0);
    //AnalyzeAction analyzeAction= new AnalyzeAction();
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

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Please Wait...");
    alert.setGraphic(null);
    alert.setHeaderText(null);
    alert.setContentText("Procesando, por favor espere.");
    cancelButon = new ButtonType("Cancelar");
    okButon = new ButtonType("Aceptar");
    alert.getButtonTypes().setAll(okButon, cancelButon);
    alert.getDialogPane().lookupButton(okButon).setDisable(true);
    Task<Integer> FxWorker = new Task() {
      @Override
      protected Integer call() throws Exception {
        tasks = new Task[analyzableList.size()];
        threads = new Thread[analyzableList.size()];
        int i = 0;
        for (AnalyzableConfiguration config : analyzableList) {
          tasks[i] = new Task() {
            @Override
            protected Object call() throws Exception {
              config.setupAnalysis(storageFolder, org, participant);
              config.startAnalysis();
              return 1;
            }
          };
          threads[i] = new Thread(tasks[i]);
          threads[i].start();
          threads[i].join();
          i++;

        }
        alert.getDialogPane().lookupButton(okButon).setDisable(false);
        alert.getDialogPane().lookupButton(cancelButon).setDisable(true);
        return 1;
      }
    };
    Thread T=new Thread(FxWorker);
    T.start();
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == okButon) {
      List<VisualizableConfiguration> vlista = (List<VisualizableConfiguration>) (List<?>) new ArrayList<>(playableConfigurations);
      vlista.addAll(visualizableConfiguration);
      System.out.println(vlista.size()+"A");
      obtainMinAndMaxTime(vlista);

      // ((NotesPlayer) notesConfiguration.getPlayer()).setStart(start);
      //((NotesPlayer) notesConfiguration.getPlayer()).setEnd(end);
      //vlista.add(notesConfiguration);
      //System.out.println(vlista.size()+"B");
      //System.out.println(vlista);
      VisualizationPlayer player = new VisualizationPlayer(vlista);
      System.out.println(org.getLocation().getAbsolutePath()+" "+player.getDockable());
      DockablesRegistry.getInstance().addDockableInProjectGroup(null, player.getDockable());

    } else {
      for (Task task : tasks) {
        task.cancel();
      }
      for (AnalyzableConfiguration config : analyzableList) {
        config.cancelAnalysis();
      }
    }
    /*JDialog waitDialog = new JDialog();
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
      for (Task task : tasks) {
        task.cancel();
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
        tasks= new Task[analyzableList.size()];
        threads = new Thread[analyzableList.size()];
        int i = 0;
        for (AnalyzableConfiguration config : analyzableList) {
          JOptionPane.showMessageDialog(null, "Thread " + i + " " + config.getId());
          tasks[i]= new Task() {
            @Override
            protected Object call() throws Exception {
              System.out.println(storageFolder.getAbsolutePath()+"\n"+org.toString()+"\n"+participant.id);
              config.setupAnalysis(storageFolder, org, participant);
              config.startAnalysis();
              return 1;
            }
          };
          threads[i]=new Thread(tasks[i]);
          threads[i].start();
          System.out.println("START");
          //threads[i].join();
          threads[i] = new Thread(new Runnable() {
            @Override
            public void run() {
              System.out.println(storageFolder.getAbsolutePath()+"\n"+org.toString()+"\n"+participant.id);
              config.setupAnalysis(storageFolder, org, participant);
              config.startAnalysis();
            }
          });
          threads[i].start();
          System.out.println("START");
          threads[i].join();
          JOptionPane.showMessageDialog(null, "Thread " + i + "C");
          i++;
        }
        System.out.println("Analisis listo" + storageFolder.getAbsoluteFile() + "Analisis listo");
        label.setText("Analisis listo");
        cancelButon.setEnabled(false);
        okButon.setEnabled(true);

        return null;
      }
    };

    mySwingWorker.execute();
    waitDialog.setVisible(true);

    if (accepted) {
      List<VisualizableConfiguration> vlista = (List<VisualizableConfiguration>) (List<?>) new ArrayList<>(playableConfigurations);
      vlista.addAll(visualizableConfiguration);
      obtainMinAndMaxTime(vlista);

      ((NotesPlayer) notesConfiguration.getPlayer()).setStart(start);
      ((NotesPlayer) notesConfiguration.getPlayer()).setEnd(end);
      vlista.add(notesConfiguration);

      VisualizationPlayer player = new VisualizationPlayer(vlista);
      DockablesRegistry.getInstance().addDockableInProjectGroup(org.getLocation().getAbsolutePath(), player.getDockable());
    }*/
  }

  @FXML

  private void cancel(ActionEvent event) {
    Platform.setImplicitExit(true);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
      }
    });
  }

  @FXML
  private void back(ActionEvent event) {
    if (currentStep.get() > 0) {
      contentPanel.getChildren().remove(steps.get(currentStep.get()));
      currentStep.set(currentStep.get() - 1);
      contentPanel.getChildren().add(steps.get(currentStep.get()));
      if (model.getRestoreBoolean()) {
        stepsControllers.get(currentStep.get()).chargeModel();
      }
      disableBtns();
    }
  }

  @FXML
  private void next(ActionEvent event) {
    Controller controller = stepsControllers.get(currentStep.get());
    if (controller.validateNext()) {
      if (currentStep.get() < (steps.size() - 1)) {
        contentPanel.getChildren().remove(steps.get(currentStep.get()));
        if (model.getRestoreBoolean() && currentStep.get() == 0) {
          currentStep.set(steps.size() - 1);
        } else {
          currentStep.set(currentStep.get() + 1);
        }
        contentPanel.getChildren().add(steps.get(currentStep.get()));
        stepsControllers.get(currentStep.get()).chargeModel();
        disableBtns();
      }
    }
  }

  // Inicia los controladores de las pestanas
  public void buildSteps() {
    try {
      final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
      final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
      FXMLLoader loaderOpen = new FXMLLoader(LayoutController.class
          .getResource("/fxml/analysis/Open.fxml"), null,
          builderFactory, callback);
      Parent openParent = loaderOpen.load();

      openParent.getProperties()
          .put(CONTROLLER_KEY, loaderOpen.getController());

      FXMLLoader loaderType = new FXMLLoader(LayoutController.class.getResource("/fxml/analysis/Type.fxml"), null,
          builderFactory, callback);
      Parent tipoParent = loaderType.load();

      tipoParent.getProperties()
          .put(CONTROLLER_KEY, loaderType.getController());

      FXMLLoader loaderPlugin = new FXMLLoader(LayoutController.class.getResource("/fxml/analysis/Plugin.fxml"), null,
          builderFactory, callback);
      Parent pluginParent = loaderPlugin.load();

      pluginParent.getProperties()
          .put(CONTROLLER_KEY, loaderPlugin.getController());

      FXMLLoader loaderParticipant = new FXMLLoader(
          LayoutController.class.getResource("/fxml/analysis/Participant.fxml"), null, builderFactory, callback);
      Parent participanteParent = loaderParticipant.load();

      participanteParent.getProperties()
          .put(CONTROLLER_KEY, loaderParticipant.getController());

      FXMLLoader loaderGroup = new FXMLLoader(LayoutController.class.getResource("/fxml/analysis/Group.fxml"), null,
          builderFactory, callback);
      Parent grupoParent = loaderGroup.load();

      grupoParent.getProperties()
          .put(CONTROLLER_KEY, loaderGroup.getController());

      FXMLLoader loaderResume = new FXMLLoader(LayoutController.class.getResource("/fxml/analysis/Resume.fxml"), null,
          builderFactory, callback);
      Parent resumeParent = loaderResume.load();

      resumeParent.getProperties()
          .put(CONTROLLER_KEY, loaderResume.getController());

      openController = (OpenController) loaderOpen.getController();

      steps.addAll(Arrays.asList(openParent, tipoParent, pluginParent, participanteParent,
          grupoParent, resumeParent));
      stepsControllers.addAll(Arrays.asList(loaderOpen.getController(), loaderType.getController(),
          loaderPlugin.getController(), loaderParticipant.getController(),
          loaderGroup.getController(), loaderResume.getController()));

    } catch (IOException ex) {
      Logger.getLogger(LayoutController.class
          .getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void initLanguage() {
  }

  private void initButtons() {
    btnBack.disableProperty().bind(currentStep.lessThanOrEqualTo(0));
    btnNext.disableProperty().bind(currentStep.greaterThanOrEqualTo(steps.size() - 1));
    btnFin.visibleProperty().bind(currentStep.greaterThanOrEqualTo(steps.size() - 1));
  }

  private void setInitialContent() {
    currentStep.set(0); // first element
    selOpen.setVisible(true);
    btnOpen.setFocusTraversable(false);
    btnOpen.setEffect(null);
    contentPanel.getChildren().add(steps.get(currentStep.get()));
  }

  @FXML
  private void setPaneResume() {
    contentPanel.getChildren().remove(steps.get(currentStep.get()));
    currentStep.set(4);
    contentPanel.getChildren().add(steps.get(currentStep.get()));
    stepsControllers.get(currentStep.get()).chargeModel();
    disableBtns();
  }

  private void obtainMinAndMaxTime(List<VisualizableConfiguration> configs) {
    long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
    for (VisualizableConfiguration config : configs) {
      if (config.getPlayer().getStart() < min) {
        min = config.getPlayer().getStart();
      }
      if (config.getPlayer().getEnd() > max) {
        max = config.getPlayer().getEnd();
      }
    }
    if (min == Long.MAX_VALUE) {
      min = 0;
    }
    if (max == Long.MIN_VALUE) {
      max = 100000;
    }

    current = start = min;
    end = max;
  }
}
