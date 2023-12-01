package mo.updating.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import mo.updating.updater;
import mo.updating.updaterLogic;

/**
 * Controlador de la vista de proceso de actualizacion
 */
public class UpdatingController {
    
    @FXML
    private ProgressBar progress;

    @FXML
    private void initialize() throws IOException{
        System.out.println("-Inicializando Vista de Actualizacion en progreso");
    }

    /**
     * Metodo que permite la ejecucion de la logica de actualizacion en segundo plano, obteniendo los booleans dados por los permisos anteriores
     * @param stage
     */
    public void secondPlaneUpdating(Stage stage){
        System.out.println("-Inicio de secondplaneUpdating");
        //Se obtienen los permisos de los procesos anteriores de comparacion gracias a su naturaleza Static
        Boolean permissionBoolean = SplashScreenController.getPermission1Obtained();
        Boolean answerBoolean = SplashScreenController.getAnswerObtained();
        //Asegurandonos que la pantalla y la barra de progreso ya estan mostradas, se procede con el procedimiento de actualizacion
        Platform.runLater(() -> {
            
            updaterLogic.updaterUpdatingLogic(permissionBoolean, answerBoolean);
            System.out.println("updaterUpdatingLogic - Abriendo MO - Terminando Launcher ");
            updater.openMO();
        });
        
    }

}  //Fin controller
