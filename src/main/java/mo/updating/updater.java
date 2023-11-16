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

            //Lectura de la version local indicada en version.txt
            String localVersion = updaterPermissions.getMOVersion();

            //Funcion que obtiene version.txt desde el repositorio, que sera anotado como Remoteregister.txt localmente
            String remoteVersion = updaterPermissions.remoteVersionRepository();

            //Se comparan las versiones declaradas en los txt de version (true == permiso para actualizar)
            boolean permission1 = updaterPermissions.permissionToUpdateByVersions(localVersion, remoteVersion);

            //Se crea una lista con todas las rutas de los archivos locales de MO
            List<Path> PathFiles = updaterRegisterCreator.listPathFiles();

            //Se crea registro local usando todas las rutas
            updaterRegisterCreator.createRegisterFile(PathFiles);

            //Se crea registro de los archivos remotos en el repositorio correspondiente
            updaterRemoteFilesProcess.getRemoteFiles();

            //Se comparan los registros generados para verificar por segunda vez si corresponde actualizar (true == existen diferencias)
            boolean answer = updaterRegisterComparison.differencesInRegisters(Paths.get("Register.txt"), Paths.get("RemoteRegister.txt"));
            System.out.println("Procesados los registros en arreglos! y las versiones\nEl permiso por comparar versiones es: " + String.valueOf(permission1) + "\nY las diferencias en los registros son: " + String.valueOf(answer));

            //Si las respuestas son las esperadas, se procede a la descarga de los archivos del repositorio en formato .zip desde link predeterminado
            boolean downloadZip = updaterDownloader.downloadFilesFromRepository(permission1, answer);

            //Se procede a reemplazar los archivos con los del comprimible
            updaterZipProcess.unzipFile("../../../Repo.zip", "../../../../", downloadZip);

            //Se acciona el comando que permite la ejecucion del wrapper de gradle
            updaterCommands.gradleBuildCommand();

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


