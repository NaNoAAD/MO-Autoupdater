/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.prueba;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import mo.core.I18n;
import mo.core.plugin.Extends;
import mo.core.plugin.Extension;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.organization.*;

/**
 *
 * @author Jorge
 */
@Extension(
    xtends = {
        @Extends(
                extensionPointId = "mo.organization.StageModule"
        )
    }
)
public class NewAnalisisStage implements StageModule {
    private ArrayList<StageAction> actions;
    private List<StagePlugin> plugins;
    
     private final static Logger logger = Logger.getLogger(NewAnalisisStage.class.getName());
    private ProjectOrganization organization;
    private I18n i18n;
    private static final String CODE_NAME = "NewAnalisisStagecode";
    
    public NewAnalisisStage() {
        plugins = new ArrayList<>();
        for  (Plugin plugin: PluginRegistry.getInstance().getPluginsFor("mo.prueba.NewAnalisisPlugin")) {
           NewAnalisisPlugin p = (NewAnalisisPlugin) plugin.getNewInstance();
           System.out.println(p.getName());
           plugins.add(p);
        }
        actions = new ArrayList<>();
        NewAnalisisAction act = new NewAnalisisAction();
        actions.add(act);
    }

    @Override
    public List<StagePlugin> getPlugins() {
        return plugins;
    }

    @Override
    public void setOrganization(ProjectOrganization org) {
        this.organization=org;
    }

    @Override
    public List<StageAction> getActions() {
        return actions;
    }

    @Override
    public String getCodeName() {
        return CODE_NAME;
    }
    @Override
    public String getName() {
        return "NewnalisisStage";
    }
    @Override
    public StageModule fromFile(File file) {
        return null;
    }
    @Override
    public File toFile(File parent) {
        return null;
    }
}
