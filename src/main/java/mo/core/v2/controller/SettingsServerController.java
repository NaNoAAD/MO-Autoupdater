/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class SettingsServerController implements Initializable {

    @FXML
    private BorderPane borderPaneSettingServer;
    @FXML
    private HBox bottonPaneSettingServer;
    @FXML
    private VBox centerPaneSettingServer;
    @FXML
    private Text TCPPort;
    @FXML
    private Text UDPPort;
    @FXML
    private TextField localIP;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
