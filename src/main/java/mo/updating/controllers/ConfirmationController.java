package mo.updating.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import mo.updating.updaterLogic;
import mo.updating.updaterVersionNotesRegister;
import javafx.stage.Stage;

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
    private void initialize() {
        System.out.println("Ahora en primera Vista de confirmacion");
        noButton.setOnAction(event -> {
            //Logica al elegir Si
            String versionNotes = "";
            try {
                versionNotes = getVersionNotesUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
            newNotesVersion.setText(versionNotes);
        });
        yesButton.setOnAction(event -> {
            //Logica al elegir no
            System.out.println("Selecionamos no");
        });
    }

    private String getVersionNotesUI() throws IOException{
        return updaterVersionNotesRegister.getRemoteVersionNotes();
    }


    public void getSecondsPermissions(Stage stage, Boolean permission1){
        stage.setOnShown(event -> {
        updaterLogic.updaterComparissonLogic(permission1);
        });
    }

}
