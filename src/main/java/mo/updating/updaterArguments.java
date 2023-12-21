package mo.updating;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class updaterArguments {

    private  static String localVersionString;
    private  static String aToken;
    private  static String bToken;
    private  static String cToken;
    private  static String remoteVersionApiUrl;
    private  static String startDirRegister;
    private  static String remoteRegisterApiUrl;
    private  static String remoteNotesApiUrl;
    private  static String downloadLinkZip;
    private  static String targetDirectoryToMoveZip;
    private  static String zipDownloadedPath;
    private  static String targetDirectoryToExtract;
    private  static String pathToExecuteWrapperGradle;

    public static String getLocalVersionString(){
        return localVersionString;
    }

    public static String getAToken(){
        return aToken;
    }

    public static String getBToken(){
        return bToken;
    }

    public static String getCToken(){
        return cToken;
    }

    public static String getRemoteVersionApiUrl(){
        return remoteVersionApiUrl;
    }

    public static String getStartDirRegister(){
        return startDirRegister;
    }

    public static String getRemoteRegisterApiUrl(){
        return remoteRegisterApiUrl;
    }

    public static String getRemoteNotesApiUrl(){
        return remoteNotesApiUrl;
    }

    public static String getDownloadLinkZip(){
        return downloadLinkZip;
    }

    public static String getTargetDirectoryToMoveZip(){
        return targetDirectoryToMoveZip;
    }

    public static String getZipDownloadedPath(){
        return zipDownloadedPath;
    }

    public static String getTargetDirectoryToExtract(){
        return targetDirectoryToExtract;
    }

    public static String getPathToExecuteWrapperGradle(){
        return pathToExecuteWrapperGradle;
    }

    public void setlocalVersionString(String argument){
        localVersionString = argument;
    }

    /**
    public void setAToken(String argument){
        aToken = argument;
    }

    public void setBToken(String argument){
        bToken = argument;
    }

    public void setCToken(String argument){
        cToken = argument;
    }

    public void setRemoteVersionApiUrl(String argument){
        this.remoteVersionApiUrl = argument;
    }

    public void setStartDirRegister(String argument){
        this.startDirRegister = argument;
    }

    public void setRemoteRegisterApiUrl(String argument){
        this.remoteRegisterApiUrl = argument;
    }

    public void setRemoteNotesApiUrl(String argument){
        this.remoteNotesApiUrl = argument;
    }

    public void setDownloadLinkZip(String argument){
        this.downloadLinkZip = argument;
    }

    public void setTargetDirectoryToMoveZip(String argument){
        this.targetDirectoryToMoveZip = argument;
    }

    public void setZipDownloadedPath(String argument){
        this.zipDownloadedPath = argument;
    }

    public void setTargetDirectoryToExtract(String argument){
        this.targetDirectoryToExtract = argument;
    }

    public void setPathToExecuteWrapperGradle(String argument){
        this.pathToExecuteWrapperGradle = argument;
    }
    **/
    

    public static void setArguments(List<String> arguments){
        localVersionString = arguments.get(0);
        aToken = arguments.get(1);
        bToken = arguments.get(2);
        cToken = arguments.get(3);
        remoteVersionApiUrl = arguments.get(4);
        startDirRegister = arguments.get(5);
        remoteRegisterApiUrl = arguments.get(6);
        remoteNotesApiUrl = arguments.get(7);
        downloadLinkZip = arguments.get(8);
        targetDirectoryToMoveZip = arguments.get(9);
        zipDownloadedPath = arguments.get(10);
        targetDirectoryToExtract = arguments.get(11);
        pathToExecuteWrapperGradle = arguments.get(12);
    }

    public static List<String> saveArguments(String filePath) {
        List<String> preArgumentsList = new ArrayList<>();
        List<String> argumentsList = new ArrayList<>();
        try {
            Path path = Paths.get(filePath);
            preArgumentsList = Files.readAllLines(path);
            for (String line : preArgumentsList) {
                String[] parts = line.split(": ", 2);
                argumentsList.add(parts[1]);
                System.err.println("(updaterArguments.java) - Añadido el argumento: " + parts[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return argumentsList;
    }


} //Fin Clase updaterArguments
