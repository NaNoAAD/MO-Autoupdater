/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.prueba.count;
import java.io.File;
import java.util.*;
import javax.swing.JOptionPane;
import mo.core.plugin.*;
import mo.organization.*;
import mo.prueba.NewAnalisisPlugin;
/**
 *
 * @author Jorge
 */
@Extension(
  xtends = {
    @Extends(
        extensionPointId = "mo.prueba.NewAnalisisPlugin"
      )
    }
  )
public class CountFilesPlugin implements NewAnalisisPlugin {
  ArrayList<Configuration> configs;
  
  public CountFilesPlugin() {
    configs = new ArrayList<>();
  }
  @Override
  public String getName() {
    return "Count";
  }

  @Override
  public Configuration initNewConfiguration (ProjectOrganization po) {
    CountFilesConfig config = new CountFilesConfig();
    config.init(po.getLocation());
    configs.add(config);
    JOptionPane.showMessageDialog(null, "HI");
    return config;
  }
  @Override
  public List<Configuration> getConfigurations() {
    return configs;
  }
  @Override
  public StagePlugin fromFile(File file) {
    return null;
  }

  @Override
  public File toFile(File file) {return null;}
  }