/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.utils;

import java.io.File;
import java.util.List;
import mo.organization.Participant;
import mo.wizardAnalysis.model.Group;
import mo.wizardAnalysis.model.OrgWizardAnalysis;
import bibliothek.util.xml.XAttribute;
import bibliothek.util.xml.XElement;
import bibliothek.util.xml.XIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mo.organization.Configuration;
import static mo.organization.ProjectOrganization.logger;
import mo.organization.StagePlugin;

/**
 *
 * @author Jorge
 */
public class Persistence {

  public static boolean SaveConfigure(OrgWizardAnalysis org) {
    try {
      File file = new File(org.getFileProject().getAbsolutePath() + "/analysis/groupConfigure/saves");
      if (!file.exists()) {
        file.mkdir();
      }
      List<Group> groups = org.getGroups();
      Date now = new Date();
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss.SSS");
      String reportDate = df.format(now);
      String name = reportDate + ".Configure.Group.xml";
      File orgXml = new File(file, name);
      orgXml.createNewFile();
      XElement root = new XElement("ConfigureGroup");
      XAttribute type = new XAttribute("type");
      type.setInt(org.getType());
      root.addAttribute(type);
      root.addElement("FileProject").setValue(org.getFileProject().getAbsolutePath());
      root.addElement("Plugin").setString(org.getPluginSelected().getName());
      root.addElement("Configure").setString(org.getConfigurationSelected().getId());
      XElement GroupsXml = new XElement("Groups");
      XAttribute groupAttribute = new XAttribute("groupsMembers");
      groupAttribute.setValue(String.valueOf(groups.size()));
      GroupsXml.addAttribute(groupAttribute);
      for (Group group : groups) {
        XElement GroupXml = new XElement("Group");
        XAttribute idGroup = new XAttribute("id");
        idGroup.setValue(group.getId().toString());
        GroupXml.addAttribute(idGroup);
        XAttribute labelGroup = new XAttribute("label");
        labelGroup.setValue(group.getLabel());
        GroupXml.addAttribute(labelGroup);
        List<Participant> participants = group.getParticipants();
        HashMap<String,File> fileParticipant = group.getParticipantFiles();
        XElement ParticipantsXml = new XElement("Participants");
        XElement FilesXml = new XElement("Files");
        XAttribute members = new XAttribute("members");
        members.setValue(String.valueOf(participants.size()));
        ParticipantsXml.addAttribute(members);
        for (Participant participant : participants) {
          XElement xParticipant = new XElement("Participant");
          xParticipant.addElement("id").setString(participant.id);
          xParticipant.addElement("name").setString(participant.name);
          xParticipant.addElement("notes").setString(participant.notes);
          xParticipant.addElement("folder").setString(participant.folder);
          XElement date = new XElement("date");
          Calendar c = Calendar.getInstance();
          c.setTime(participant.date);
          date.addElement("day").setInt(c.get(Calendar.DAY_OF_MONTH));
          date.addElement("month").setInt(c.get(Calendar.MONTH));
          date.addElement("year").setInt(c.get(Calendar.YEAR));
          xParticipant.addElement(date);
          XAttribute locked = new XAttribute("isLocked");
          locked.setBoolean(participant.isLocked);
          xParticipant.addAttribute(locked);
          ParticipantsXml.addElement(xParticipant);
        }
        for (Map.Entry me : fileParticipant.entrySet()) {
          me.getKey();
          ((File) me.getValue()).toPath();
          XAttribute id= new XAttribute("id");
          id.setString((String) me.getKey());
          XElement fileG = new XElement("file");
          fileG.setString(((File) me.getValue()).getPath());
          fileG.addAttribute(id);
          FilesXml.addElement(fileG);
        }
        GroupXml.addElement(ParticipantsXml);
        GroupXml.addElement(FilesXml);
        GroupsXml.addElement(GroupXml);
      }
      root.addElement(GroupsXml);
      XElement ParticipantNoUsedXML = new XElement("ParticipantNoUsed");
      for (Participant participant : org.getParticipantsNoUsed()) {
        XElement xParticipant = new XElement("Participant");
        xParticipant.addElement("id").setString(participant.id);
        xParticipant.addElement("name").setString(participant.name);
        xParticipant.addElement("notes").setString(participant.notes);
        xParticipant.addElement("folder").setString(participant.folder);
        XElement date = new XElement("date");
        Calendar c = Calendar.getInstance();
        c.setTime(participant.date);
        date.addElement("day").setInt(c.get(Calendar.DAY_OF_MONTH));
        date.addElement("month").setInt(c.get(Calendar.MONTH));
        date.addElement("year").setInt(c.get(Calendar.YEAR));
        xParticipant.addElement(date);
        XAttribute locked = new XAttribute("isLocked");
        locked.setBoolean(participant.isLocked);
        xParticipant.addAttribute(locked);
        ParticipantNoUsedXML.addElement(xParticipant);
      }
      root.addElement(ParticipantNoUsedXML);
      FileOutputStream FileOutputStream =new FileOutputStream(orgXml);
      XIO.writeUTF(root, FileOutputStream);
      FileOutputStream.close();
    } catch (IOException ex) {
      Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
    return true;
   
  }

  public static OrgWizardAnalysis LoadConfigure(File file) {
    OrgWizardAnalysis org = new OrgWizardAnalysis();
    ArrayList <Participant> allParticipants= new ArrayList<Participant>();
    try {
      FileInputStream fileInputStream =new FileInputStream(file);
      XElement root = XIO.readUTF(fileInputStream);
      org.setType(root.getAttribute("type").getInt());
      String pluginName = root.getElement("Plugin").getString();
      String configureId = root.getElement("Configure").getString();
      XElement[] GroupsXml = root.getElement("Groups").getElements("Group");

      File project = new File(root.getElement("FileProject").getString());
      org.setFileProject(project);

      for (StagePlugin plugin : org.getPlugins()) {
        if (plugin.getName().replaceAll("\\s","").equals(pluginName.replaceAll("\\s",""))) {
          org.setPluginSelected(plugin);
          for (Configuration configure : plugin.getConfigurations()) {
            if (configure.getId().replaceAll("\\s","").equals(configureId.replaceAll("\\s",""))) {
              org.setConfigurationSelected(configure);
            }
          }
        }
      }
      List <Group> Groups = new ArrayList<Group>();
      for (XElement GroupXml : GroupsXml) {
        Group group = new Group(GroupXml.getAttribute("id").getInt(), GroupXml.getAttribute("label").getValue());
        XElement[] partipantsXml = GroupXml.getElement("Participants").getElements("Participant");
        List<Participant> participants = loadParticipants(partipantsXml);
        group.setParticipants(participants);
        allParticipants.addAll(participants);
        for (XElement subFile : GroupXml.getElement("Files").getElements("file")) {
          File fileParticipant=new File(subFile.getString());
          if(fileParticipant.exists()){
            group.getParticipantFiles().put(subFile.getAttribute("id").getString(),fileParticipant);
          }
          else{
            return null;
          }
        }
        Groups.add(group);
      }
      XElement[] ParticipantsNoUsedXML =root.getElement("ParticipantNoUsed").getElements("Participant");
      List<Participant> copy = (List<Participant>) allParticipants.clone();
      allParticipants.addAll(loadParticipants(ParticipantsNoUsedXML));
      
      org.setParticipantsAll(allParticipants);
      org.setParticipants(copy);
      org.setGroups(Groups);
      fileInputStream.close();
    } catch (FileNotFoundException ex) {
      Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    } catch (IOException ex) {
      Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }catch (Exception ex){
      Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
      return  null;
    }
    return org;
  }

  static List<Participant> loadParticipants(XElement[] partipantsXml) {
    List<Participant> participants = new ArrayList<Participant>();
    for (XElement participant : partipantsXml) {
      Participant p = new Participant();
      p.id = participant.getElement("id").getString();
      p.name = participant.getElement("name").getString();
      p.notes = participant.getElement("notes").getString();
      p.folder = participant.getElement("folder").getString();
      SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
      String day = participant.getElement("date").getElement("day").getString();
      String month = participant.getElement("date").getElement("month").getString();
      String year = participant.getElement("date").getElement("year").getString();
      Date date = new Date();
      try {
        date = formatter.parse(day + " " + (Integer.parseInt(month) + 1) + " " + year);
      } catch (ParseException ex) {
        logger.log(Level.SEVERE, null, ex);
      }
      p.date = date;

      if (participant.attributeExists("isLocked")) {
        p.isLocked = participant.getAttribute("isLocked").getBoolean();
      }
      participants.add(p);
    }
    return participants;
  }
}
