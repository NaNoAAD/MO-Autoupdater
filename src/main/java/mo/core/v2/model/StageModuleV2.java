/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.model;

import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import mo.organization.StagePlugin;

/**
 *
 * @author Francisco
 */

@XmlRootElement(name="stage")
@XmlType(propOrder={"name","plugins"})
public class StageModuleV2 {
    
    String name;
    List<StagePlugin> plugins;
    ObservableList<String> configPluginObservable = FXCollections.observableArrayList();
    
    public StageModuleV2(String name, List<StagePlugin> plugins){
        this.name = name;
        this.plugins = plugins;
    }
    
    public StageModuleV2(){
        this(null, null);
    }
    
    @XmlElement(name="name")
    public String gatName(){
        return name;
    }
    @XmlElementWrapper(name="plugins")
    @XmlElement(name="plugin")
    public List<StagePlugin> getPlugins(){
        if(plugins == null){
            plugins = new LinkedList<>();
        }
        return plugins;
    }
    public ObservableList<String> getConfigPluginObservable(){
        return configPluginObservable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
