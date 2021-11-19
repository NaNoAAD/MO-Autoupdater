/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.model;

import com.google.inject.AbstractModule;
import java.util.ArrayList;
import java.util.List;
import mo.capture.CaptureProvider;
import mo.core.plugin.Plugin;
import mo.core.plugin.PluginRegistry;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.StagePlugin;


/**
 *
 * @author Francisco
 */
public class Module extends AbstractModule{
    
    @Override
    protected void configure() {

        List<Participant> participants = new ArrayList<>();
        List<StagePluginV2> plugins = new ArrayList<>();
        List<Configuration> configurations = new ArrayList<>();
        List<StageModuleV2> stages = new ArrayList<>();
        addCapturePlugins(plugins);
        StageModuleV2 StageCapture = new StageModuleV2("Captura", plugins);
        stages.add(StageCapture);
        StagePlugin pluginSelected = null;
        Configuration configurationSelected = null;
        Integer type = new Integer(0);
        Organization model = new Organization(participants,plugins,configurations,pluginSelected,configurationSelected, stages);
        bind(Organization.class).toInstance(model);
    }
    
    public List<StagePluginV2> addCapturePlugins(List<StagePluginV2> plugins){
        for(Plugin plugin : PluginRegistry.getInstance().getPluginData().getPluginsFor("mo.capture.CaptureProvider")){
            List<ConfigurationV2> configurations = new ArrayList<>();
            CaptureProvider c = (CaptureProvider) plugin.getNewInstance();
            plugins.add(new StagePluginV2(c.getName(), configurations));
        }
        return plugins;
    }
}
