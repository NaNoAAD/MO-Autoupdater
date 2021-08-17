/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2;

import com.google.inject.Guice;
import com.google.inject.Injector;
import mo.core.v2.controller.MainWindowsController;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javax.swing.JFrame;
import mo.core.v2.model.Module;

/**
 *
 * @author Francisco
 */
public class Managment {
    
    public FXMLLoader loader;
    public static int height = 650;
    public static int width = 915;
    public static MainWindowsController controller;
    
    private void initFxml(JFXPanel jfxPanel){
        final Injector injector = Guice.createInjector(new Module());
        final Parent p;
        try {
          loader = new FXMLLoader(MainWindowsController.class.getResource("/fxml/core/ui/MainWindows.fxml"), null,
              new JavaFXBuilderFactory(), (ac) -> injector.getInstance(ac));
          p = loader.load();
          p.getProperties().put("controller", loader.getController());
          controller = (MainWindowsController) loader.getController();
          Scene scene = new Scene(p, width, height);
          jfxPanel.setScene(scene);
        } catch (IOException ex) {
          Logger.getLogger(Managment.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/core/ui/MainWindows.fxml"));
            Scene scene = new Scene(root);
            jfxPanel.setScene(scene);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }*/
    }
    
   public void initAndShow(){
       JFrame frame = new JFrame ("Multimodal Observer");
       frame.setResizable(true);
       JFXPanel jfxPanel = new JFXPanel();
       frame.setSize(654,800);
       frame.getContentPane().add(jfxPanel, BorderLayout.CENTER);
       frame.setVisible(true);
       
       Platform.runLater(() -> initFxml(jfxPanel));
   }
    
}
