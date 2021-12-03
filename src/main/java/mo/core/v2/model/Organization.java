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
import javafx.util.Pair;
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
    Participant pSelected = null;
    //List<StageModule> stages;
    StageModule MOCaptureStage;
    List<StagePluginV2> plugins;
    List<StagePluginV2> plugins2;
    List<StagePluginV2> plugins3;
    List<Configuration> configurations;
    StagePlugin pluginSelected;
    Configuration configurationSelected;
    ProjectOrganization org;
    Boolean restoreBoolean = false;
    List<CaptureProvider> captures;
    List<Pair<String,String>> configCaptures = new ArrayList<Pair<String,String>>();
    List<AnalysisProvider> analysis;
    List<VisualizationProvider> visualization;
    String config;
    ObservableList<String> ObservablePlugins = FXCollections.observableArrayList();
    String aux;
    List<StageModuleV2> stages;
    List<StageModuleV2> stages2;
    List<StageModuleV2> stages3;

    @Inject
    public Organization(List<Participant> participants, List<StagePluginV2> plugins, List<StagePluginV2> plugins2, List<StagePluginV2> plugins3, List<Configuration> configurations, StagePlugin pluginSelected, Configuration configurationSelected, List<StageModuleV2> stages, List<StageModuleV2> stages2, List<StageModuleV2> stages3) {
        this.fileProject = fileProject;
        this.plugins = plugins;
        this.plugins2 = plugins2;
        this.plugins3 = plugins3;
        this.configurations = configurations;
        this.pluginSelected = pluginSelected;
        captures = new ArrayList<CaptureProvider>();
        analysis = new ArrayList<AnalysisProvider>();
        visualization = new ArrayList<VisualizationProvider>();
        this.stages = stages;
        this.stages2 = stages2;
        this.stages3 = stages3;
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

    public Participant getpSelected() {
        return pSelected;
    }

    public void setpSelected(Participant pSelected) {
        this.pSelected = pSelected;
    }

    public List<Pair<String, String>> getConfigCaptures() {
        return configCaptures;
    }

    public void setConfigCaptures(List<Pair<String, String>> configCaptures) {
        this.configCaptures = configCaptures;
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

    public List<StagePluginV2> getPlugins2() {
        return plugins2;
    }

    public void setPlugins2(List<StagePluginV2> plugins2) {
        this.plugins2 = plugins2;
    }

    public List<StagePluginV2> getPlugins3() {
        return plugins3;
    }

    public void setPlugins3(List<StagePluginV2> plugins3) {
        this.plugins3 = plugins3;
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

    public List<StageModuleV2> getStages2() {
        return stages2;
    }

    public StageModuleV2 getAnalysisStage(){
        return stages2.get(0);
    }
    
    public void setStages2(List<StageModuleV2> stages2) {
        this.stages2 = stages2;
    }

    public List<StageModuleV2> getStages3() {
        return stages3;
    }

    public StageModuleV2 getVisualizationStage(){
        return stages3.get(0);
    }
    
    public void setStages3(List<StageModuleV2> stages3) {
        this.stages3 = stages3;
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
