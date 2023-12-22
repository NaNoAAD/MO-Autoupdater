package mo.updating;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se escarga del manejo de actualizaciones de los plugins de Multimodal Observer
 */
public class updaterPluginsUpdating {

    public static List<String> pluginName = new ArrayList<>();
    public static List<String> upFile = new ArrayList<>();



    public static void setUpFilesList(List<String> uPListIncoming){
        upFile.addAll(uPListIncoming);
    }

    /**
     * Metodo que lee el registro de plugins bajo supervision a actualizar
     * @param registerFile es la ruta Path del registro .txt que lista los plugins registrados
     * @return void
     */
    /**
     * Metodo que lee el registro de plugins bajo supervision a actualizar
     * @param filePath String que indica la ruta del archivo donde obtener la lista de archivos .up a trabajar
     * @return Una lista con todas las rutas relativas a todos los archivos .up que serviran para actualizar los plugins registrados
     */
    public static List<String> getUpFilePluginsToUpdate(String filePath) {
        List<String> prePluginsList = new ArrayList<>();
        List<String> PluginList = new ArrayList<>();
        try {
            Path path = Paths.get(filePath);
            prePluginsList = Files.readAllLines(path);
            for (String line : prePluginsList) {
                String[] parts = line.split(": ", 2);
                pluginName.add(parts[0]);
                PluginList.add("./ups/" + parts[1]);
                System.out.println("(updaterArguments.java) - obtenido el .up de nombre: " + parts[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PluginList;
    }

    public static boolean updaterComparissonLogicPlugin(boolean permission1, String aToken, String bToken, String cToken, String remoteRegisterApiUrl, String pathToPluginRegisterFile){
        try{
            //Se crea registro de los archivos remotos en el repositorio correspondiente
            updaterRemoteFilesProcess.getRemoteFiles(aToken, bToken, cToken, remoteRegisterApiUrl);

            //Se comparan los registros generados de manera interna para verificar por segunda vez si corresponde actualizar (true == existen diferencias)
            //Este metodo ocupa el mismo metodod para actualizar mo, pero en modo plugin, esto es, que no borrara el registro local, dado que en este caso, el registro
            //no es creado por la app, si no que es recogido desde la carpeta ups, y logicamente, no queremos borrarlo, a deferencia del otro.
            boolean answer = updaterRegisterComparison.differencesInRegisters(Paths.get(pathToPluginRegisterFile), Paths.get("./RemoteRegister.txt"), "plugin");
            System.out.println("(updaterPluginUpdater.java) - Procesados los registros en arreglos! y las versiones\n--- El permiso por comparar versiones es: " + String.valueOf(permission1) + "\n--- Y las diferencias en los registros son: " + String.valueOf(answer));
            
            //Si updater genero booleans que no permiten la actualizacion, se borra el registros generados internamente
            updaterRegisterComparison.deleteFilesIfNotPermission(permission1, answer, Paths.get("./RemoteRegister.txt") );

            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("(updaterLogic.java) - Respuesta defecto: false");
        return false;

    }

    public static void moveJarToPluginsFolder(String origin, String target){
        Path originPath = Paths.get(origin);
        Path targetPath = Paths.get(target);
        try {
            Files.move(originPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("(updaterPluginsUpdating.java) - archivo Jar movido desde " + originPath + " a " + targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeFirstsElements(List<String> upList, List<String> nameList){
        if (upList.size() != 0 && nameList.size() != 0) {
            upList.remove(0);
            nameList.remove(0);
        }
    }
     
    
}
