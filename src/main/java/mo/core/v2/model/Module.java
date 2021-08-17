/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.model;

import com.google.inject.AbstractModule;
import java.util.ArrayList;
import java.util.List;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.StagePlugin;
import mo.wizardAnalysis.model.Group;
import mo.wizardAnalysis.model.OrgWizardAnalysis;

/**
 *
 * @author Francisco
 */
public class Module extends AbstractModule{
    
    @Override
    protected void configure() {

      List<Participant> participants = new ArrayList<>();
        List<StagePlugin> plugins = new ArrayList<>();
        List<Group> group = new ArrayList<>();
        List<Configuration> configurations = new ArrayList<>();
        StagePlugin pluginSelected = null;
        Configuration configurationSelected = null;
        Integer type = new Integer(0);
        Organization model = new Organization(participants,plugins,configurations,pluginSelected,configurationSelected);
        bind(Organization.class).toInstance(model);
    }
}
