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
import mo.organization.StagePlugin;

/**
 *
 * @author Francisco
 */
public class OrganizationV2 {
    
    private File fileProject;
    private List<Participant> participants = new ArrayList<>();
    private List<StagePlugin> plugins = new ArrayList<>();
    private List<Configuration> configurations = new ArrayList<>();
    private ProjectOrganization projectOrg;

    @Inject
    public OrganizationV2(List<Participant> participants, List<StagePlugin> plugins, List<Configuration> configurations){
        this.fileProject = null;
        this.participants = participants;
        this.plugins = plugins;
        this.configurations = configurations;
        this.projectOrg = null;
    }
    
    public void restore(OrganizationV2 org){
        this.fileProject = org.getFileProject();
        this.participants = org.getParticipants();
        this.plugins = org.getPlugins();
        
    }
    
    public File getFileProject() {
        return fileProject;
    }

    public void setFileProject(File fileProject) {
        this.fileProject = fileProject;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<StagePlugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<StagePlugin> plugins) {
        this.plugins = plugins;
    }

    public List<Configuration> getConfiguratios() {
        return configurations;
    }

    public void setConfiguratios(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public ProjectOrganization getProjectOrg() {
        return projectOrg;
    }

    public void setProjectOrg(ProjectOrganization projectOrg) {
        this.projectOrg = projectOrg;
    }
    
    
    
    
}
