package mo.updating.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import mo.updating.updaterVersionNotesRegister;

public class UpdatingController {
    
    @FXML
    private ProgressBar progress;

    @FXML
    private void initialize() throws IOException{
        System.out.println("Ahora en Vista de Actualizacion en progreso");
        //Aca es probable que se deba colocar un sistema para sincronizar el progreso de la logica con la barra de progreso
        progress.setProgress(0.5);
    }
    
}
