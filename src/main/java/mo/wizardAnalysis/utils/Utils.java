/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.utils;

/**
 *
 * @author Jorge
 */
public class Utils {
  
  public static String eliminateExtXML(String path){
    if(path.contains(".xml")){
      return path.substring(0,path.length()-4);
    }
    return path;
  }
}
