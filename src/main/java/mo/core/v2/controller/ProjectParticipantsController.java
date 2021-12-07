/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mo.core.v2.model.Organization;
import mo.organization.Participant;
import mo.organization.visualization.tree.OrganizationDockable;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectParticipantsController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    private static final Logger LOGGER = Logger.getLogger(OrganizationDockable.class.getName());
    
    @FXML
    private ScrollPane scrollPaneParticipants;
    @FXML
    private GridPane gridPaneParticipants;
    @FXML
    private ImageView iconParticipant;
    @FXML
    private Text textParticipant;
    @FXML
    private Circle addCircle;
    @FXML
    private ImageView addPlus;
    @FXML
    private ImageView lockParticipant;
    @FXML
    private ImageView recordParticipant;
    @FXML
    private ImageView editParticipant;
    @FXML
    private ImageView deleteParticipant;
    @FXML
    private ImageView visulizationParticipant;
    @Inject
    Injector injector;
    @Inject
    Organization model;
    private List<Participant> participants = new ArrayList<>();
    int result = 0;
    int row = 0;
    int aux = -1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(!model.getParticipants().isEmpty()){
            participants = model.getParticipants();
        }
        iniciar();
    } 
    
    @FXML
    private void addParticipant(MouseEvent event) {
        addView("New Participant");        
    }

    private void unLockParticipant(MouseEvent event, ImageView i) {
        model.getOrg().getParticipants().forEach((participant) -> {
            if(participant.id.equals(i.getId())){
                if(participant.isLocked){
                    participant.isLocked=false;
                    ImageView iAux = new javafx.scene.image.ImageView("/images/unlocked2.png");
                    i.setImage(iAux.getImage());
                    model.setParticipants(model.getOrg().getParticipants());
                    
                }
                else{
                   participant.isLocked=true;
                   ImageView iAux = new javafx.scene.image.ImageView("/images/locked2.png");
                   i.setImage(iAux.getImage());
                   model.setParticipants(model.getOrg().getParticipants());
                }
            }
        });
    }


    private void edit(MouseEvent event, String id) {
        for(Participant p : model.getParticipants()){
            if(p.id.equals(id)){
                model.setpSelected(p);
                addView("Edit Participant");
                p.name = model.getpSelected().name;
                p.notes = model.getpSelected().notes;
                deleteOfGrid();
                addAgain2();
                break;
            }
        }
    }

    
    private void iniciar(){
        if(model.getParticipants().isEmpty()){
            iconParticipant.opacityProperty().set(0.50);
            textParticipant.opacityProperty().set(0.50);
            lockParticipant.opacityProperty().set(0.50);
            recordParticipant.opacityProperty().set(0.50);
            editParticipant.opacityProperty().set(0.50);
            deleteParticipant.opacityProperty().set(0.50);
            visulizationParticipant.opacityProperty().set(0.50);
            if(result == 1){
                textParticipant.setText("Participant");
                gridPaneParticipants.add(addCircle,0,row);
                gridPaneParticipants.add(addPlus,0,row);
                gridPaneParticipants.add(iconParticipant, 0, row);
                gridPaneParticipants.add(textParticipant, 0, row);
                gridPaneParticipants.add(lockParticipant, 0, row);
                gridPaneParticipants.add(recordParticipant, 0, row);
                gridPaneParticipants.add(editParticipant, 0, row);
                gridPaneParticipants.add(deleteParticipant, 0, row);
                gridPaneParticipants.add(visulizationParticipant, 0, row);
                
            }
        }
        else{
            addAgain(0);
        } 
    }

    private void delete(MouseEvent event, String id) {
        System.out.println("in delet");
        aux=-1;
        participants = model.getParticipants();
        for(Participant p : participants){
            aux++;
            if(p.id.equals(id)){
                //File f = new File(model.getOrg().getLocation()+"/"+p.folder);
                //deleteDirectory(f);
                model.getOrg().deleteParticipant(p);
                model.getOrg().store();
                model.getParticipants().remove(p);
                break;
            }
        }
        participants = model.getParticipants();
        deleteOfGrid();
    }
    
    private void add(Participant p, int n){
        if(row>gridPaneParticipants.getRowConstraints().size()){
            gridPaneParticipants.addRow(row, null);
        }
        if(model.getParticipants().size()==1 && n==0){
            //Participant p = new Participant();
            iconParticipant.opacityProperty().set(1);
            iconParticipant.setId(p.id);
            textParticipant.opacityProperty().set(1);
            textParticipant.setId(p.id);
            textParticipant.setText(p.name);
            lockParticipant.opacityProperty().set(1);
            lockParticipant.setId(p.id);
            lockParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                unLockParticipant(event, lockParticipant);
            });
            recordParticipant.opacityProperty().set(1);
            recordParticipant.setId(p.id);
            
            editParticipant.opacityProperty().set(1);
            editParticipant.setId(p.id);
            editParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                edit(event, editParticipant.getId());
            });
            deleteParticipant.opacityProperty().set(1);
            deleteParticipant.setId(p.id);
            deleteParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                delete(event, deleteParticipant.getId());
            }); 
            visulizationParticipant.opacityProperty().set(1);
            visulizationParticipant.setId(p.id);
            visulizationParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                seeVisualizations(event, visulizationParticipant.getId());
            });
            row++;
            model.getOrg().getParticipants().add(model.getParticipants().get(0));
            model.getOrg().store();
        }
        else if(n==1){
            javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView("/images/participant2.png");
            icon2.setId(p.id);
            icon2.setFitHeight(20);
            icon2.setFitWidth(20);
            icon2.setTranslateX(25);
            gridPaneParticipants.add(icon2, 0, row);
            Text text2 = new Text(p.name);
            text2.setTranslateX(50);
            gridPaneParticipants.add(text2,0,row);
            ImageView lock2 = new ImageView();
            lock2.setImage(lockParticipant.getImage());
            lock2.setFitHeight(20);
            lock2.setFitWidth(20);
            lock2.setId(p.id);
            lock2.setTranslateX(125);
            lock2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                unLockParticipant(event, lock2);
            });
            gridPaneParticipants.add(lock2,0,row);
            javafx.scene.image.ImageView record2 = new javafx.scene.image.ImageView();
            record2.setImage(recordParticipant.getImage());
            record2.setId(p.id);
            record2.setFitHeight(20);
            record2.setFitWidth(20);
            record2.setTranslateX(152);
            record2.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
                record(event);
            });
            gridPaneParticipants.add(record2,0,row);
            javafx.scene.image.ImageView edit2 = new javafx.scene.image.ImageView();
            edit2.setId(p.id);
            edit2.setImage(editParticipant.getImage());
            edit2.setFitHeight(20);
            edit2.setFitWidth(20);
            edit2.setTranslateX(182);
            edit2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                edit(event, edit2.getId());
            });
            gridPaneParticipants.add(edit2,0,row);
            javafx.scene.image.ImageView vizu2 = new javafx.scene.image.ImageView();
            vizu2.setId(p.id);
            vizu2.setImage(visulizationParticipant.getImage());
            vizu2.setFitHeight(20);
            vizu2.setFitWidth(20);
            vizu2.setTranslateX(212);
            vizu2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                seeVisualizations(event, vizu2.getId());
            });
            gridPaneParticipants.add(vizu2,0,row);
            javafx.scene.image.ImageView delete2 = new javafx.scene.image.ImageView();
            delete2.setId(p.id);
            delete2.setImage(deleteParticipant.getImage());
            delete2.setFitHeight(20);
            delete2.setFitWidth(20);
            delete2.setTranslateX(238);
            delete2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                delete(event,delete2.getId());
            });
            gridPaneParticipants.add(delete2,0,row);            
            row++;
        }
        else {
            int numAux = model.getParticipants().size()-1;
            int numAux2 = model.getOrg().getParticipants().size()-1;
            if(!model.getParticipants().get(numAux).equals(model.getOrg().getParticipants().get(numAux2))){
                javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView();
                icon2.setImage(iconParticipant.getImage());
                icon2.setId(p.id);
                icon2.setFitHeight(20);
                icon2.setFitWidth(20);
                icon2.setTranslateX(25);
                //icon2.setPadding(new Insets(25, 25, 25, 25));
                //icon2.setStyle(iconParticipant.getStyle());
                gridPaneParticipants.add(icon2, 0, row);
                Text text2 = new Text(p.name);
                text2.setTranslateX(50);
                gridPaneParticipants.add(text2,0,row);
                ImageView lock2 = new ImageView();
                lock2.setImage(lockParticipant.getImage());
                lock2.setFitHeight(20);
                lock2.setFitWidth(20);
                lock2.setId(p.id);
                lock2.setTranslateX(125);
                lock2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    unLockParticipant(event, lock2);
                });
                gridPaneParticipants.add(lock2,0,row);
                javafx.scene.image.ImageView record2 = new javafx.scene.image.ImageView();
                record2.setImage(recordParticipant.getImage());
                record2.setId(p.id);
                record2.setFitHeight(20);
                record2.setFitWidth(20);
                record2.setTranslateX(152);
                record2.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
                    record(event);
                });
                gridPaneParticipants.add(record2,0,row);
                javafx.scene.image.ImageView edit2 = new javafx.scene.image.ImageView();
                edit2.setId(p.id);
                edit2.setImage(editParticipant.getImage());
                edit2.setFitHeight(20);
                edit2.setFitWidth(20);
                edit2.setTranslateX(182);
                edit2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    edit(event, edit2.getId());
                });
                gridPaneParticipants.add(edit2,0,row);
                javafx.scene.image.ImageView vizu2 = new javafx.scene.image.ImageView();
                vizu2.setId(p.id);
                vizu2.setImage(visulizationParticipant.getImage());
                vizu2.setFitHeight(20);
                vizu2.setFitWidth(20);
                vizu2.setTranslateX(212);
                vizu2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    seeVisualizations(event, vizu2.getId());
                });
                gridPaneParticipants.add(vizu2,0,row);
                javafx.scene.image.ImageView delete2 = new javafx.scene.image.ImageView();
                delete2.setId(p.id);
                delete2.setImage(deleteParticipant.getImage());
                delete2.setFitHeight(20);
                delete2.setFitWidth(20);
                delete2.setTranslateX(238);
                delete2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    delete(event,delete2.getId());
                });
                gridPaneParticipants.add(delete2,0,row);
                row++;
            }
            model.getOrg().getParticipants().add(model.getParticipants().get(numAux));
            model.getOrg().store();
        }
    }

    private void seeVisualizations(MouseEvent event, String id) {
        System.out.println("See");
    }

    @FXML
    private void record(MouseEvent event) {
        System.out.println("Record");
    }
    
    private static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        boolean result = false;
        try {
            result = path.delete();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, (Supplier<String>) ex);
        }

        return result;
    }
    
    private void addView(String action){
        try{
            Stage popUp = new Stage();
            final JavaFXBuilderFactory builderFactory = new JavaFXBuilderFactory();
            final Callback<Class<?>, Object> callback = (clazz) -> injector.getInstance(clazz);
            FXMLLoader loaderOpen = new FXMLLoader(ProjectParticipantsController.class
                    .getResource("/fxml/core/ui/AddParticipant.fxml"), null,
                    builderFactory, callback);
            Parent openParent = loaderOpen.load();
            openParent.getProperties()
                    .put(CONTROLLER_KEY, loaderOpen.getController());
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle(action);
            popUp.setScene(new Scene(openParent));
            popUp.showAndWait();
            if(participants.size()!=model.getParticipants().size())
                add(model.getParticipants().get(row),0);
        }
        catch(IOException ex){
            Logger.getLogger(ProjectParticipantsController.class
          .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void deleteOfGrid(){

        List<Node> nodes = gridPaneParticipants.getChildren();
        gridPaneParticipants.getChildren().removeAll(nodes);
        gridPaneParticipants.getChildren().removeAll(nodes);
        row = 0;
        if(model.getParticipants().size()==0){
            result = 1;
            iniciar();
        }
        else{
            addAgain(1);
        }
    }
    
    private void addAgain(int n){
        if(!model.getParticipants().isEmpty()){
            for(Participant p : model.getParticipants()){
                if(row==0){
                    if(n==1){
                        gridPaneParticipants.add(addCircle,0,row);
                        gridPaneParticipants.add(addPlus,0,row);
                    }
                    iconParticipant.opacityProperty().set(1);
                    iconParticipant.setId(p.id);
                    gridPaneParticipants.add(iconParticipant, 0, row);
                    textParticipant.opacityProperty().set(1);
                    textParticipant.setId(p.id);
                    textParticipant.setText(p.name);
                    gridPaneParticipants.add(textParticipant, 0, row);
                    lockParticipant.opacityProperty().set(1);
                    lockParticipant.setId(p.id);
                    lockParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        unLockParticipant(event, lockParticipant);
                    });
                    gridPaneParticipants.add(lockParticipant, 0, row);
                    recordParticipant.opacityProperty().set(1);
                    recordParticipant.setId(p.id);
                    gridPaneParticipants.add(recordParticipant, 0, row);
                    editParticipant.opacityProperty().set(1);
                    editParticipant.setId(p.id);
                    editParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        edit(event, editParticipant.getId());
                    });
                    gridPaneParticipants.add(editParticipant, 0, row);
                    deleteParticipant.opacityProperty().set(1);
                    deleteParticipant.setId(p.id);
                    deleteParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        delete(event, deleteParticipant.getId());
                    });
                    gridPaneParticipants.add(deleteParticipant, 0, row);
                    visulizationParticipant.opacityProperty().set(1);
                    visulizationParticipant.setId(p.id);
                    visulizationParticipant.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        seeVisualizations(event, visulizationParticipant.getId());
                    });
                    gridPaneParticipants.add(visulizationParticipant, 0, row);
                    row++;
                }
                else{
                    add(p,1);
                }

            }
        }
        else{
            iniciar();
        }
        
    }
    
    private void addAgain2(){
        row = 0;
        for(Participant p : model.getParticipants()){
            add(p,1);
        }
    }
}
