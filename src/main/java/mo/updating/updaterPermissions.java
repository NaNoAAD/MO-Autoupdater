package mo.updating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class updaterPermissions {
    //Esta clase es la encargada de obtener los permisos correspondientes para actualizar MO

    //Metodo que permite obtener la version de MO indicada en el texto de notas de MO
    //Retorna la primera linea del archivo de notas de version "version=x.x.x.x"
    public static String getMOVersion(){
        String version = "";
        String versionString = "";
        Path file = Paths.get("../../../versionNotes.txt");
        List<String> lines;
        

        try {
            // Si el archivo de Notas de version no existe, se cancela la opcion de actualizar y se da el paso a ejecutar directamente MO
            if (!Files.exists(file)) {
                //Files.createFile(file);
                System.out.println("El archivo de Notas de version no existe \n");
                return version = "NULL";
            } else {
                System.out.println("El archivo de Notas exsite ya existe. Se Procede con Launcher\n");
                //Files.delete(file);
                //Files.createFile(file);
                //System.out.println("Se ha creado otro archivo 'Register.txt'");
                // Creating object of FileReader and BufferedReader  
                lines = Files.readAllLines(file);
                versionString = lines.get(0);
                String[] versionStringArray = versionString.split("=");
                version = versionStringArray[1];
                System.out.println("version indicada: " + version + "\n Lo demas indica esto");
                for(String line : lines){
                    System.out.println(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }        
        return version;
    }

}
