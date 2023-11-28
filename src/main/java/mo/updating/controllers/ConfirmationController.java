package mo.updating.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import mo.updating.updaterVersionNotesRegister;

/**
 * Controlador del splasher inicial del Launcher
 */
public class ConfirmationController {

    @FXML
    private Button noButton;

    @FXML
    private Button yesButton;

    @FXML
    private TextArea newNotesVersion;

    // Lógica y métodos para el SplashScreen
    @FXML
    private void initialize() throws IOException {
        System.out.println("Ahora en primera Vista de confirmacion");
        // Establecer el TextArea newNotesVersion como de solo lectura
        String notes = getVersionNotesUI();
        //Se obtienen las notas de version remotas y se muestran en la vista
        //this.newNotesVersion.setCellValueFactory(new PropertyValueFactory("nombre"));
        
    }

    private String getVersionNotesUI() throws IOException{
        return updaterVersionNotesRegister.getRemoteVersionNotes();
    }

    @FXML
    private void cancelUpdate(ActionEvent event){
        System.out.println("Cerrando app");
        Stage stage = (Stage) this.noButton.getScene().getWindow();
        stage.close();
    }

}
