package mo.updating;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Clase que obtiene el registro de archivos remotos desde Github
 */
public class updaterRemoteFilesProcess {

    //

    /**
     * Metodo que obtiene una lista de todos los archivos de un commit desde un repositorio de github publico usando un token especifico.
     * @throws IOException
     */
    public static void getRemoteFiles() throws IOException {


        String RepoOwner = "NaNoAAD"; // Reemplazar con el dueño del repositorio
        String repoName = "MO-Autoupdater"; // Reemplazar con el nombre del repositorio
        //Token
        String a = "ghp_0D6Zmt";
        String b = "4sfGEZJzK7Fiutyfj6J";
        String c = "DizVO3CK3zW";
        String githubToken = a + b + c; 

        String apiUrl = String.format("https://raw.githubusercontent.com/%s/%s/master/FileRegister.txt", RepoOwner, repoName);
        
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Se ejecuta la conexion con los permisos otorgados por Token
        connection.setRequestProperty("Authorization", "token " + githubToken);
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        //Si la solicitud tiene exito (codigo 200)
        if (responseCode == 200) {

            System.out.println(" (updaterRemoteFilesProcess) Ingresamos sin problemas al Repositorio! \nLink: " + apiUrl + "\n");
            
            //Se genera un buffer que leera la respuesta de la solicitud de la API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //JSON respuesta con contenidos de la solicitud, unidos como string y delimitados por salto de linea
            String jsonResponse = reader.lines().collect(Collectors.joining("\n"));
            reader.close();

            //DEBUG: print mostrara como el json fue capturado
            //System.out.println(jsonResponse);

            Path registerPath = Paths.get("RemoteRegister.txt");

            if (Files.exists(registerPath)) {
                try {
                    //Si archivo existe, se borra y se crea de nuevo
                    System.out.println("Registro remoto existia localmente\n");
                    Files.delete(registerPath);
                    FileWriter fileWriter = new FileWriter("RemoteRegister.txt");
                    fileWriter.write(jsonResponse);
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //Caso contrario, simplemente se crea
                System.out.println("Registro remoto no existia localmente\n");
                FileWriter fileWriter = new FileWriter("RemoteRegister.txt");
                fileWriter.write(jsonResponse);
                fileWriter.close();
            }          

        } else {
            System.err.println("Error en la solicitud. Codigo: " + responseCode);
        }
    }
}
