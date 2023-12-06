package mo.updating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Clase que se escarga del manejo de actualizaciones de los plugins de Multimodal Observer
 */
public class updaterPluginsUpdating {

    /**
     * Metodo que lee el registro de plugins bajo supervision a actualizar
     * @param registerFile es la ruta Path del registro .txt que lista los plugins registrados
     * @return void
     */
    public static void readPluginsToUpdate(Path registerFile){
        //Se confirma la existencia del registro de plugins a supervisar
        try {
            // Si el archivo no existe, no es posible saber que plugins pueden/deben actualizarse
            if (!Files.exists(registerFile)) {
                System.out.println("(updaterPluginsUpdating.java) - El registro de plugins a supervisar no existe\n Se omite la revision de los Plugins");
                Files.deleteIfExists(registerFile);
            } else {
                System.out.println("(updaterPluginsUpdating.java) - El registro de plugins a supervisar existe\n Se revisa si el formato es el adecuado");
                Files.deleteIfExists(registerFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
