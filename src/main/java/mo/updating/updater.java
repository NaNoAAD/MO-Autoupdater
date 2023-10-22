package mo.updating;

import java.io.IOException;
import java.lang.String;
import java.nio.file.Path;
import java.util.List;

public class updater {

    //METODO MAIN
    public static void main(String[] args) {
        try {

            //Creo que es mejor ter un txt con su infomacion
            String localVersion = updaterPermissions.getMOVersion();

            //Se crea una lista con todas las rutas de los archivos locales de MO
            List<Path> PathFiles = updaterRegisterCreator.listPathFiles();

            //Se crea registro usando todas las rutas
            updaterRegisterCreator.createRegisterFile(PathFiles);

            //Se crea registro de los archivos remotos en el repositorio correspondiente
            updaterRemoteFilesProcess.getRemoteFiles();

            //ocupar PathFiles

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


