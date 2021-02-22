/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.prueba.count;

import java.io.File;
import java.util.Date;
import javax.swing.JOptionPane;
import mo.organization.Configuration;
import mo.prueba.NewAnalisisConfiguration;

/**
 *
 * @author Jorge
 */
public class CountFilesConfig implements NewAnalisisConfiguration{
  String id;  
  File folder;
  
  @Override
  public void init(File folder) {
    id = Long.toString((new Date()).getTime());this.folder = folder;  
  }

  @Override
  public void count() {
    JOptionPane.showMessageDialog(null, folder.listFiles().length);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public File toFile(File parent) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Configuration fromFile(File file) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
