package mo.updating.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import mo.updating.updaterLogic;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controlador del splasher inicial del Launcher
 */
public class SplashScreenController {

    // Lógica y métodos para el SplashScreen
    @FXML
    private void initialize() {
        System.out.println("Ahora en primera Vista Splasher");
        //boolean permission1 = updaterLogic.updaterComparissonLogic();
        
    }

    public void getFirstsPermission(Stage stage){
        stage.setOnShown((WindowEvent event) -> {
                //Luego asegurandonos que se muestre la escena y los nodos
                Platform.runLater(() -> {
                    boolean permission1 = updaterLogic.updaterpermissionsLogic();
                    boolean answer = updaterLogic.updaterComparissonLogic(permission1);
                });
            });
        
    }

}
