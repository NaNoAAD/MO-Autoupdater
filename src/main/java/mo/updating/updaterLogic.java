package mo.updating;

import java.io.IOException;
import java.lang.String;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Clase que contiene de manera general toa la logica del updater. Dividida en 3 metodos
 */
public class updaterLogic {

    /**
     * Primer metodo contendor de la logica del updater. Obtiene los primeros permisos para establecer si se puede o no
     *  actualizar Multimodal Observer
     * @return true si las comparaciones entre registros entre archivos y las versiones declaradas arrojan diferencias necesarias para actulizar
     */
    public static boolean updaterpermissionsLogic() {
        boolean permission1 = false;

        try {
            //Lectura de la version local indicada en version.txt
            String localVersion = updaterPermissions.getMOVersion();

            //Funcion que obtiene version.txt desde el repositorio, que sera anotado como Remoteregister.txt localmente
            String remoteVersion = updaterPermissions.remoteVersionRepository();

            //Se comparan las versiones declaradas en los txt de version (true == permiso para actualizar)
            permission1 = updaterPermissions.permissionToUpdateByVersions(localVersion, remoteVersion);

            } catch (IOException e) {
            e.printStackTrace();
        }
        //por defecto se arrojara false
        return permission1;
    }

    public static boolean updaterComparissonLogic(boolean permission1){
        try{
            //Se crea una lista con todas las rutas de los archivos locales de MO
            List<Path> PathFiles = updaterRegisterCreator.listPathFiles();

            //Se crea registro local usando todas las rutas
            updaterRegisterCreator.createRegisterFile(PathFiles);

            //Se crea registro de los archivos remotos en el repositorio correspondiente
            updaterRemoteFilesProcess.getRemoteFiles();

            //Se comparan los registros generados para verificar por segunda vez si corresponde actualizar (true == existen diferencias)
            boolean answer = updaterRegisterComparison.differencesInRegisters(Paths.get("Register.txt"), Paths.get("RemoteRegister.txt"));
            System.out.println("(updaterLogic.java) - Procesados los registros en arreglos! y las versiones\nEl permiso por comparar versiones es: " + String.valueOf(permission1) + "\nY las diferencias en los registros son: " + String.valueOf(answer));

            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("(updaterLogic.java) - Respuesta defecto: false");
        return false;

    }

    public static void updaterUpdatingLogic(boolean permission1, boolean answer){
        try {
            
            //Si las respuestas son las esperadas, se procede a la descarga de los archivos del repositorio en formato .zip desde link predeterminado
            boolean downloadZip = updaterDownloader.downloadFilesFromRepository(permission1, answer);

            //Se procede a reemplazar los archivos con los del comprimible
            updaterZipProcess.unzipFile("../../../Repo.zip", "../../../../", downloadZip);

            //Se acciona el comando que permite la ejecucion del wrapper de gradle
            updaterCommands.gradleBuildCommand();

            //Con el nuevo .jar creado, se verifica ue no hayan archivos sobrantes borrandolos
            updaterPostUpdateProcesses.deleteLeftoversFiles();

            //Se crea un nuevo archivo de nota
            //Este metodo esta listo para recibir un string y ser ocupado para generar/sobreescribir un nuevo archivo de notas de version
            //updaterVersionNotesRegister.newNotesVersion("Hola\nSoy un nuevo archivo y poseo nuevas notas\nUna de mis cualidades sera\n\nTener nuevas caracteristicas!");

            //Se prueba la obtencion del archivo de notas remoto
            //updaterVersionNotesRegister.getRemoteVersionNotes();

            // Ruta al archivo JAR MO
            //String Mo = "multimodal-observer-server-5-0.0.0"; // Reemplazar con la ruta correcta

            // ejecución del archivo JAR MO
            //String command = "java -jar " + Mo + ".jar";

            // Iniciar MO
            //Process process = Runtime.getRuntime().exec("java -jar multimodal-observer-server-5-0.0.0.jar");

            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


