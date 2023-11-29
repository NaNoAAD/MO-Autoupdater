package mo.updating.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    /**
     * Metodo del controlador que permite el cerrado del launcher y la apertura de MO en caso de que el usuario seleccione no actualizar
     * @param event
     */
    @FXML
    private void cancelUpdate(ActionEvent event){
        System.out.println("Cerrando app");
        Stage stage = (Stage) this.noButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void acceptUpdate(ActionEvent event){
        System.out.println("Abriendo vista Updating");
        Stage stage = (Stage) this.yesButton.getScene().getWindow();
        stage.close();
        //Se hace apertura de la vista de actualizacion en progreso
        try {
            // Se carga FXML con vista de confirmacion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../visual/Updating.fxml"));
            Parent root = loader.load();
            //Se carga nuevo controlador (Si es necesario algun procedimiento a priori)
            //ConfirmationController controller = loader.getController();

            // Se configura la nueva escena
            Scene scene = new Scene(root);

            // Se crea un nuevo Stage para la segunda vista
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Actualizando...");
            newStage.setResizable(false);

            // Mostrar la nueva vista
            newStage.show();    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
