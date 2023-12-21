package mo.updating;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se escarga del manejo de actualizaciones de los plugins de Multimodal Observer
 */
public class updaterPluginsUpdating {

    public static List<String> plugin;
    public static List<String> upFile = new ArrayList<>();


    public static void setUpFilesList(List<String> uPListIncoming){
        upFile.addAll(uPListIncoming);
    }

    /**
     * Metodo que lee el registro de plugins bajo supervision a actualizar
     * @param registerFile es la ruta Path del registro .txt que lista los plugins registrados
     * @return void
     */
    public static List<String> getUpFilePluginsToUpdate(String filePath) {
        List<String> prePluginsList = new ArrayList<>();
        List<String> PluginList = new ArrayList<>();
        try {
            Path path = Paths.get(filePath);
            prePluginsList = Files.readAllLines(path);
            for (String line : prePluginsList) {
                String[] parts = line.split(": ", 2);
                PluginList.add(parts[1]);
                System.out.println("(updaterArguments.java) - obtenido el .up de nombre: " + parts[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PluginList;
    }
     
    
}
