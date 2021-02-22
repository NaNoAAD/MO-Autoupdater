/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.prueba.inter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
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
public class interPlugin implements NewAnalisisPlugin{
    
    ArrayList<Configuration> configs;

    public interPlugin() {
        configs= new ArrayList<> () ;
    }
    
    @Override
    public String getName() {
    return "inter";
    }

    @Override
    public Configuration initNewConfiguration(ProjectOrganization organization) {
        InterFilesConfig config=new InterFilesConfig();
        JOptionPane.showMessageDialog(null, "HI");
        config.init(organization.getLocation());
        configs.add(config);
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
    public File toFile(File parent) {
        return null;
    }
    
}
