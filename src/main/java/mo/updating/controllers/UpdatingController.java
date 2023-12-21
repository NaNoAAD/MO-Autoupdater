package mo.updating.controllers;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import mo.updating.updater;
import mo.updating.updaterArguments;
import mo.updating.updaterLogic;

/**
 * Controlador de la vista de proceso de actualizacion
 */
public class UpdatingController {
    
    @FXML
    private ProgressBar progress;

    @FXML
    private void initialize() throws IOException{
        System.out.println("(UpdatingController.java) - Inicializando Vista de Actualizacion en progreso");
        progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        //Ejecutamos las operaciones logicas de actualizacion en un hilo de fondo
        //Para evitar que bloqueen la responsividad de la animcacion de la barra de progreso indeterminada
        CompletableFuture.runAsync(() -> {
            //Asegurandonos que la pantalla y la barra de progreso ya estan mostradas, se procede con el procedimiento de actualizacion
            // Se obtienen los permisos de los procesos anteriores de comparacion gracias a su naturaleza Static
            Boolean permissionBoolean = SplashScreenController.getPermission1Obtained();
            Boolean answerBoolean = SplashScreenController.getAnswerObtained();
            
            //Usamos la logica responsable de actualizacion
            //updaterLogic.updaterUpdatingLogic(permissionBoolean, answerBoolean, "https://github.com/NaNoAAD/MO-Autoupdater/archive/refs/heads/master.zip", 
              //  "./", "./Repo.zip", "../", "./");

            updaterLogic.updaterUpdatingLogic(permissionBoolean, answerBoolean, updaterArguments.getDownloadLinkZip(), updaterArguments.getTargetDirectoryToMoveZip(), 
            updaterArguments.getZipDownloadedPath(), updaterArguments.getTargetDirectoryToExtract(), updaterArguments.getPathToExecuteWrapperGradle());

            System.out.println("(UpdatingController.java) - Abriendo MO - Terminando Launcher ");

            // Se abre MO
            updater.openMO();

            // Cierra la vista en el hilo de JavaFX y se cierra la app
            Platform.runLater(() -> closeStage());
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

}  //Fin controller
