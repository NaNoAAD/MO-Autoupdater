package mo.wizardAnalysis;

import com.google.inject.AbstractModule;
import java.util.ArrayList;
import java.util.List;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.StagePlugin;
import mo.wizardAnalysis.model.Group;
import mo.wizardAnalysis.model.OrgWizardAnalysis;

/**
 * Created by carl on 4/30/16.
 */
public class WizardModule extends AbstractModule {

  @Override
  protected void configure() {

    List<Participant> participants = new ArrayList<>();
    List<StagePlugin> plugins = new ArrayList<>();
    List<Group> group = new ArrayList<>();
    List<Configuration> configurations = new ArrayList<>();
    StagePlugin pluginSelected = null;
    Configuration configurationSelected = null;
    Integer type = new Integer(0);
    OrgWizardAnalysis model = new OrgWizardAnalysis(group,type,participants,plugins,configurations,pluginSelected,configurationSelected);
    bind(OrgWizardAnalysis.class).toInstance(model);
  }

  /*
     * public List<StagePluginWizard> addCapturePlugins(List<StagePluginWizard>
     * plugins){
     * 
     * for (Plugin plugin :
     * PluginRegistry.getInstance().getPluginData().getPluginsFor(
     * "mo.capture.CaptureProvider")) { List<ConfigurationWizard> configurations =
     * new ArrayList<>(); CaptureProvider c = (CaptureProvider)
     * plugin.getNewInstance(); plugins.add(new
     * StagePluginWizard(c.getName(),configurations)); } return plugins; }
   */
}
