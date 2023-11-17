package mo.updating;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Clase que permite la escritura del archivo .txt con las notas de version
 */
public class updaterVersionNotesRegister {
    
    /**Metodo que permite a traves de un string la creacion/sobreescritura de un nuevo archivo de notas de version
    *PAra ser usada con posterioridad
    *@param notes es el string escrito con las notas de la nueva version
    *@return void
    */
    public static void newNotesVersion(String notes){
        //Se considera la revision de la existencia de un nuevo archivo de notas de version
        Path file = Paths.get("versionNotes.txt");
        try {
            // Crea el archivo si no existe
            if (!Files.exists(file)) {
                Files.createFile(file);
                System.out.println("El registro de notas no existia, Se ha creado el archivo 'versionNotes.txt'\n");
            } else {
                System.out.println("El archivo 'versionNotes.txt' ya existe. Se borra para crear uno nuevo\n");
                Files.delete(file);
                Files.createFile(file);
                System.out.println("Se ha creado otro archivo 'versionNotes.txt\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Se crea y escribe el nuevo archivo de notas
        try{
            BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            try {
                //Escritura
                writer.write(notes);     
                writer.close();    
                //DEBUG: borrado de archivo
                Files.deleteIfExists(file);          
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
