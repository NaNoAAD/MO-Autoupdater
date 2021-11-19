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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mo.analysis.AnalysisProvider;
import mo.capture.CaptureProvider;
import mo.organization.Configuration;
import mo.organization.Participant;
import mo.organization.ProjectOrganization;
import mo.organization.StageModule;
import mo.organization.StagePlugin;
import mo.visualization.VisualizationProvider;

/**
 *
 * @author Francisco
 */
public class Organization {
    File fileProject;
    int type;
    public int newProyect;
    List<Participant> participants = new ArrayList<>();
    //List<StageModule> stages;
    StageModule MOCaptureStage;
    //List<Participant> participantsNoUsed =new ArrayList<>();
    //List<Participant> participantsAll = new ArrayList<>();
    List<StagePluginV2> plugins;
    List<Configuration> configurations;
    StagePlugin pluginSelected;
    Configuration configurationSelected;
    ProjectOrganization org;
    Boolean restoreBoolean = false;
    List<CaptureProvider> captures;
    List<AnalysisProvider> analysis;
    List<VisualizationProvider> visualization;
    String config;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    String aux;
    List<StageModuleV2> stages;

    @Inject
    public Organization(List<Participant> participants, List<StagePluginV2> plugins, List<Configuration> configurations, StagePlugin pluginSelected, Configuration configurationSelected, List<StageModuleV2> stages) {
        this.fileProject = fileProject;
        this.plugins = plugins;
        this.configurations = configurations;
        this.pluginSelected = pluginSelected;
        captures = new ArrayList<CaptureProvider>();
        analysis = new ArrayList<AnalysisProvider>();
        visualization = new ArrayList<VisualizationProvider>();
        this.stages=stages;
    }
    
    public File getFileProject() {
        return fileProject;
    }

    public void setFileProject(File fileProject) {
        this.fileProject = fileProject;
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

    /*public List<Participant> getParticipantsNoUsed() {
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
    }*/

    public List<StagePluginV2> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<StagePluginV2> plugins) {
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

    public List<StageModuleV2> getStageModules(){
        return stages;
    }
    
    public StageModuleV2 getCaptureStage(){
        return stages.get(0);
    }
    
    public void setStageModules(List<StageModuleV2> stages){
        this.stages = stages;
    }
    
    
    public StageModule getMOCaptureStage() {
        return MOCaptureStage;
    }

    public void setMOCaptureStage(StageModule stageModule) {
        this.MOCaptureStage = stageModule;
    }

    public List<CaptureProvider> getCaptures() {
        return captures;
    }

    public void setCaptures(List<CaptureProvider> captures) {
        this.captures = captures;
    }

    public List<AnalysisProvider> getAnalysis() {
        return analysis;
    }

    public void setAnalysis(List<AnalysisProvider> analysis) {
        this.analysis = analysis;
    }

    public List<VisualizationProvider> getVisualization() {
        return visualization;
    }

    public void setVisualization(List<VisualizationProvider> visualization) {
        this.visualization = visualization;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
    
    public ObservableList<String> getObservablePlugins() {
        return ObservablePlugins;
    }

    public void setObservablePlugins(ObservableList<String> ObservablePlugins) {
        this.ObservablePlugins = ObservablePlugins;
    }
        
}
