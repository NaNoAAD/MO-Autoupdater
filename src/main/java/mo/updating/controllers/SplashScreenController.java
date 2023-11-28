package mo.updating.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mo.updating.updater;
import mo.updating.updaterLogic;

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

    /**
     * Metodo de controlador que permite la ejecucion de fondo de los metodos logicos que permiten comparar los registros de versiones locales y remotos
     * ademas de lso archivos, ganando los boolean necesarios para ejecutar la actualizacion si correspondiese
     * @param stage
     */
    public void getFirstsPermission(Stage stage){
        stage.setOnShown((WindowEvent event) -> {
                //Luego asegurandonos que se muestre la escena y los nodos
                Platform.runLater(() -> {
                    boolean permission1 = updaterLogic.updaterpermissionsLogic();
                    boolean answer = updaterLogic.updaterComparissonLogic(permission1);
                    
                    //Debug: Prueba de cerrado
                    stage.close();
                    //updater.loadConfirmationView("visual/Confirmacion.fxml");
                    try {
                            //Se debe actualizar, se cierra el splasher y se accede a la vista de confirmacion
                            stage.close();
                            // Se carga FXML con vista de confirmacion
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../visual/Confirmacion.fxml"));
                            Parent root = loader.load();
                            //Se carga nuevo controlador (Si es necesario algun procedimiento a priori)
                            //ConfirmationController controller = loader.getController();

                            // Se configura la nueva escena
                            Scene scene = new Scene(root);

                            // Se crea un nuevo Stage para la segunda vista
                            Stage newStage = new Stage();
                            newStage.setScene(scene);
                            newStage.setTitle("Nuevas Caracteristicas encontradas");
                            newStage.setResizable(false);

                            // Mostrar la nueva vista
                            newStage.show();    
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    
                    /*Ahora, dependiendo de los 2 valores anteriores, se decide si se lanza la vista de 
                     * confirmacion, o se pasa de largo y continuamos con la apertura sencilla de MO
                      */
                    if(permission1 == true && answer == true){
                        try {
                            //Se debe actualizar, se cierra el splasher y se accede a la vista de confirmacion
                            stage.close();
                            // Se carga FXML con vista de confirmacion
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("visual/Confirmacion.fxml"));
                            Parent root = loader.load();
                            //Se carga nuevo controlador (Si es necesario algun procedimiento a priori inicialize)
                            //ConfirmationController controller = loader.getController();

                            // Se configura la nueva escena
                            Scene scene = new Scene(root);

                            // Se crea un nuevo Stage para la segunda vista
                            Stage newStage = new Stage();
                            newStage.setScene(scene);

                            // Mostrar la nueva vista
                            newStage.show();    
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                    } else {
                        //Se debe simplemente abrir MO
                        updater.openMO();
                    }
                });
            });
        
    }

}
