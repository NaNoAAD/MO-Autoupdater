/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.core.v2.model.Organization;
import mo.organization.Participant;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class ProjectParticipantsController implements Initializable {
    private final String CONTROLLER_KEY = "controller";
    
    
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
    Organization model;
    private List<Participant> participants = new ArrayList<>();
    int result = 4;
    int row = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        iniciar();
    } 
    
    @FXML
    private void addParticipant(MouseEvent event) {
        add(new Participant());
    }

    @FXML
    private void unLockParticipant(MouseEvent event) {
        System.out.println("un/Lock");
    }


    @FXML
    private void edit(MouseEvent event) {
        System.out.println("Edit");
    }

    
    private void iniciar(){
        iconParticipant.opacityProperty().set(0.50);
        textParticipant.opacityProperty().set(0.50);
        addCircle.opacityProperty().set(0.50);
        addPlus.opacityProperty().set(0.50);
        lockParticipant.opacityProperty().set(0.50);
        recordParticipant.opacityProperty().set(0.50);
        editParticipant.opacityProperty().set(0.50);
        deleteParticipant.opacityProperty().set(0.50);
        visulizationParticipant.opacityProperty().set(0.50);
        
    }

    @FXML
    private void delete(MouseEvent event) {
        System.out.println("Delete");
    }
    
    private void add(Participant p){
        if(row>gridPaneParticipants.getRowConstraints().size()){
            gridPaneParticipants.addRow(row, null);
        }
        Stage popUp = new Stage();
        Button btnCancel, btnAdd;
        Label lbId, lbName, lbDate, lbDescription;
        btnCancel = new Button("Cancel");
        btnAdd = new Button("Add");
        int numAux = model.getParticipants().size()+1;
        TextField id = new TextField(Integer.toString(numAux));
        TextField name = new TextField();
        DatePicker date = new DatePicker();
        TextArea description = new TextArea();
        btnCancel.setOnAction(e-> popUp.close());
        btnAdd.setOnAction(e-> buttonModal(e, id.getText(), name.getText(), date.getValue(), description.getText(), 1, popUp));
        btnAdd.setLayoutX(btnCancel.getLayoutX());
        btnAdd.setTranslateX(50);
        lbId = new Label("Id: ");
        lbName = new Label("Name: ");
        lbDate = new Label("Date: ");
        lbDescription = new Label("Notes: ");
        popUp.initModality(Modality.APPLICATION_MODAL);
        VBox pop = new VBox(20);
        pop.setStyle("-fx-padding:10px;");
        pop.getChildren().addAll(new Text("New participant"), lbId, id, lbName, name, lbDate, date, lbDescription, description, btnAdd, btnCancel);
        Scene popUp1 = new Scene(pop, 300, 600);
        popUp.setTitle("New participant");
        popUp.setScene(popUp1);
        popUp.showAndWait();
        if(result==1){
            System.out.println(model.getParticipants().size());
            if(model.getParticipants().size()==1){
            //Participant p = new Participant();
            iconParticipant.opacityProperty().set(1);
            textParticipant.opacityProperty().set(1);
            addCircle.opacityProperty().set(1);
            addPlus.opacityProperty().set(1);
            lockParticipant.opacityProperty().set(1);
            recordParticipant.opacityProperty().set(1);
            editParticipant.opacityProperty().set(1);
            deleteParticipant.opacityProperty().set(1);
            textParticipant.setText(participants.get(0).name);
            visulizationParticipant.opacityProperty().set(1);
            row++;
            }
            else{
                System.out.println(iconParticipant.getStyle());
                javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView();
                icon2.setImage(iconParticipant.getImage());
                icon2.setId("icon"+p.id);
                icon2.setFitHeight(20);
                icon2.setFitWidth(20);
                icon2.setTranslateX(25);
                //icon2.setPadding(new Insets(25, 25, 25, 25));
                //icon2.setStyle(iconParticipant.getStyle());
                gridPaneParticipants.add(icon2, 0, row);
                Text text2 = new Text(model.getParticipants().get(model.getParticipants().size()-1).name);
                text2.setTranslateX(50);
                gridPaneParticipants.add(text2,0,row);
                ImageView lock2 = new ImageView();
                lock2.setImage(lockParticipant.getImage());
                lock2.setFitHeight(20);
                lock2.setFitWidth(20);
                lock2.setId("lock"+p.id);
                lock2.setTranslateX(125);
                //lock2.setEventDispatcher(lockParticipant.getEventDispatcher());
                lock2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    unLockParticipant(event);
                });
                gridPaneParticipants.add(lock2,0,row);
                javafx.scene.image.ImageView record2 = new javafx.scene.image.ImageView();
                record2.setImage(recordParticipant.getImage());
                record2.setId("record"+p.id);
                record2.setFitHeight(20);
                record2.setFitWidth(20);
                record2.setTranslateX(152);
                record2.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
                    record(event);
                });
                gridPaneParticipants.add(record2,0,row);
                javafx.scene.image.ImageView edit2 = new javafx.scene.image.ImageView();
                edit2.setId("edit"+p.id);
                edit2.setImage(editParticipant.getImage());
                edit2.setFitHeight(20);
                edit2.setFitWidth(20);
                edit2.setTranslateX(182);
                edit2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    edit(event);
                });
                gridPaneParticipants.add(edit2,0,row);
                javafx.scene.image.ImageView vizu2 = new javafx.scene.image.ImageView();
                vizu2.setId("vizu"+p.id);
                vizu2.setImage(visulizationParticipant.getImage());
                vizu2.setFitHeight(20);
                vizu2.setFitWidth(20);
                vizu2.setTranslateX(212);
                vizu2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    seeVisualizations(event);
                });
                gridPaneParticipants.add(vizu2,0,row);
                javafx.scene.image.ImageView delete2 = new javafx.scene.image.ImageView();
                delete2.setId("delete"+p.id);
                delete2.setImage(deleteParticipant.getImage());
                delete2.setFitHeight(20);
                delete2.setFitWidth(20);
                delete2.setTranslateX(238);
                delete2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    delete(event);
                });
                gridPaneParticipants.add(delete2,0,row);
                row++;   
            }
        }
    }
    
    private void buttonModal(ActionEvent e, String id ,String name, LocalDate date, String notes, int type, Stage popUp){
        Participant pAux = new Participant();
        pAux.id = id;
        pAux.name = name;
        Instant instant = Instant.from(date.atStartOfDay(ZoneId.systemDefault()));
        Date dateAux = Date.from(instant);
        pAux.date = dateAux;
        pAux.notes = notes;
        pAux.isLocked = false;
        pAux.folder = model.getFileProject().getPath();
        participants.add(pAux);
        model.getOrg().addParticipant(pAux);
        model.setParticipants(participants);
        result = 1;
        System.out.println("Agregado :D");
        popUp.close(); 
    }

    @FXML
    private void seeVisualizations(MouseEvent event) {
        System.out.println("See");
    }

    @FXML
    private void record(MouseEvent event) {
        System.out.println("Record");
    }
}
