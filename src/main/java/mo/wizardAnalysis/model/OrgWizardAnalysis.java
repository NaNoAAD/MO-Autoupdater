/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.model;

import com.google.inject.Inject;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mo.core.Utils.getBaseFolder;
import mo.core.plugin.PluginRegistry;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import mo.wizardAnalysis.Controller.PluginController;

/**
 *
 * @author Jorge
 */
public class OrgWizardAnalysis {

  File fileProject;
  List<Group> groups;
  int type;
  List<Participant> participants = new ArrayList<>();
  List<Participant> participantsNoUsed =new ArrayList<>();
  List<Participant> participantsAll = new ArrayList<>();
  List<StagePlugin> plugins;
  List<Configuration> configurations;
  StagePlugin pluginSelected;
  Configuration configurationSelected;
  ProjectOrganization org;
  Boolean restoreBoolean = false;
  StageModule stageModule;

  public StageModule getStageModule() {
    return stageModule;
  }

  public OrgWizardAnalysis() {
  }

  @Inject
  public OrgWizardAnalysis(List<Group> group, int type, List<Participant> participants, List<StagePlugin> plugins, List<Configuration> configurations, StagePlugin pluginSelected, Configuration configurationSelected) {
    this.fileProject = null;
    this.groups = group;
    this.type = type;
    this.participants = participants;
    this.plugins = plugins;
    this.configurations = configurations;
    this.pluginSelected = pluginSelected;
    this.configurationSelected = configurationSelected;
  }

  public void restore(OrgWizardAnalysis orgW) {
    this.fileProject = orgW.getFileProject();
    this.groups = orgW.getGroups();
    this.type = orgW.getType();
    this.participantsAll = orgW.getParticipantsAll();
    this.setParticipants(orgW.getParticipants());
    this.plugins = orgW.getPlugins();
    this.configurations = orgW.getConfigurations();
    this.pluginSelected = orgW.getPluginSelected();
    this.configurationSelected = orgW.getConfigurationSelected();
    this.org=orgW.getOrg();
    this.restoreBoolean = true;
  }

  public File getFileProject() {
    return fileProject;
  }

  public void setFileProject(File fileProject) {
    this.fileProject = fileProject;
    this.restoreBoolean = false;
    loadParticipants(fileProject);
    loadPlugins(fileProject);

  }
  
  //This
  private void loadPlugins(File fileProject) {
    try {
      String path = "analysis.xml";
      Class<?> clazz = PluginRegistry.getInstance().getClassForName("mo.analysis.AnalysisStage");
      Object o = clazz.newInstance();
      Method method = clazz.getDeclaredMethod("fromFile", File.class);
      StageModule stage = (StageModule) method.invoke(o, new File(fileProject.getAbsolutePath(), path));
      this.stageModule=stage;
      setPlugins(stage.getPlugins());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
        | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
      Logger.getLogger(OrgWizardAnalysis.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> group) {
    this.groups = group;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public List<Participant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<Participant> participants) {
    this.participants = participants;
    ArrayList <Participant>noUsed = new ArrayList<Participant>();
    for (Participant participant : participantsAll) {
      boolean add = true;
      for (Participant participant1 : participants) {
        if(participant1.name.equals(participant.name) && participant1.id.equals(participant.id)){
          add = false;
          break;
        }
      }
      if(add){
       noUsed.add(participant);
      }
    }
    this.participantsNoUsed = noUsed;
  }

  public List<StagePlugin> getPlugins() {
    return plugins;
  }

  public void setPlugins(List<StagePlugin> plugins) {
    this.plugins = plugins;
  }

  public List<Configuration> getConfigurations() {
    return configurations;
  }

  public void setConfigurations(List<Configuration> configurations) {
    this.configurations = configurations;
  }

  public StagePlugin getPluginSelected() {
    return pluginSelected;
  }

  public void setPluginSelected(StagePlugin pluginSelected) {
    this.pluginSelected = pluginSelected;
  }

  public Configuration getConfigurationSelected() {
    return configurationSelected;
  }

  public void setConfigurationSelected(Configuration configurationSelected) {
    this.configurationSelected = configurationSelected;
  }

  public List<Participant> getParticipantsAll() {
    return participantsAll;
  }

  public void setParticipantsAll(List<Participant> participantsAll) {
    this.participantsAll = participantsAll;
  }
  
  //This
  private void loadParticipants(File fileProject) {
    org = new ProjectOrganization(fileProject.getAbsolutePath());
    participantsAll = org.getParticipants();
    setParticipants(new ArrayList<>());
  }

  public Boolean getRestoreBoolean() {
    return restoreBoolean;
  }

  public List<Participant> getParticipantsNoUsed() {
    return participantsNoUsed;
  }

  public ProjectOrganization getOrg() {
    return org;
  }
  public StagePlugin getNotesPlugin(){
    for (StagePlugin plugin : this.plugins) {
      if(plugin.getName().equals("Notes plugin")){
        return plugin;
      }
    }
    return null;
  }
}
