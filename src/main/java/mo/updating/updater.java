package mo.updating;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mo.updating.controllers.SplashScreenController;

/**
 * Clase principal encarga de iniciar el launcher, la logica y todo los demas procesos
 * Contiene el metodo main()
 */
public class updater extends Application {

    public boolean permissionByVersions;
    public boolean pluginPermission;
    

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Se inicia la primera vista (Splasher)
        String currentPath = System.getProperty("user.dir").replace("\\", "/");
        System.out.println("UPDATER INICIADO DESDE: " + currentPath + "\n");

        //Se cargan y setean los argumentos globales desde el archivo args.up para trabajar MO
        updaterArguments.setArguments(updaterArguments.saveArguments("./args.up"));

        //Se carga el archivo que contiene los plugins integrados a ser revisados en la carpeta .ups
        //y se guardan en la variable estatica para ser usado en los procesos posteriores a actualizar mo
        ///Este archivo a capturar tiene en cada linea <nombre_del_plugin>: <string ruta a argumentos de trabajo de plugin>
        updaterPluginsUpdating.upFile.addAll(updaterPluginsUpdating.getUpFilePluginsToUpdate("./plugins.up"));

        //Con todo lo anterior listo, se inicia el splasher de MO y las logicas posteriores
        loadSplashView("/src/main/java/mo/updating/visual/SplasherPrincipal.fxml", "Bienvenido a Multimodal Observer", primaryStage);
    }

    /**
     * Metodo que carga la vista Spalsh
     * @param fxml Archivo que contiene la informacion descriptiva de la vista
     * @param title String que pone titulo a la ventana que se presenta
     * @param stage Stage necesaria para proseguir con el funcionamiento de JavaFX
     */
    private void loadSplashView(String fxml, String title, Stage stage) throws Exception{
        try {
            System.out.println("(updater.java) - Se carga vista de Splasher" + getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            //Se carga el archivo de vista
            URL fxmlURL = getClass().getResource(fxml);
             System.out.println(fxml);
            FXMLLoader loader = new FXMLLoader(fxmlURL);

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
     *
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
    }*/


    /**
     * Metodo que ejecuta Multimodal Observer a traves de su .jar
     */
    public static void openMO(){
        try {
            System.out.println("(updater.java) -Apertura de MO - Finalizando Launcher");
            // Iniciar MO
            //Es importante que este comando tenga el nombre el .jar de MO (verificar el uso de contains)
            Runtime.getRuntime().exec("java -jar ./build/libs/multimodal-observer-server-5-0.0.0.jar");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}