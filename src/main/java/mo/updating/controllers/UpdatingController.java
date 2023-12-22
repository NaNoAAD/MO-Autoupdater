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
import mo.updating.updaterPluginsUpdating;

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

            //Una vez que la actualizacion de mo termino, si hay upfiles que revisar, entonces es el turno de los plugins
            //Y para ello se vuelve a abrir la vista de confirmacion pero antes! se actualizan las variables globales del updater
            if (updaterPluginsUpdating.upFile.size() != 0) {
                //Actualizacion de las variables globales
                //Se obtiene el primer string de upFile, que es un String con la direccion relativa de un .up de la carpeta /ups
                ///Con esta ruta, se trabaja el archivo del cual se obtienen las nuevas variables 
                ////con estas variables se hace set de las variables globales del updater 
                updaterArguments.setArguments(updaterArguments.saveArguments(updaterPluginsUpdating.upFile.get(0)));
                System.out.println("(UpdatingController.java) - Variables globales actualizadas con el archivo: " + updaterPluginsUpdating.upFile.get(0) + " pertenecientes al plugin " + updaterPluginsUpdating.pluginName.get(0));
                
                //Ahora que las variables fueron actualizadas, se abre la vista de confirmacion de actualizacion de plugin
                
            
                } else {
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

}  //Fin controller
