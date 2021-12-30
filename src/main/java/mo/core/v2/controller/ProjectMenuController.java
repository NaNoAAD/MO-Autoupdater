/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import bibliothek.gui.dock.common.CControl;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import mo.analysis.AnalyzableConfiguration;
import mo.analysis.NotPlayableAnalyzableConfiguration;
import mo.analysis.NotesAnalysisConfig;
import mo.analysis.NotesVisualization;
import mo.analysis.PlayableAnalyzableConfiguration;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.core.ui.dockables.DockableElement;
import mo.core.ui.dockables.DockablesRegistry;
import mo.core.v2.Analysis.AnalyzeActionV2;
import mo.core.v2.model.Organization;
import mo.core.v2.Vizualization.VisualizeActionV2;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;
import mo.organization.StageAction;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import mo.visualization.VisualizableConfiguration;
import mo.visualization.VisualizationPlayer;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectMenuController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    
    public ProjectOrganization projectOrg;
    @FXML
    private BorderPane projectMenuPane;
    @FXML
    private ImageView closeButton;
    @FXML
    private Text nameProjectText;
    @FXML
    private Button participantsButton;
    @FXML
    private Button captureButton;
    @FXML
    private Button analysisButton;
    @FXML
    private Button visualizationButton;
    @FXML
    private AnchorPane centerPane;
    @FXML
    private Pane tutorialPane;
    int row=0;
    Integer IndexDockable = null;
    private Thread[] threads;
    
    //Participants
    private ScrollPane scrollParticipants = new ScrollPane();
    private GridPane gridPane = new GridPane();
    private GridPane gridPane2 = new GridPane();
    private GridPane gridPane3 = new GridPane();
    private GridPane gridPane4 = new GridPane();
    List<Participant> participants = new ArrayList<>();
    List<StageAction> action;
    private boolean edit = false;
    
    //Capture
    List<String> capturesAux = new ArrayList<>();
    List<String> analysisAux = new ArrayList<>();
    List<String> visuAux = new ArrayList<>();
    ProjectCapturesControllerAux pcca;
    ProjectAnalysisControllerAux paca;
    ProjectVisualizationControllerAux pvca;
    
    //Visu
    List<PlayableAnalyzableConfiguration> playableConfigurations = new ArrayList<>();
    
    @Inject
    public Injector injector;
    @Inject 
    Organization model; 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("newProject?: " + model.newProyect);
        if(model.newProyect!=0){
            name();
        }
        init();
        //addOneStage("name");
        //addAllStages();
    }    
    
    @FXML
    private void closeButton(MouseEvent event) {
    }

    @FXML
    private void clickParticipants(MouseEvent event) {
        row=0;
        ProjectParticipantsControllerAux ppca = new ProjectParticipantsControllerAux(model);
        //List<Node> nodes = gridPane.getChildren();
        //gridPane.getChildren().removeAll(nodes);
        scrollParticipants.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollParticipants.setPrefViewportWidth(600);
        scrollParticipants.setPrefViewportHeight(400);
        gridPane.setPrefWidth(600);
        gridPane.setPrefHeight(400);
        gridPane.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        if(model.getParticipants().isEmpty()){
            initVistaParticipants();
        }
        else{
            model.getParticipants().forEach((p) -> {
                add(p);
            });
        }
        centerPane.getChildren().clear();
        scrollParticipants.setContent(gridPane);
        centerPane.getChildren().add(scrollParticipants);
        if(!model.getConfigCaptures().isEmpty() || !model.getConfigAnalysis().isEmpty() || !model.getConfigVisualizations().isEmpty()){
            action = ppca.actions();
        }
        projectMenuPane.setCenter(centerPane);
        //System.out.println("is empty?: "+action.get(0));
    }

    @FXML
    private void clickCaptures(MouseEvent event) {
        addOneStage("CaptureStage");
        /*try {
            
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderCaptures = new FXMLLoader(MainWindowsController.class
                    .getResource("/fxml/core/ui/ProjectCaptures.fxml"), null,
                    builderFactory, callback);
            Parent captureParent = loaderCaptures.load();
            
            captureParent.getProperties()
                    .put(CONTROLLER_KEY, loaderCaptures.getController());
            centerPane.getChildren().add(captureParent);
        } catch (IOException ex) {
            Logger.getLogger(ProjectMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        row=0;
        pcca = new ProjectCapturesControllerAux(model);
        pcca.init();
        pcca.initCaptureStage();
        List<Node> nodes = gridPane2.getChildren();
        gridPane2.getChildren().removeAll(nodes);
        gridPane2.setPrefWidth(600);
        gridPane2.setPrefHeight(400);
        gridPane2.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        initVistaCaptures(0, model.getConfigCaptures());
        row = 0;
        capturesAux.clear();
        for(Pair<String, String> c : model.getConfigCaptures()){
            addConfigsV(c.getValue() + " (" + c.getKey() + ")", 0, capturesAux);
        }
        scrollParticipants.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollParticipants.setPrefViewportWidth(600);
        scrollParticipants.setPrefViewportHeight(400);
        scrollParticipants.setContent(gridPane2);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(scrollParticipants);
        projectMenuPane.setCenter(centerPane);
    }

    @FXML
    private void clickAnalysis(MouseEvent event) {
        addOneStage("AnalysisStage");
        /*try{
            addOneStage("AnalysisStage");
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderAnalysis = new FXMLLoader(MainWindowsController.class
                    .getResource("/fxml/core/ui/ProjectAnalysis.fxml"), null,
                    builderFactory, callback);
            Parent analysisParent = loaderAnalysis.load();
            
            analysisParent.getProperties()
                    .put(CONTROLLER_KEY, loaderAnalysis.getController());
            centerPane.getChildren().add(analysisParent);
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        row=0;
        paca = new ProjectAnalysisControllerAux(model);
        paca.init();
        paca.initAnalysisStage();
        System.out.println("a");
        List<Node> nodes = gridPane2.getChildren();
        gridPane2.getChildren().removeAll(nodes);
        gridPane2.setPrefWidth(600);
        gridPane2.setPrefHeight(400);
        initVistaCaptures(1, model.getConfigAnalysis());
        System.out.println("b");
        analysisAux.clear();
        if(!model.getConfigAnalysis().isEmpty()){
            for(Pair<String, String> c : model.getConfigAnalysis()){
                addConfigsV(c.getValue() + " (" + c.getKey() + ")", 1, analysisAux);
            }
        }
        scrollParticipants.setContent(gridPane2);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(scrollParticipants);
        projectMenuPane.setCenter(centerPane);
    }

    @FXML
    private void clickVisualizations(MouseEvent event) {
        addOneStage("VisualizationStage");
        /*try{
            addOneStage("VisualizationStage");
            centerPane.getChildren().clear();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderVisualization = new FXMLLoader(MainWindowsController.class
                    .getResource("/fxml/core/ui/ProjectVisualization.fxml"), null,
                    builderFactory, callback);
            Parent visualizationParent = loaderVisualization.load();
            
            visualizationParent.getProperties()
                    .put(CONTROLLER_KEY, loaderVisualization.getController());
            centerPane.getChildren().add(visualizationParent);
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        row=0;
        pvca = new ProjectVisualizationControllerAux(model);
        pvca.init();
        pvca.initVisualizationStage();
        List<Node> nodes = gridPane2.getChildren();
        gridPane2.getChildren().removeAll(nodes);
        gridPane2.setPrefWidth(600);
        gridPane2.setPrefHeight(400);
        System.out.println("preee: " + model.getConfigVisualizations().size());
        initVistaCaptures(2, model.getConfigVisualizations());
        visuAux.clear();
        for(Pair<String, String> c : model.getConfigVisualizations()){
            System.out.println("config visu");
            addConfigsV(c.getValue() + " (" + c.getKey() + ")", 2, visuAux);
        }
        scrollParticipants.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollParticipants.setPrefViewportWidth(600);
        scrollParticipants.setPrefViewportHeight(400);
        scrollParticipants.setContent(gridPane2);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(scrollParticipants);
        projectMenuPane.setCenter(centerPane);
    }

    public void init(){
        if(model.newProyect==0){
            tutorialPane.getChildren().clear();
            nameProjectText.setText(model.getOrg().getLocation().getName());
        }
        else{
            nameProjectText.setText(model.getOrg().getLocation().getName());
        }
    }
    
    private void addStageNodeIfNotExists(StageModule stage){
        String newNodeName = stage.getName();
        System.out.println("name stage: " + stage.getName());
        if(model.getOrg().getStages().isEmpty()||model.getOrg().getStages().size()<3){
            stage.setOrganization(model.getOrg());
            model.getOrg().addStage(stage);
            model.getOrg().store();
            System.out.println("added stage??_: "+model.getOrg().getStages().isEmpty());
        }
    }
    
    private void addAllStages(){
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
            System.out.println(nodeProvider);
            addStageNodeIfNotExists(nodeProvider);
        }
    }
    
    private void addOneStage(String name){
        List<Plugin> stagePlugins = PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.organization.StageModule");
        for(Plugin stagePlugin : stagePlugins){
            if(stagePlugin.getName().equals(name)){
                StageModule nodeProvider = (StageModule) stagePlugin.getNewInstance();
                int aux = model.getOrg().getStages().size();
                if(aux == 0){
                    addStageNodeIfNotExists(nodeProvider);
                }
                else{
                    if(aux == 1){
                        if(!nodeProvider.getName().equals(model.getOrg().getStages().get(0).getName())){
                            addStageNodeIfNotExists(nodeProvider);
                        }
                    }
                    else if(aux == 2){
                        if(!nodeProvider.getName().equals(model.getOrg().getStages().get(0).getName()) && !nodeProvider.getName().equals(model.getOrg().getStages().get(1).getName())){
                            addStageNodeIfNotExists(nodeProvider);
                        }
                    }
                }
            }
        }
    }
    
    private void name(){
        try{
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(ProjectMenuController.class
                    .getResource("/fxml/core/ui/NewProject.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
        }
        catch(IOException ex){
            Logger.getLogger(MainWindowsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Funciones de gestion de info de Participant
    public void addParticipant(MouseEvent event){
        addView("New Participant");
    }
    
    private void unLockParticipant(MouseEvent event, ImageView i) {
        System.out.println("1");
        model.getOrg().getParticipants().forEach((participant) -> {
            System.out.println("2");
            if(participant.id.equals(i.getId())){
                System.out.println("3");
                if(participant.isLocked){
                    System.out.println("4");
                    participant.isLocked=false;
                    ImageView iAux = new javafx.scene.image.ImageView("/images/unlocked2.png");
                    i.setImage(iAux.getImage());
                    System.out.println("cambio");
                    model.setParticipants(model.getOrg().getParticipants());
                    
                }
                else{
                    System.out.println("5");
                   participant.isLocked=true;
                   ImageView iAux = new javafx.scene.image.ImageView("/images/locked2.png");
                   i.setImage(iAux.getImage());
                   System.out.println("Cambio");
                   model.setParticipants(model.getOrg().getParticipants());
                }
            }
            System.out.println("participant locked?: " + participant.isLocked);
        });
    }
    
    public void record(MouseEvent event, String id){
        System.out.println("1");
        Participant p = model.getParticipantById(id);
        System.out.println("2");
        System.out.println(model.getOrg().getStages().isEmpty());
        for(StageModule sm : model.getOrg().getStages()){
            System.out.println("3");
            if(sm.getName().equals("Captura")){
                System.out.println("4");
                action.get(0).init(model.getOrg(), p, sm);
            }
        }
    }
    
    public void editParticipant(MouseEvent event, String id){
        edit = true;
        for(Participant p : model.getParticipants()){
            if(p.id.equals(id)){
                model.setpSelected(p);
                addView("Edit Participant");
                p.name = model.getpSelected().name;
                p.notes = model.getpSelected().notes;
                model.getOrg().updateParticipant(p);
                List<Node> nodos = gridPane.getChildren();
                gridPane.getChildren().removeAll(nodos);
                row=0;
                break;
            }
        }
        for(Participant participant : model.getOrg().getParticipants()){
            add(participant);
        }
    }
    
    public void deleteParticipant(MouseEvent event, String id){
        List<Node> nodos = gridPane.getChildren();
        gridPane.getChildren().removeAll(nodos);
        row=0;
        if(model.getParticipants().size()==1){
            Participant p = model.getParticipantById(id);
            model.getOrg().deleteParticipant(p);
            this.initVistaParticipants();
        }
        else{
            for(Participant participant : model.getOrg().getParticipants()){
                if(!participant.id.equals(id)){
                    add(participant);
                }
            }
            Participant pAux = model.getParticipantById(id);
            model.getOrg().deleteParticipant(pAux);
        }
        System.out.println("org: " + model.getOrg().getParticipants().size());
    }
    
    public void analysis(MouseEvent event, String id){
        Participant p = model.getParticipantById(id);
        //File storageFile = new File(model.getOrg().getLocation().getAbsolutePath() + p.folder + "\\capture");
        StageModule sm = null;
        File storageFolder = new File(model.getOrg().getLocation() + "/" + p.folder + "/analysis");
        PlayableAnalyzableConfiguration notesConfiguration = null;
        for(StageModule smAux : model.getOrg().getStages()){
            if(smAux.getName().equals(model.getAnalysisStage().getName())){
                sm=smAux;
            }
        }
        AnalyzeActionV2 action = new AnalyzeActionV2(model);
        action.init(model.getOrg(), p, sm);
        for(StagePlugin plugin : sm.getPlugins()){
            if(plugin.getName().equals("Notes plugin")){
                notesConfiguration = (PlayableAnalyzableConfiguration) plugin.getConfigurations().get(0);
            }
        }
        //AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
        Configuration c = model.getConfigurationSelected().get(0);
        try {
            c = c.getClass().getConstructor(String.class).newInstance(model.getConfigurationSelected().get(0).getId());
        } catch (NoSuchMethodException | SecurityException | InstantiationException
            | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ProjectMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<PlayableAnalyzableConfiguration> playableConfigurations = new ArrayList<>();
        List<NotPlayableAnalyzableConfiguration> notPlayableConfigurations = new ArrayList<>();
        List<VisualizableConfiguration> visualizableConfiguration = new ArrayList<>();
        PlayableAnalyzableConfiguration pac;
        VisualizableConfiguration vc;
        NotPlayableAnalyzableConfiguration npac;
        if (c instanceof PlayableAnalyzableConfiguration) {
            pac = (PlayableAnalyzableConfiguration) c;
            pac.addFile(model.getFile());
            playableConfigurations.add(pac);
            System.out.println("Path: " + model.getFile().getAbsolutePath());
            System.out.println("play: " + pac.getClass().getName());
            NotesVisualization notesVisualization = new NotesVisualization(model.getFile().getAbsolutePath(), pac.getClass().getName());
            ((NotesAnalysisConfig) notesConfiguration).addPlayable(notesVisualization);
        }else if (c instanceof VisualizableConfiguration) {
            vc = (VisualizableConfiguration) c;
            vc.addFile(model.getFile());
            visualizableConfiguration.add(vc);
            NotesVisualization notesVisualization = new NotesVisualization(model.getFile().getAbsolutePath(), vc.getClass().getName());
            ((NotesAnalysisConfig) notesConfiguration).addVisualizable(notesVisualization);// #marca
        } else {
            npac = (NotPlayableAnalyzableConfiguration) c;
            npac.addFile(model.getFile());
            notPlayableConfigurations.add(npac);
        }
        List<AnalyzableConfiguration> analyzableConfigurations = new ArrayList<>(playableConfigurations);
        analyzableConfigurations.addAll(notPlayableConfigurations);
        List<AnalyzableConfiguration> analyzableList = new ArrayList(analyzableConfigurations);
        analyzableList.add(notesConfiguration);
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
                          //config.setupAnalysis(storageFolder, model.getOrg(), p);
                          //config.startAnalysis();
                        }
                    });

                    threads[i].start();
                    threads[i].join();
                    threads[i].interrupt();
                    i++;
                }
                return null;
            }
        };
        int countA = DockablesRegistry.getInstance().getControl().getCDockableCount();
        countA--;
        CControl controlA = DockablesRegistry.getInstance().getControl();
        //mySwingWorker.execute();
        IndexDockable = getIndexNewDockable(countA, controlA);
        System.out.println("Index: " + IndexDockable);
        JPanel panelA = null;
        JPanel panelAux = null;
        if(IndexDockable != null){
            ((DockableElement) controlA.getCDockable(IndexDockable)).setTitleText("testststs");
            //IndexDockable-2 para el analisis??
            DockableElement docka = (DockableElement) controlA.getCDockable(IndexDockable);
            docka.setVisible(false);
            panelA = (JPanel) docka.getContentPane();
            panelA.setSize(200, 200);
            DockableElement docka2 = (DockableElement) controlA.getCDockable(IndexDockable-2);
            docka2.setVisible(false);
            panelAux = (JPanel) docka2.getContentPane();
        }
        List<VisualizableConfiguration> vlista = (List<VisualizableConfiguration>) (List<?>) new ArrayList<>(playableConfigurations);
        vlista.addAll(visualizableConfiguration);
        VisualizationPlayer player = new VisualizationPlayer(vlista);
        player.getDockable().setTitleText("Test222");
        JPanel panelB = (JPanel) player.getDockable().getContentPane();
        
        
        SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode, panelA);
        SwingNode nodo = new SwingNode();
        createSwingContent(nodo, panelB);
        SwingNode nodo2 = new SwingNode();
        createSwingContent(nodo2, panelAux);
        
        StackPane pane = new StackPane();
        pane.getChildren().clear();
        pane.getChildren().add(swingNode);
        
        StackPane pane2 = new StackPane();
        pane2.getChildren().clear();
        pane2.getChildren().add(nodo);
        
        StackPane pane3 = new StackPane();
        pane3.getChildren().clear();
        pane3.getChildren().add(nodo2);
        
        pane.setMinHeight(200);
        pane.setMaxHeight(400);
        pane.setMinWidth(200);
        pane.setMaxWidth(200);
        pane.alignmentProperty().set(Pos.CENTER_RIGHT);
        pane2.setMinHeight(200);
        pane2.setMaxHeight(200);
        pane2.setMinWidth(200);
        pane2.setMaxWidth(200);
        pane3.setMinHeight(200);
        pane3.setMaxHeight(200);
        pane3.setMinWidth(200);
        pane3.setMaxWidth(200);
        //Stage stage = new Stage();
        //stage.setTitle("Swing in JavaFX");
        //stage.setScene(new Scene(pane, 250, 150));
        //stage.show();
        //pane.getChildren().add(nodo);
        List<Node> nodos = gridPane3.getChildren();
        gridPane3.getChildren().removeAll(nodos);
        System.out.println(gridPane3.getRowConstraints().size());
        gridPane3.setPrefSize(600, 600);
        gridPane3.alignmentProperty().set(Pos.CENTER);
        System.out.println("11111");
        if(gridPane.getRowConstraints().size()==0){
            System.out.println("22222");
            for(int i=0; i<3;i++){
                System.out.println("333333");
                RowConstraints aux = new RowConstraints(200);
                aux.setMaxHeight(200);
                aux.setMinHeight(200);
                gridPane3.getRowConstraints().add(aux);
            } 
        }
               
        gridPane3.add(pane, 0, 0);
        gridPane3.add(pane2, 0, 1);
        gridPane3.add(pane3, 0, 2);
        
        projectMenuPane.setCenter(gridPane3);
        
        //centerPane.getChildren().clear();
        //centerPane.getChildren().add(pane);        
    }
    
    public void visualization(MouseEvent event, String id){
        /*VisualizeAction visuAction = new VisualizeAction();
        Participant p = model.getParticipantById(id);
        StageModule sm = null;
        for(StageModule m : model.getOrg().getStages()){
            if(m.getName().equals(model.getStages3().get(0).getName())){
                sm=m;
            }
        }
        visuAction.init(model.getOrg(), p, sm);
        CControl controlA = DockablesRegistry.getInstance().getControl();
        DockablesRegistry.getInstance().getItem();
        JPanel panel = new JPanel();
        System.out.println("Num dockable: " + DockablesRegistry.getInstance().getControl());
        //3 no se que tiene
        DockableElement element  = (DockableElement)controlA.getCDockable(5);
        panel.add(element.getContentPane());
        panel.setBounds(50, 50, 200, 200);
        SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode, panel);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(swingNode);*/
        Participant p = model.getParticipantById(id);
        //File storageFile = new File(model.getOrg().getLocation().getAbsolutePath() + p.folder + "\\capture");
        StageModule sm = null;
        File storageFolder = new File(model.getOrg().getLocation() + "/" + p.folder + "/visualization");
        for(StageModule smAux : model.getOrg().getStages()){
            if(smAux.getName().equals(model.getVisualizationStage().getName())){
                sm=smAux;
            }
        }
        VisualizeActionV2 action = new VisualizeActionV2(model);
        action.init(model.getOrg(), p, sm);
        Configuration c = model.getConfigurationSelected().get(0);
        try {
            c = c.getClass().getConstructor(String.class).newInstance(model.getConfigurationSelected().get(0).getId());
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ProjectMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        int countA = DockablesRegistry.getInstance().getControl().getCDockableCount();
        countA--;
        CControl controlA = DockablesRegistry.getInstance().getControl();
        IndexDockable = getIndexNewDockable(countA, controlA);
        JPanel panelA = null;
        JPanel panelAux = null;
        if(IndexDockable != null){
            ((DockableElement) controlA.getCDockable(IndexDockable)).setTitleText("testststs");
            //IndexDockable-2 para el analisis??
            DockableElement docka = (DockableElement) controlA.getCDockable(IndexDockable);
            docka.setVisible(false);
            panelA = (JPanel) docka.getContentPane();
            panelA.setSize(200, 200);
            DockableElement docka2 = (DockableElement) controlA.getCDockable(IndexDockable-1);
            docka2.setVisible(false);
            panelAux = (JPanel) docka2.getContentPane();
        }
        SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode, panelA);
        SwingNode nodo2 = new SwingNode();
        createSwingContent(nodo2, panelAux);
        
        StackPane pane = new StackPane();
        pane.getChildren().clear();
        pane.getChildren().add(swingNode);
        
        StackPane pane2 = new StackPane();
        pane2.getChildren().clear();
        pane2.getChildren().add(nodo2);
        
        pane.setMinHeight(200);
        pane.setMaxHeight(400);
        pane.setMinWidth(200);
        pane.setMaxWidth(200);
        pane.alignmentProperty().set(Pos.CENTER_RIGHT);
        
        pane2.setMinHeight(200);
        pane2.setMaxHeight(200);
        pane2.setMinWidth(200);
        pane2.setMaxWidth(200);
        
        List<Node> nodos = gridPane3.getChildren();
        gridPane3.getChildren().removeAll(nodos);
        gridPane3.setPrefSize(600, 600);
        gridPane3.alignmentProperty().set(Pos.CENTER);
        if(gridPane.getRowConstraints().size()==0){
            System.out.println("22222");
            for(int i=0; i<3;i++){
                System.out.println("333333");
                RowConstraints aux = new RowConstraints(200);
                aux.setMaxHeight(200);
                aux.setMinHeight(200);
                gridPane3.getRowConstraints().add(aux);
            } 
        }      
        gridPane3.add(pane, 0, 0);
        gridPane3.add(pane2, 0, 1);
        projectMenuPane.setCenter(gridPane3);
    }
    
    private void createSwingContent(SwingNode swingNode, JPanel p) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                //JPanel test = new JPanel();
                swingNode.setContent(p);
            }
        });
    }
    
    private void addView(String action){
        try {
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(ProjectMenuController.class
                    .getResource("/fxml/core/ui/AddParticipant.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle(action);
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
            if(!edit){
                if(participants.size()!= model.getOrg().getParticipants().size()){
                    if(participants.size()==0){
                        List<Node> nodes = gridPane.getChildren();
                        gridPane.getChildren().removeAll(nodes);
                        add(model.getParticipants().get(row));
                    }
                    else{
                        add(model.getParticipants().get(row)); 
                    }
                } 
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ProjectMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Funciones de gestion de iconografia de participantes
    private void initVistaParticipants(){
        int size=20, trasI=25, trasN=50, trasL=250, trasC=280, trasE=310, trasA=340, trasV=370, trasD=400, trasAdd=500; 
        if(model.getParticipants().isEmpty()){
            for(int i=0; i<8; i++){
                RowConstraints aux = new RowConstraints(50);
                aux.setMinHeight(50);
                aux.setMaxHeight(50);
                gridPane.getRowConstraints().add(aux);
            }
            javafx.scene.image.ImageView addCircle = new javafx.scene.image.ImageView("/images/addCircle.png");
            addCircle.setFitHeight(size);
            addCircle.setFitWidth(size);
            addCircle.setTranslateX(trasAdd);
            addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                addParticipant(event);
            });
            gridPane.add(addCircle, 0, row);

            javafx.scene.image.ImageView addPlus = new javafx.scene.image.ImageView("/images/add.png");
            addPlus.setFitHeight(12);
            addPlus.setFitWidth(12);
            addPlus.setTranslateX(trasAdd+4);
            addPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                addParticipant(event);
            });
            gridPane.add(addPlus, 0, row);
            
            Text name = new Text("Participant");
            name.opacityProperty().set(0.5);
            name.setTranslateX(trasN);
            gridPane.add(name, 0, row);

            javafx.scene.image.ImageView lock = new javafx.scene.image.ImageView("/images/unlocked2.png");
            lock.opacityProperty().set(0.5);
            lock.setFitHeight(size);
            lock.setFitWidth(size+5);
            lock.setTranslateX(trasL);
            gridPane.add(lock, 0, row);

            javafx.scene.image.ImageView rec = new javafx.scene.image.ImageView("/images/rec.png");
            rec.opacityProperty().set(0.5);
            rec.setFitHeight(size);
            rec.setFitWidth(size);
            rec.setTranslateX(trasC);
            gridPane.add(rec, 0, row);

            javafx.scene.image.ImageView edit = new javafx.scene.image.ImageView("/images/edit.png");
            edit.opacityProperty().set(0.5);
            edit.setFitHeight(size);
            edit.setFitWidth(size);
            edit.setTranslateX(trasE);
            gridPane.add(edit, 0, row);

            javafx.scene.image.ImageView analysis = new javafx.scene.image.ImageView("/images/analysis.png");
            analysis.opacityProperty().set(0.5);
            analysis.setFitHeight(size);
            analysis.setFitWidth(size);
            analysis.setTranslateX(trasA);
            analysis.setStyle("-fx-background-color: WHITE");
            gridPane.add(analysis, 0, row);

            javafx.scene.image.ImageView visu = new javafx.scene.image.ImageView("/images/capture.png");
            visu.opacityProperty().set(0.5);
            visu.setFitHeight(size);
            visu.setFitWidth(size);
            visu.setTranslateX(trasV);
            gridPane.add(visu, 0, row);

            javafx.scene.image.ImageView delete = new javafx.scene.image.ImageView("/images/delete.png");
            delete.opacityProperty().set(0.5);
            delete.setFitHeight(size);
            delete.setFitWidth(size);
            delete.setTranslateX(trasD);
            gridPane.add(delete, 0, row);

            javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView("/images/participant2.png");
            icon2.opacityProperty().set(0.5);
            icon2.setTranslateX(trasI);
            icon2.setFitHeight(size);
            icon2.setFitWidth(size);
            gridPane.add(icon2, 0, row);
        }
        else{
            for(Participant p : model.getParticipants()){
                gridPane.getChildren().clear();
                add(p);
                row++;
            }
        }
        
    }
    
    private void add(Participant p){
        int size=20, trasI=25, trasN=50, trasL=250, trasC=280, trasE=310, trasA=340, trasV=370, trasD=400, trasAdd=500;
        
        if(row>gridPane.getRowConstraints().size()){
            RowConstraints aux = new RowConstraints(50);
            aux.setMinHeight(50);
            aux.setMaxHeight(50);
            gridPane.getRowConstraints().add(aux);
        }
        if(row==0){
            javafx.scene.image.ImageView addCircle = new javafx.scene.image.ImageView("/images/addCircle.png");
            addCircle.setFitHeight(size);
            addCircle.setFitWidth(size);
            addCircle.setTranslateX(trasAdd);
            addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                addParticipant(event);
            });
            gridPane.add(addCircle, 0, row);

            javafx.scene.image.ImageView addPlus = new javafx.scene.image.ImageView("/images/add.png");
            addPlus.setFitHeight(12);
            addPlus.setFitWidth(12);
            addPlus.setTranslateX(trasAdd+4);
            addPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                addParticipant(event);
            });
            gridPane.add(addPlus, 0, row);
        }
        javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView("/images/participant2.png");
        icon2.setId(p.id);
        icon2.setTranslateX(trasI);
        icon2.setFitHeight(size);
        icon2.setFitWidth(size);
        gridPane.add(icon2, 0, row);
        
        Text name = new Text(p.name);
        name.setTranslateX(trasN);
        gridPane.add(name, 0, row);
        
        javafx.scene.image.ImageView lock = new javafx.scene.image.ImageView("/images/unlocked2.png");
        lock.setId(p.id);
        lock.setFitHeight(size);
        lock.setFitWidth(size+5);
        lock.setTranslateX(trasL);
        lock.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            unLockParticipant(event, lock);
        });
        gridPane.add(lock, 0, row);
        
        javafx.scene.image.ImageView rec = new javafx.scene.image.ImageView("/images/rec.png");
        rec.setId(p.id);
        rec.setFitHeight(size);
        rec.setFitWidth(size);
        rec.setTranslateX(trasC);
        rec.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            record(event, rec.getId());
        });
        gridPane.add(rec, 0, row);
        
        javafx.scene.image.ImageView edit = new javafx.scene.image.ImageView("/images/edit.png");
        edit.setId(p.id);
        edit.setFitHeight(size);
        edit.setFitWidth(size);
        edit.setTranslateX(trasE);
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            editParticipant(event, edit.getId());
        });
        gridPane.add(edit, 0, row);
        
        javafx.scene.image.ImageView analysis = new javafx.scene.image.ImageView("/images/analysis.png");
        analysis.setId(p.id);
        analysis.setFitHeight(size);
        analysis.setFitWidth(size);
        analysis.setTranslateX(trasA);
        analysis.setStyle("-fx-background-color: WHITE");
        analysis.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            analysis(event, analysis.getId());
        });
        gridPane.add(analysis, 0, row);
        
        javafx.scene.image.ImageView visu = new javafx.scene.image.ImageView("/images/capture.png");
        visu.setFitHeight(size);
        visu.setFitWidth(size);
        visu.setTranslateX(trasV);
        visu.setId(p.id);
        visu.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            visualization(event, visu.getId());
        });
        gridPane.add(visu, 0, row);
        
        javafx.scene.image.ImageView delete = new javafx.scene.image.ImageView("/images/delete.png");
        delete.setFitHeight(size);
        delete.setFitWidth(size);
        delete.setTranslateX(trasD);
        delete.setId(p.id);
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            deleteParticipant(event, delete.getId());
        });
        gridPane.add(delete, 0, row); 
        participants.add(p);
        row++;
    }
    
    private void addAgain(){
        List<Node> nodos = gridPane.getChildren();
        gridPane.getChildren().removeAll(nodos);
        row=0;
        for(Participant p : model.getParticipants()){
            System.out.println("a");
            add(p);
        }
    }
    
    //Funciones de gestion de info de Captures
    private void addCapture(MouseEvent event){
        try {
            model.setType(1);
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(ProjectMenuController.class
                    .getResource("/fxml/core/ui/AddPlugin.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle("Add Capture");
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
            String aux = pcca.addConfiguration();
            addConfigsV(aux,0, capturesAux);
            
        } 
        catch (IOException ex) {
            Logger.getLogger(ProjectMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Funciones de gestion de info de Analysis
    private void addAnalysis(MouseEvent event){
        try {
            model.setType(2);
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(ProjectMenuController.class
                    .getResource("/fxml/core/ui/AddPlugin.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle("Add Analysis");
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
            String aux = paca.addConfiguration();
            addConfigsV(aux,1, analysisAux);
        } catch (IOException ex) {
            Logger.getLogger(ProjectMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addVisu(MouseEvent event){
        try {
            model.setType(3);
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(ProjectMenuController.class
                    .getResource("/fxml/core/ui/AddPlugin.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle("Add Visualization");
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
            String aux = pvca.addConfiguration();
            addConfigsV(aux,2, visuAux);
        } catch (IOException ex) {
            Logger.getLogger(ProjectMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Funciones de gestion de iconografia de stages
    private void initVistaCaptures(int type, List<Pair<String,String>> configs){
        System.out.println("a.1");
        int size=20, trasI=25, trasN=50, trasAdd=500;
        if(configs.isEmpty()){ 
            System.out.println("a.1.1");
            for(int i=0; i<8; i++){
                RowConstraints aux = new RowConstraints(50);
                aux.setMinHeight(50);
                aux.setMaxHeight(50);
                gridPane2.getRowConstraints().add(aux);
            }
            
            javafx.scene.image.ImageView addCircle = new javafx.scene.image.ImageView("/images/addCircle.png");
            addCircle.setFitHeight(size);
            addCircle.setFitWidth(size);
            addCircle.setTranslateX(trasAdd);
            
            gridPane2.add(addCircle, 0, row);

            javafx.scene.image.ImageView addPlus = new javafx.scene.image.ImageView("/images/add.png");
            addPlus.setFitHeight(12);
            addPlus.setFitWidth(12);
            addPlus.setTranslateX(trasAdd+4);
            gridPane2.add(addPlus, 0, row);
            
            javafx.scene.image.ImageView icon;
            Text name;
            if(type==0){
                System.out.println("1");
                icon = new javafx.scene.image.ImageView("/images/capture.png");
                name = new Text("Capture");
                addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addCapture(event);
                });
                addPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addCapture(event);
                });
            }
            else if(type==1){
                System.out.println("2");
                icon = new javafx.scene.image.ImageView("/images/analysis.png");
                name = new Text("Analysis");
                addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addAnalysis(event);
                });
                addPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addAnalysis(event);
                });
            }
            else{
                icon = new javafx.scene.image.ImageView("/images/screen.png");
                name = new Text("Visualization");
                addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addVisu(event);
                });
            }
            
            System.out.println("a.1");
            icon.opacityProperty().set(0.5);
            icon.setTranslateX(trasI);
            icon.setFitHeight(size);
            icon.setFitWidth(size);
            gridPane2.add(icon, 0, row);
            
            name.opacityProperty().set(0.5);
            name.setTranslateX(trasN);
            gridPane2.add(name, 0, row);
        }
    }
    
    private void addConfigsV(String nameCapture, int type, List<String> list){
        int size=20, trasI=25, trasN=50, trasAdd=500;
        if(row > gridPane2.getRowConstraints().size()){
            RowConstraints aux = new RowConstraints(50);
            aux.setMinHeight(50);
            aux.setMaxHeight(50);
            gridPane2.getRowConstraints().add(aux);
        }
        if(list.size()<1){
            List<Node> nodes = gridPane2.getChildren();
            gridPane2.getChildren().removeAll(nodes);
            
            javafx.scene.image.ImageView addCircle = new javafx.scene.image.ImageView("/images/addCircle.png");
            addCircle.setFitHeight(size);
            addCircle.setFitWidth(size);
            addCircle.setTranslateX(trasAdd);
            
            gridPane2.add(addCircle, 0, row);

            javafx.scene.image.ImageView addPlus = new javafx.scene.image.ImageView("/images/add.png");
            addPlus.setFitHeight(12);
            addPlus.setFitWidth(12);
            addPlus.setTranslateX(trasAdd+4);
            
            
            if(type==0){
                addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addCapture(event);
                });
                addPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addCapture(event);
                });
            }
            else if(type==1){
                addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addAnalysis(event);
                });
                addPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addAnalysis(event);
                });
            }
            else{
                addCircle.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addVisu(event);
                });
                addPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addVisu(event);
                });
            }
            
            gridPane2.add(addPlus, 0, row);
        }
        

        Text name = new Text(nameCapture);
        name.opacityProperty().set(1);
        name.setTranslateX(trasN);
        gridPane2.add(name, 0, row);
        
        javafx.scene.image.ImageView icon;
        
        if(type==0){
            icon = new javafx.scene.image.ImageView("/images/capture.png");
        }
        else if(type==1){
            icon = new javafx.scene.image.ImageView("/images/analysis.png");
        }
        else{
            icon = new javafx.scene.image.ImageView("/images/screen.png");
        }
        list.add(nameCapture);
        icon.setTranslateX(trasI);
        icon.setFitHeight(size);
        icon.setFitWidth(size);
        gridPane2.add(icon, 0, row);
        row++;
    }
    
    public Integer getIndexNewDockable(int cantPrev, CControl controlPrev) {
        Integer IndexDockable = null;
        int countB = DockablesRegistry.getInstance().getControl().getCDockableCount();
        CControl controlB = DockablesRegistry.getInstance().getControl();
        
        System.out.println("countB: " + countB);
        System.out.println("cantPrev: " + cantPrev);
        if (countB > cantPrev) {
            System.out.println("1");
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
                System.out.println("j1: " + j);
                if (!idsA.contains(idB)) {
                    System.out.println("j2: " + j);
                    IndexDockable = j;
                    break;
                }
            }
        }
        return IndexDockable;
    }
}