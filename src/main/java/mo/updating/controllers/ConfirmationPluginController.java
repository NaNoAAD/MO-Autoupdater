package mo.updating.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import mo.updating.updater;
import mo.updating.updaterArguments;
import mo.updating.updaterPluginsUpdating;
import mo.updating.updaterRegisterComparison;
import mo.updating.updaterVersionNotesRegister;

/**
 * Controlador del splasher inicial del Launcher
 */
public class ConfirmationPluginController implements Initializable{

    @FXML
    private Button noButton;

    @FXML
    private Button yesButton;

    @FXML
    private TextArea newNotesVersion;

    // Lógica de inicialiacion de la vista de confirmacion
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("(ConfirmationPluginController.java) - Inicializando Vista de confirmacion de plugin");
        // Establecer el TextArea newNotesVersion como de solo lectura
        String notes = "";
        try {
            //Se obtienen las notas de version remotas y se muestran en la vista
            //notes = updaterVersionNotesRegister.getRemoteVersionNotes("ghp_0D6Zmt", "4sfGEZJzK7Fiutyfj6J", 
            //"DizVO3CK3zW", "https://raw.githubusercontent.com/NaNoAAD/MO-Autoupdater/master/versionNotes.txt");
            notes = updaterVersionNotesRegister.getRemoteVersionNotes(updaterArguments.getAToken(), updaterArguments.getBToken(), updaterArguments.getCToken(),
                 updaterArguments.getRemoteNotesApiUrl());
            //Se solocan las notas obtenidas en la vista
            newNotesVersion.setText(notes);
            newNotesVersion.setEditable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Metodo del controlador que permite el cerrado del launcher y la apertura de MO en caso de que el usuario seleccione no actualizar
     * @param event
     */
    @FXML
    private void cancelUpdate(ActionEvent event) throws IOException {
        if(updaterPluginsUpdating.loopRevisorPluginsToUpdate()){
            //si y solo si hay archivos up disponibles, se carga la nueva vista de confirmationPlugin para el proximo plugin si hay disponible

            //Se cierra el stage actual
            Stage stage = (Stage) this.noButton.getScene().getWindow();
            stage.close();

            //Se abre la vista
            //Platform.runLater(() -> loadConfirmationPluginView());
            loadConfirmationPluginView();
            
        } else {
            System.out.println("(ConfirmationController.java) - Eliminando Archivo remoto obtenido y cerrando app");
            updaterRegisterComparison.deleteFilesIfNotPermission(false, false, Paths.get("./RemoteRegister.txt") );
            Stage stage = (Stage) this.noButton.getScene().getWindow();
            stage.close();
            updater.openMO();
        }
        
    }

    /**
     * Metodo que abre la vista que muestra el progreso de la actualizacion de MO
     * @param event
     */
    @FXML
    private void acceptUpdate(ActionEvent event){
        System.out.println("(ConfirmationPluginController.java) - Abriendo vista UpdatingPlugin");
        Stage stage = (Stage) this.yesButton.getScene().getWindow();
        stage.close();
        //Se hace apertura de la vista de actualizacion en progreso
        try {
            // Se carga FXML con vista de confirmacion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/java/mo/updating/visual/UpdatingPlugin.fxml"));
            Parent root = loader.load();
            //Se carga nuevo controlador (Si es necesario algun procedimiento a priori)
            //ConfirmationController controller = loader.getController();

            // Se configura la nueva escena
            Scene scene = new Scene(root);

            //Se instancia el controller par usar el metodo propio de Updating
            //UpdatingController controller = loader.getController();

            //Con los recursos listos y mostrados, al igual que en SplashController se ejecuta metodo
            //controller.secondPlaneUpdating(stage);



            // Se crea un nuevo Stage para la segunda vista
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Actualizando...");
            newStage.setResizable(false);

            // Mostrar la nueva vista
            newStage.show();    

            //Con los recursos listos y mostrados, al igual que en SplashController se ejecuta metodo
            //controller.secondPlaneUpdating(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadConfirmationPluginView(){
        try {
            // Se carga FXML con vista de confirmacion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/main/java/mo/updating/visual/ConfirmacionPlugin.fxml"));
            Parent root = loader.load();
            //Se carga nuevo controlador (Si es necesario algun procedimiento a priori)
            //ConfirmationController controller = loader.getController();

            // Se configura la nueva escena
            Scene scene = new Scene(root);

            // Se crea un nuevo Stage para la segunda vista
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Actualizacion para Plugin encontrada");
            newStage.setResizable(false);

            // Mostrar la nueva vista
            newStage.show();   

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
