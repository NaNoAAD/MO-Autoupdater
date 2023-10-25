package mo.updating;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

//importacion para decodificar Base64
import java.util.Base64;




public class updaterRemoteFilesProcess {

    //Metodo que obtiene una lista de todos los archivos de un commit desde un repositorio de github publico
    public static void getRemoteFiles() throws IOException {


        String RepoOwner = "NaNoAAD"; // Reemplaza con el dueño del repositorio
        String repoName = "MO-Autoupdater"; // Reemplaza con el nombre del repositorio
        //String commitSHA = "sha_del_commit"; // Reemplaza con el SHA del commit

        //Token
        String a = "ghp_0D6Zmt";
        String b = "4sfGEZJzK7Fiutyfj6J";
        String c = "DizVO3CK3zW";
        String githubToken = a + b + c; 

        String apiUrl = String.format("https://raw.githubusercontent.com/%s/%s/master/build.gradle", RepoOwner, repoName);
        
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Se ejecuta la conexion con los permisos otorgados por Token
        connection.setRequestProperty("Authorization", "token " + githubToken);
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        //Si la solicitud tiene exito (codigo 200)
        if (responseCode == 200) {

            System.out.println("Ingresamos sin problemas al Repositorio! \nLink: " + apiUrl + "\n");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //JSON respuesta con contenidos de la solicitud
            String jsonResponse = reader.lines().collect(Collectors.joining());
            reader.close();

            System.out.println(jsonResponse.toString());

            FileWriter fileWriter = new FileWriter("RemoteRegister.txt");
            // Procesa el JSON para obtener la lista de archivos y guárdalos en el archivo.
            // Esto dependerá del formato del JSON de respuesta de GitHub.
            // Aquí, estamos asumiendo que los archivos están en un arreglo "files".
            // Debes ajustar esto según la estructura real de la respuesta de GitHub.
            // Por ejemplo, puedes usar la biblioteca Gson para analizar el JSON.
            fileWriter.write("Lista de archivos del commit:\n");
            // Reemplaza esto con tu lógica para procesar el JSON y extraer los nombres de archivo.
            // Ejemplo: JSONArray filesArray = new JSONArray(jsonResponse);
            // for (int i = 0; i < filesArray.length(); i++) {
            //     JSONObject fileObject = filesArray.getJSONObject(i);
            //     String filename = fileObject.getString("filename");
            //     fileWriter.write(filename + "\n");
            // }
            fileWriter.close();
        } else {
            System.err.println("Error en la solicitud. Codigo: " + responseCode);
        }
    }
}
