package mo.updating;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mo.updating.controllers.SplashScreenController;

/**
 * Clase principal encarga de iniciar el launcher y las vistas
 */
public class updater extends Application {

    public boolean permissionByVersions;
    

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Se inicia la primera vista (Splasher)
        loadSplashView("visual/Splasher Principal.fxml", "Multimodal Observer - Launcher", primaryStage);
        //Se configura la vista
    }

    /**
     * Metodo que carga la vista Spalsh
     * @param fxml Archivo que contiene la informacion descriptiva de la vista
     * @param title String que pone titulo a la ventana que se presenta
     * @param stage Stage necesaria para proseguir con el funcionamiento de JavaFX
     */
    private void loadSplashView(String fxml, String title, Stage stage) throws Exception{
        try {
            //Se carga el archivo de vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            //Se instancia controller para poder usar el metodo getFirstsPermission que esta en controlador splasher
            SplashScreenController controller = loader.getController();

            //con la vista ya mostrada, se procede a usar la logica de las primeras comparaciones
            //Primero asegurandonos que se muestre la ventana, la escena y los nodos en el siguiente metodo
            controller.getFirstsPermission(stage);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();     

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}