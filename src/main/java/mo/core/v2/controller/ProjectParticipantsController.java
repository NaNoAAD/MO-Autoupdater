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
import javafx.geometry.Insets;
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
    private Circle recordParticipant;
    @FXML
    private ImageView editParticipant;
    @FXML
    private ImageView deleteParticipant;
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
    }

    @FXML
    private void recParticipant(MouseEvent event) {
    }

    @FXML
    private void edit(MouseEvent event) {
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
        
    }

    @FXML
    private void delete(MouseEvent event) {
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
        int numAux = model.getParticipantsAll().size()+1;
        TextField id = new TextField(Integer.toString(numAux));
        TextField name = new TextField();
        DatePicker date = new DatePicker();
        TextArea description = new TextArea();
        btnCancel.setOnAction(e-> popUp.close());
        btnAdd.setOnAction(e-> buttonModal(e, id.getText(), name.getText(), date.getValue(), description.getText(), 1, popUp));
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
            System.out.println(model.getParticipantsAll().size());
            if(model.getParticipantsAll().size()==1){
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
            row++;
            }
            else{
                System.out.println(iconParticipant.getStyle());
                javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView();
                icon2.setImage(iconParticipant.getImage());
                icon2.setId("icon2");
                icon2.setFitHeight(20);
                icon2.setFitWidth(20);
                icon2.setStyle("-fx-margin{-fx-padding-left: 25px;}");
                //icon2.setPadding(new Insets(25, 25, 25, 25));
                //icon2.setStyle(iconParticipant.getStyle());
                gridPaneParticipants.add(icon2, 0, row);
                Text text2 = new Text(participants.get(participants.size()-1).name);
                text2.setStyle("text-with-margin{-fx-padding-left: 50px;}");
                gridPaneParticipants.add(text2,0,1);
                ImageView lock2 = new ImageView();
                lock2.setImage(lockParticipant.getImage());
                lock2.setId("lock2");
                lock2.setStyle("-fx-margin-left:25px;");
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
        model.setParticipantsAll(participants);
        result = 1;
        System.out.println("Agregado :D");
        popUp.close(); 
    }
}
