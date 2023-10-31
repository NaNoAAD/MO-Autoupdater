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

            updaterPermissions.versionComparison(localVersion, remoteVersion);

            //Se crea una lista con todas las rutas de los archivos locales de MO
            List<Path> PathFiles = updaterRegisterCreator.listPathFiles();

            //Se crea registro local usando todas las rutas
            updaterRegisterCreator.createRegisterFile(PathFiles);

            //Se crea registro de los archivos remotos en el repositorio correspondiente
            updaterRemoteFilesProcess.getRemoteFiles();

            //Se comparan los registros generados para verificarpor segunda vez si corresponde actualizar
            String answer = String.valueOf(updaterRegisterComparison.compareRegisterFiles(Paths.get("Register.txt"), Paths.get("remoteRegister.txt")));
            System.out.println("Procesados los registros en arreglos!\nLa respuesta por ahora es : " + answer + "\n");

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


