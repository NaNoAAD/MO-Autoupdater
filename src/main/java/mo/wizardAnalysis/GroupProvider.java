/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis;

import com.zavtech.morpheus.frame.DataFrame;
import java.io.File;
import mo.core.plugin.ExtensionPoint;
import mo.organization.StagePlugin;
import mo.wizardAnalysis.model.Group;

/**
 *
 * @author Jorge
 */
@ExtensionPoint
public interface GroupProvider extends StagePlugin{
  public DataFrame<Integer, String> unificate(DataFrame<Integer, String> inicial, DataFrame<Integer, String> add);
  public DataFrame readFileFrame(File file, Integer initial);
  public void writeFileFrame(DataFrame frame, File file, String separador, Boolean header);
  public void restructureIn(DataFrame<?,?> frameIn);
  public void restructureOut(DataFrame<?,?> frameIn);
  public File groupProcess(Group group,String folderName);
  //public void grupos();
}
