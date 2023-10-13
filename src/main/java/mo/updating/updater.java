package mo.updating;

import java.io.IOException;
import java.lang.String;
import java.nio.file.Path;
import java.util.List;


public class updater {

    //METODO MAIN
    public static void main(String[] args) {
        try {

            //Se crea una lista con todas las rutas de los archivos locales de MO
            List<Path> PathFiles = updaterRegisterCreator.listPathFiles();

            //Se crea registro usando todas las rutas
            updaterRegisterCreator.createRegisterFile(PathFiles);

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


