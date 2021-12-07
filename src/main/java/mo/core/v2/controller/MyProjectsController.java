/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mo.core.v2.controller;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import mo.core.v2.model.Organization;
import mo.core.filemanagement.project.Project;
import mo.organization.Configuration;
import mo.organization.ProjectOrganization;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author Francisco
 */
public class MyProjectsController implements Initializable {
    private final String CONTROLLER_KEY = "controller";

    @FXML
    private GridPane projectsGrid;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Text nameProject;
    @FXML
    private Text participantsNumber;
    @FXML
    private Text capturesNumber;
    @FXML
    private Text analysisNumber;
    @FXML
    private Button seeButton;
    @FXML
    private ImageView iconProject;
    @FXML
    private Text visualizationsNumber;
    @FXML
    private Text nameProject2;
    @FXML
    private Text participantsNumber2;
    @FXML
    private Text capturesNumber2;
    @FXML
    private Text analysisNumber2;
    @FXML
    private Text visualizationsNumber2;
    @Inject
    public Injector injector;
    @Inject 
    Organization model;
    public List<String> projectsPath = new ArrayList<>();
    public List<ProjectOrganization> projectsOrgs = new ArrayList<>();
    int row=0, colum = 0;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            init();
            getDataByPath();
        } catch (SAXException | ParserConfigurationException | IOException | ParseException ex) {
            Logger.getLogger(MyProjectsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public void seeProject(MouseEvent event, String name){
        for(ProjectOrganization PO : projectsOrgs){
            if(PO.getLocation().getName().equals(name)){
                model.setOrg(PO);
                model.setParticipants(PO.getParticipants());
                model.setFileProject(PO.getLocation());
                model.newProyect=0;
                model.getProviderFromOrg();
            }
        }
        Stage stage = (Stage) seeButton.getScene().getWindow();
        stage.close();
    }
    
    //Lee el archivo File.xml y guarda el path de los proyectos guardados
    private void init() throws SAXException, ParserConfigurationException, IOException{
        File file = new File("files.xml");
        int numProjects=0;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(file);
        document.getDocumentElement().normalize();
        NodeList nlist = document.getDocumentElement().getChildNodes();
        int aux;
        for(aux=0; aux<nlist.getLength(); aux++){
            Node node = nlist.item(aux);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element elem = (Element) node;
                numProjects = elem.getElementsByTagName("project").getLength();
                break;
            }
        }
        for(int i=0; i<numProjects; i++){
            Node node = nlist.item(aux);
            Element elem = (Element) node;
            projectsPath.add(elem.getElementsByTagName("project").item(i).getChildNodes().item(0).getNodeValue());
        }
    }
    private void getDataByPath() throws ParserConfigurationException, IOException, SAXException, ParseException{
        for(String path : projectsPath){
            Project p = new Project(path);
            ProjectOrganization po = new ProjectOrganization(p);
            projectsOrgs.add(po);
            
        }
        
        showProjects();
    }
    private void showProjects(){
        int aux = 0;
        capturesNumber2.setText("0");
        analysisNumber2.setText("0");
        visualizationsNumber2.setText("0");
        for(int i=0; i<projectsOrgs.size();i++){
            ProjectOrganization PO =  projectsOrgs.get(i);
            if(i==0){
                nameProject2.setText(PO.getLocation().getName());
                participantsNumber2.setText(String.valueOf(PO.getParticipants().size()));
                seeButton.setId(PO.getLocation().getName());
                seeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    seeProject(event, seeButton.getId());
                });
                if(!PO.getStages().isEmpty()){
                    for(int j=0; j<PO.getStages().size(); j++){
                        aux=0;
                        for(int k=0; k<PO.getStages().get(j).getPlugins().size(); k++){
                            if(!PO.getStages().get(j).getPlugins().get(k).getConfigurations().isEmpty()){
                                aux = aux + PO.getStages().get(j).getPlugins().get(k).getConfigurations().size();
                            }
                        }
                        switch(j){
                            case 0: 
                                capturesNumber2.setText(String.valueOf(aux));
                                break;
                            case 1:
                                analysisNumber2.setText(String.valueOf(aux));
                                break;
                            case 2:
                                visualizationsNumber2.setText(String.valueOf(aux));
                                break;     
                        }
                    }
                }
                colum++;
            }
            else{
                if(row>projectsGrid.getRowConstraints().size()){
                    projectsGrid.addRow(row, null);
                }
                if(colum<2){
                    creatAndAddToGrid(PO, row, colum);
                    if(colum == 1){
                        colum=0;
                        row++;
                    }
                    else{
                        colum++;
                    }
                }
            } 
        }
    }
    public void creatAndAddToGrid(ProjectOrganization PO, int row, int colum){
        javafx.scene.image.ImageView icon2 = new javafx.scene.image.ImageView("/images/folder.png");
        String idAux = PO.getLocation().getName();
        icon2.setId(idAux);
        icon2.setImage(iconProject.getImage());
        icon2.setFitHeight(25);
        icon2.setFitWidth(25);
        icon2.setTranslateX(5);
        icon2.setTranslateY(-45);
        projectsGrid.add(icon2, colum, row);

        Text name2 = new Text ("Name:");
        name2.setId(idAux);
        name2.setTranslateX(40);
        name2.setTranslateY(-49);
        projectsGrid.add(name2, colum, row);

        Text nameLabel2 = new Text(PO.getLocation().getName());
        nameLabel2.setId(idAux);
        nameLabel2.setTranslateX(120);
        nameLabel2.setTranslateY(-49);
        projectsGrid.add(nameLabel2, colum, row);

        Text participant2 = new Text(participantsNumber.getText());
        participant2.setId(idAux);
        participant2.setTranslateX(40);
        participant2.setTranslateY(-29);
        projectsGrid.add(participant2, colum, row);

        int a = PO.getParticipants().size();
        Text participantLabel2 = new Text(String.valueOf(a));
        participantLabel2.setId(idAux);
        participantLabel2.setTranslateX(120);
        participantLabel2.setTranslateY(-29);
        projectsGrid.add(participantLabel2, colum, row);

        Text capture2 = new Text("Captures:");
        capture2.setId(idAux);
        capture2.setTranslateX(40);
        capture2.setTranslateY(-9);
        projectsGrid.add(capture2, colum, row);
        
        Text analysis2 = new Text("Analysis:");
        analysis2.setId(idAux);
        analysis2.setTranslateX(40);
        analysis2.setTranslateY(11);
        projectsGrid.add(analysis2, colum, row);
        
        Text visualization2 = new Text("Visualizations:");
        visualization2.setId(idAux);
        visualization2.setTranslateX(40);
        visualization2.setTranslateY(31);
        projectsGrid.add(visualization2, colum, row);
        
        Text captureLabel2 = new Text("");
        captureLabel2.setId(idAux);
        captureLabel2.setTranslateX(120);
        captureLabel2.setTranslateY(-9);
        projectsGrid.add(captureLabel2, colum, row);

        Text analysisLabel2 = new Text("");
        analysisLabel2.setId(idAux);
        analysisLabel2.setTranslateX(120);
        analysisLabel2.setTranslateY(11);
        projectsGrid.add(analysisLabel2, colum, row);

        Text visualizationLabel2 = new Text("");
        visualizationLabel2.setId(idAux);
        visualizationLabel2.setTranslateX(120);
        visualizationLabel2.setTranslateY(31);
        projectsGrid.add(visualizationLabel2, colum, row);
        
        Button seeButton2 = new Button("See");
        seeButton2.setStyle("-fx-background-color: #3D3D3D40; ");
        seeButton2.setTranslateX(220);
        seeButton2.setTranslateY(44);
        projectsGrid.add(seeButton2, colum, row);
        seeButton2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            seeProject(event, seeButton.getId());
        });
        
        
        if(PO.getStages().isEmpty()){
            captureLabel2.setText("0");
            analysisLabel2.setText("0");
            visualizationLabel2.setText("0");
        }
        else{
            for(int i=0; i<PO.getStages().size(); i++){
                int aux = 0;
                for(int j=0; j<PO.getStages().get(i).getPlugins().size();j++){
                    if(!PO.getStages().get(i).getPlugins().get(j).getConfigurations().isEmpty()){
                        System.out.println(PO.getStages().get(i).getPlugins().get(j).getConfigurations().size());
                        for(int k=0; k<PO.getStages().get(i).getPlugins().get(j).getConfigurations().size();k++){
                            if(aux==0){
                                aux++;
                            }
                            else{
                                aux++;
                            }
                        }
                    }                    
                }
                switch(i){
                    case 0:
                        captureLabel2.setText(String.valueOf(aux));
                        break;
                    case 1:
                        analysisLabel2.setText(String.valueOf(aux));
                        break;
                    case 2:
                        visualizationLabel2.setText(String.valueOf(aux));
                        break;
                }
            }
        }
    }
}


