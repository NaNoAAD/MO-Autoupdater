package mo.updating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class updaterPermissions {
    //Esta clase es la encargada de obtener los permisos correspondientes para actualizar MO

    //Metodo que permite obtener la version de MO indicada en el texto de notas de MO
    //Retorna la primera linea del archivo de notas de version "version=x.x.x.x"
    public static String getMOVersion(){
        String versionString = "";
        Path file = Paths.get("../../../version.txt");
        List<String> lines;        

        try {
            // Si el archivo de Notas de version no existe, se cancela la opcion de actualizar y se da el paso a ejecutar directamente MO
            if (!Files.exists(file)) {
                //Files.createFile(file);
                System.out.println("El archivo de version no existe \n");
                return versionString = "NULL";
            } else {
                System.out.println("El archivo de version ya existe. Se Procede con Launcher\n");
                //Files.delete(file);
                //Files.createFile(file);
                //System.out.println("Se ha creado otro archivo 'Register.txt'");
                // Creating object of FileReader and BufferedReader  
                lines = Files.readAllLines(file);
                versionString = lines.get(0);
                System.out.println("version indicada en version.txt local: " + versionString + "\n" );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        
        return versionString;
    }

    //Metodo que obtiene la version indicada en el txt remoto en el repositorio
    //Retorna el string que indica la version
    public static String remoteVersionRepository() throws IOException{

        String version;

        String RepoOwner = "NaNoAAD"; // Reemplaza con el dueño del repositorio
        String repoName = "MO-Autoupdater"; // Reemplaza con el nombre del repositorio
        //Token
        String a = "ghp_0D6Zmt";
        String b = "4sfGEZJzK7Fiutyfj6J";
        String c = "DizVO3CK3zW";
        String githubToken = a + b + c; 

        //Solicitud que se hace a la API de Github
        String apiUrl = String.format("https://raw.githubusercontent.com/%s/%s/master/version.txt", RepoOwner, repoName);
        
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Se ejecuta la conexion con los permisos otorgados por Token
        connection.setRequestProperty("Authorization", "token " + githubToken);
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        //Si la solicitud tiene exito (codigo 200)
        if (responseCode == 200) {

            System.out.println("Ingresamos sin problemas al Repositorio para buscar la version en txt remoto! \nLink: " + apiUrl + "\n");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //JSON respuesta con contenidos de la solicitud
            //La respuesta debera tener el contenido raw del txt de notas de version del repositorio
            String jsonResponse = reader.lines().collect(Collectors.joining());
            reader.close();

            System.out.println("JSON DE SOLICITUD A TXT REMOTO version.txt\n");
            System.out.println(jsonResponse.toString());
            
            //Se obtiene la version remota a traves del txt que lo indica
            version = jsonResponse.toString();
            
            System.out.println("DEL JSON SE OBTIENE: " + version + "\n");
            return version;
            
            
        } else {
            System.err.println("Error en la solicitud de obtencion de version remota. Codigo: " + responseCode);
            version = "NULL";
            return version;
        }
    }

    //Metodo que comparara 2 strings que indican versiones
    // true si es necesario actualizar
    //False en caso contrario
    public static boolean permissionToUpdateByVersions(String localVersion, String RemoteVersion) throws IOException{
        System.out.println("Versiones a comparar >> local " + localVersion + " " + "remoto: " + RemoteVersion + "\n");
        if(localVersion == "NULL" || RemoteVersion == "NULL"){
            System.out.println("localVersion o RemoteVersion indican NULL, no hay actualizacion\n");
            return false;
        } else if (localVersion.compareTo(RemoteVersion) < 0) {
            System.out.println("localVersion" + " es menor que " + "RemoteVersion\nSe procede a Actualizar\n");
            return true;
        } else if (localVersion.compareTo(RemoteVersion) > 0) {
            System.out.println("localVersion" + " es mayor que " + "RemoteVersion\n Innecesario Actualizar\n");
            return false;
        } else {
            System.out.println("Los txt indican versiones iguales, no se Actualiza\n");
            return false;
        }
    }


}
