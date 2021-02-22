/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.prueba.inter;

import java.io.File;
import java.util.Date;
import javax.swing.JOptionPane;
import mo.organization.Configuration;
import mo.prueba.NewAnalisisConfiguration;

/**
 *
 * @author Jorge
 */
public class InterFilesConfig implements NewAnalisisConfiguration {

    String id;
    File folder;
    
    @Override
    public void init(File folder) {
        id = "nueva config";
        this.folder = folder;
    }

    @Override
    public void count() {
        JOptionPane.showMessageDialog(null, folder.getPath());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public File toFile(File parent) {
        return null;
    }

    @Override
    public Configuration fromFile(File file) {
        return null;
    }
    
}
