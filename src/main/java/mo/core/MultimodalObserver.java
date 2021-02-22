package mo.core;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import static mo.core.Language.loadLocale;
import mo.core.plugin.PluginRegistry;

public class MultimodalObserver {
    public static final String APP_PREFERENCES_FILE = 
            Utils.getBaseFolder()+"/preferences.xml";
    
    /*private void nonStaticMain(String args[]){
        loadLocale();
        
        PluginRegistry.getInstance();

        MainWindow window = new MainWindow();
        MainPresenter presenter = new MainPresenter(window);
        presenter.start();

    }*/
    
    private void initFxml(JFXPanel jfxPanel){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/core/ui/Test2.fxml"));
            Scene scene = new Scene(root);
            jfxPanel.setScene(scene);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
   private void initAndShow(){
       JFrame frame = new JFrame ("Multimodal Observer");
       frame.setResizable(false);
       JFXPanel jfxPanel = new JFXPanel();
       frame.setSize(654,654);
       frame.getContentPane().add(jfxPanel, BorderLayout.CENTER);
       frame.setVisible(true);
       
       Platform.runLater(() -> initFxml(jfxPanel));
   }
    
    public static void main(String args[]){

        Logger l = Logger.getLogger("");
        l.setLevel(Level.INFO);
        l.getHandlers()[0].setLevel(Level.INFO);
        
        MultimodalObserver app = new MultimodalObserver();
        app.initAndShow();
        //app.nonStaticMain(args);
    }

    /*private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(new JButton("Click me!"));
            }
        });
    }*/
    
}
