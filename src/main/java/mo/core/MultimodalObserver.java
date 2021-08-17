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
import mo.core.v2.Managment;

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
    
    public static void main(String args[]){
        
        loadLocale();
        
        PluginRegistry.getInstance();

        MainWindow window = new MainWindow();
        MainPresenter presenter = new MainPresenter(window);
        presenter.start();
        ////
        Logger l = Logger.getLogger("");
        l.setLevel(Level.INFO);
        l.getHandlers()[0].setLevel(Level.INFO);
        
        MultimodalObserver app = new MultimodalObserver();
        new Managment().initAndShow();
        //app.nonStaticMain(args);
    }
    
}
