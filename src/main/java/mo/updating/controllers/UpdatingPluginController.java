package mo.updating.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import mo.updating.updater;
import mo.updating.updaterArguments;
import mo.updating.updaterLogic;
import mo.updating.updaterPluginsUpdating;
import mo.updating.updaterPostUpdateProcesses;

/**
 * Controlador de la vista de proceso de actualizacion
 */
public class UpdatingPluginController {
    
    @FXML
    private ProgressBar progress;

    @FXML
    private void initialize() throws IOException{
        System.out.println("(UpdatingPluginController.java) - Inicializando Vista de Actualizacion en progreso");
        progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        //Ejecutamos las operaciones logicas de actualizacion en un hilo de fondo
        //Para evitar que bloqueen la responsividad de la animcacion de la barra de progreso indeterminada
        CompletableFuture.runAsync(() -> {
            //Asegurandonos que la pantalla y la barra de progreso ya estan mostradas, se procede con el procedimiento de actualizacion

            updaterLogic.updaterUpdatingLogic(true, true, updaterArguments.getDownloadLinkZip(), updaterArguments.getTargetDirectoryToMoveZip(), 
            updaterArguments.getZipDownloadedPath(), updaterArguments.getTargetDirectoryToExtract(), updaterArguments.getPathToExecuteWrapperGradle());
            try {
                //Con el plugin actualizado, se intenta mover el jar generado a la carpeta de plugins de MO y a eliminar la carpeta extraida

                updaterPluginsUpdating.moveJarToPluginsFolder("./ups/" + updaterArguments.getJarLocationInRepository() + "/" + updaterArguments.getBuildedJarFileName(), "./build/libs/plugins/" + updaterArguments.getBuildedJarFileName());

                //Se actualiza desde lo descargado, el registro local y el indicador de version local
                updaterPostUpdateProcesses.updatingLocalRegisterAndLocalVersionPlugin(updaterArguments.getPathToExecuteWrapperGradle(), updaterArguments.getPathToPluginRegisterFile(), updaterArguments.getLocalVersionString());

                updaterPostUpdateProcesses.deleteLeftoversFilesPlugin(updaterArguments.getRepositoryName());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Una vez que la actualizacion de mo termino, si hay upfiles que revisar, entonces es el turno de los plugins
            //Y para ello se vuelve a abrir la vista de confirmacion pero antes! se actualizan las variables globales del updater
            if(updaterPluginsUpdating.loopRevisorPluginsToUpdate()){
                //si y solo si hay archivos up disponibles, se carga la vista de confirmationPlugin
                loadConfirmationPluginView();
            } else {
                //Si simplemente no habian archivos .up identificados, simplemente procedemos a cerrar el updater
                System.out.println("(UpdatingController.java) - Abriendo MO - Terminando Launcher ");
                // Se abre MO
                updater.openMO();
                // Cierra la vista en el hilo de JavaFX y se cierra la app
                Platform.runLater(() -> closeStage());
            }

            
        });
    }

    /**
     * Metodo interno del conrolador que cierra la vista y tambien la app
     */
    private void closeStage() {
        // Obtén el Stage asociado a la vista a traves de la barra de progreso
        Stage stage = (Stage) progress.getScene().getWindow();
        // Cerrado del Stage (y la vista)
        stage.close();
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

}  //Fin controller
