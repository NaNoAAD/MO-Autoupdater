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

    /**
     * Metodo que se utiliza para hacer muestra de la vista de confirmacion
     */
    public void loadConfirmationView(String fxml){
        try {
            //Se obtiene el classloader del hilo actual para la obtencion del FXML
            //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // Se carga FXML con vista de confirmacion
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // Se configura la nueva escena
            Scene scene = new Scene(root);

            // Se crea un nuevo Stage para la segunda vista
            Stage stage = new Stage();
            stage.setScene(scene);

            // Mostrar la nueva vista
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo que ejecuta Multimodal Observer
     */
    public static void openMO(){
        try {
            // Iniciar MO
            //Es importante que este comando tenga el nombre el .jar de MO (verificar el uso de contains)
            Runtime.getRuntime().exec("java -jar multimodal-observer-server-5-0.0.0.jar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}