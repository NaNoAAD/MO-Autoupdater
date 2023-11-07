package mo.updating;

import java.io.IOException;
import java.lang.String;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class updater {

    //METODO MAIN
    public static void main(String[] args) {
        try {

            //Creo que es mejor ter un txt con su infomacion
            String localVersion = updaterPermissions.getMOVersion();

            //Funcion que obtiene txt remoto
            String remoteVersion = updaterPermissions.remoteVersionRepository();

            //Se comparan las versiones declaradas en los txt de version (true == necesario actualizar)
            boolean permission1 = updaterPermissions.versionComparison(localVersion, remoteVersion);

            //Se crea una lista con todas las rutas de los archivos locales de MO
            List<Path> PathFiles = updaterRegisterCreator.listPathFiles();

            //Se crea registro local usando todas las rutas
            updaterRegisterCreator.createRegisterFile(PathFiles);

            //Se crea registro de los archivos remotos en el repositorio correspondiente
            updaterRemoteFilesProcess.getRemoteFiles();

            //Se comparan los registros generados para verificarpor segunda vez si corresponde actualizar (true == iguales)
            boolean answer = updaterRegisterComparison.compareRegisterFiles(Paths.get("Register.txt"), Paths.get("RemoteRegister.txt"));
            System.out.println("Procesados los registros en arreglos!\nLa respuesta por ahora es : " + String.valueOf(answer) + "\nY en las versiones declaradas es: " + String.valueOf(permission1));

            //Si las respuestas son las esperadas, se procede a la descarga de los archivos del repositorio
            updaterDownloader.downloadFilesFromRepository(permission1, answer);

            // Ruta al archivo JAR MO
            String Mo = "multimodal-observer-server-5-0.0.0"; // Reemplazar con la ruta correcta

            // ejecución del archivo JAR MO
            //String command = "java -jar " + Mo + ".jar";

            // Iniciar MO
            Process process = Runtime.getRuntime().exec("java -jar multimodal-observer-server-5-0.0.0.jar");

            


            // Esperar a que el proceso termine (en este caso, nunca terminará)
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


