/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.model;

import com.google.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import mo.wizardAnalysis.model.Group;

/**
 *
 * @author Francisco
 */
public class Organization {
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
    List<StageModule> stages;
    StageModule stageModule;

    @Inject
    public Organization(List<Participant> participants, List<StagePlugin> plugins, List<Configuration> configurations, StagePlugin pluginSelected, Configuration configurationSelected) {
        this.fileProject = fileProject;
        this.plugins = plugins;
        this.configurations = configurations;
        this.pluginSelected = pluginSelected;
        this.org = org;
        this.stageModule = stageModule;
    }
    
    public File getFileProject() {
        return fileProject;
    }

    public void setFileProject(File fileProject) {
        this.fileProject = fileProject;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
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
    }

    public List<Participant> getParticipantsNoUsed() {
        return participantsNoUsed;
    }

    public void setParticipantsNoUsed(List<Participant> participantsNoUsed) {
        this.participantsNoUsed = participantsNoUsed;
    }

    public List<Participant> getParticipantsAll() {
        return participantsAll;
    }

    public void setParticipantsAll(List<Participant> participantsAll) {
        this.participantsAll = participantsAll;
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

    public ProjectOrganization getOrg() {
        return org;
    }

    public void setOrg(ProjectOrganization org) {
        this.org = org;
    }

    public Boolean getRestoreBoolean() {
        return restoreBoolean;
    }

    public void setRestoreBoolean(Boolean restoreBoolean) {
        this.restoreBoolean = restoreBoolean;
    }

    public List<StageModule> getStageModules(){
        return stages;
    }
    
    public StageModule getCaptureStage(){
        return stages.get(0);
    }
    
    public void setStageModules(List<StageModule> stages){
        this.stages = stages;
    }
    
    
    public StageModule getStageModule() {
        return stageModule;
    }

    public void setStageModule(StageModule stageModule) {
        this.stageModule = stageModule;
    }
    
    
    
}
