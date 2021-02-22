/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.wizardAnalysis.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mo.organization.Participant;

/**
 *
 * @author Jorge
 */
public class Group {
  
  private Integer id;
  private String Label;
  private List<Participant> Participants;
  private HashMap<String,File> ParticipantFiles=new HashMap<String,File>();
  
  public Group(int id) {
    this.id = id;
    this.Label = "etiqueta "+id;
    this.Participants = new ArrayList<Participant>();
  }
  public Group(int id, String Label) {
    this.id = id;
    this.Label = Label;
    this.Participants = new ArrayList<Participant>();
  }
  public Group(int id, String Label, List<Participant> Participants) {
    this.id = id;
    this.Label = Label;
    this.Participants = Participants;
  }

  public String getLabel() {
    return Label;
  }

  public void setLabel(String Label) {
    this.Label = Label;
  }

  public List<Participant> getParticipants() {
    return Participants;
  }

  public void setParticipants(List<Participant> Participants) {
    this.Participants = Participants;
  }

  public Integer getId() {
    return id;
  }

  public void addParticipant(Participant Participant) {
    this.Participants.add(Participant);
  }
  public Integer getSizeParticipant (){
    return this.Participants.size();
  }
  public void addFile(String participant,File file){
      ParticipantFiles.put(participant, file);
  }
  public void removeFile(String participant,File file){
      ParticipantFiles.remove(participant);
  }
  public HashMap<String,File> getParticipantFiles(){
    return ParticipantFiles;
  }
  public void clearFiles (){
    ParticipantFiles.clear();
  }
  public void setParticipantFiles(HashMap<String,File> ParticipantFiles){
    this.ParticipantFiles=ParticipantFiles;
  }
}
