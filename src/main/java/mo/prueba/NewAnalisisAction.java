/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.prueba;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import mo.organization.*;



/**
 *
 * @author Jorge
 */
public class NewAnalisisAction implements StageAction {

    @Override
    public String getName() {
        return "NewAnalisisAction";
    }

    @Override
    public void init(ProjectOrganization organization, Participant participant, StageModule stage) {
        ArrayList<NewAnalisisConfiguration> configs = new ArrayList<>();
        for (StagePlugin plugin : stage.getPlugins()) {
            for (Configuration configuration : plugin.getConfigurations()) {
                configs.add((NewAnalisisConfiguration) configuration);}
        }
        for (NewAnalisisConfiguration config : configs) {
            config.count();
        }
    }
    
}
