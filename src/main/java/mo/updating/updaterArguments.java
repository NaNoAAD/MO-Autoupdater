package mo.updating;

import java.nio.file.Paths;
import java.io.File;
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
    private  static String repositoryName;
    private  static String pathToExecuteWrapperGradle;
    private  static String pathToPluginRegisterFile;
    private  static String jarLocationInRepository;
    private  static String buildedJarFileName; 

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

    public static String getRepositoryName(){
        return repositoryName;
    }

    public static String getPathToExecuteWrapperGradle(){
        return pathToExecuteWrapperGradle;
    }

    public static String getPathToPluginRegisterFile(){
        return pathToPluginRegisterFile;
    }

    public static String getJarLocationInRepository(){
        return jarLocationInRepository;
    }

    public static String getBuildedJarFileName(){
        return buildedJarFileName;
    }


    /**
    public void setlocalVersionString(String argument){
        localVersionString = argument;
    }

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
    
    /**
     * Metodo que permite setear todas las variables globales necesarias para el funcionamiento del modulo launcher
     * @param arguments Lista de Strings que contienen los argumentos obtenidos desde saveArguments()
     * @return Un boolean que indica el resultado de la operacion
     */
    public static boolean setArguments(List<String> arguments){
        if (arguments.size() != 17) {
            return false;
        } else {
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
            repositoryName = arguments.get(12);
            pathToExecuteWrapperGradle = arguments.get(13);
            pathToPluginRegisterFile = arguments.get(14);
            jarLocationInRepository = arguments.get(15);
            buildedJarFileName = arguments.get(16);
            return true;
        }
        
    }

    /**
     * Metodo que permite el guardado de los argumentos desde un archivo .up 
     * En caso de que no exista el archivo, se abre MO y se cierra el launcher con codigo 1
     * @param filePath String con la direccion del archivo args.up
     * @return una lista de strings, lista para ser leida por el metodo setArguments()
     */
    public static List<String> saveArguments(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            List<String> preArgumentsList = new ArrayList<>();
            List<String> argumentsList = new ArrayList<>();
            try {
                Path path = Paths.get(filePath);
                preArgumentsList = Files.readAllLines(path);
                for (String line : preArgumentsList) {
                    String[] parts = line.split(": ", 2);
                    argumentsList.add(parts[1]);
                    System.out.println("(updaterArguments.java) - Añadido el argumento: " + parts[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return argumentsList;
        } else {
            System.out.println("(updaterArguments.java) - Archivo args.up no detectado - Cerrando Launcher - iniciando MO");
            updater.openMO();
            System.exit(1);
            return null;
        }
        
    }


} //Fin Clase updaterArguments
