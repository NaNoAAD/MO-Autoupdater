//package mo.updating;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.String;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class updater {




    //METODO
    //obtiene los directorios de todos los archivos de MO presentes en carpeta "src"
    //Retorna una lista de PATH's
    public static List<Path> listPathFiles() {
        //Declaro variable de salida
        List<Path> pathList = new ArrayList<>();
        //formo variable con directorio de inicio
        Path startDir = Paths.get("../../../..");  // replace with your directory path
        //coleccionar todos los archivos desde inicio declarado
        try {
            pathList = Files.walk(startDir)
                                        .filter(Files::isRegularFile)
                                        .collect(Collectors.toList());

            pathList.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathList;
    }

    //METODO
    //Escribe en un archivo todos los archivos y su fecha de modificacion

    public static void createRegisterFile(List<Path> pathList){
        Path file = Paths.get("Register.txt");

        try {
            // Crea el archivo si no existe
            if (!Files.exists(file)) {
                Files.createFile(file);
                System.out.println("Se ha creado el archivo 'Register.txt' con lo necesario");
            } else {
                System.out.println("El archivo 'Register.txt' ya existe. Se borra para crear uno nuevo");
                Files.delete(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Abre el archivo en modo de agregar (APPEND)
            BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    
            for (Path pathfile : pathList) {
                try {
                    BasicFileAttributes attributes = Files.readAttributes(pathfile, BasicFileAttributes.class);
                    long milisegundos = attributes.lastModifiedTime().toMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String modificationDate = sdf.format(milisegundos);
    
                    // Escribe en el archivo
                    writer.write(pathfile.toString());
                    writer.newLine();
                    writer.write(modificationDate);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    
            // Cierra el archivo
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //METODO MAIN
    public static void main(String[] args) {
        try {

            List<Path> PathFiles = listPathFiles();
            createRegisterFile(PathFiles);

            //ocupar PathFiles

            // Ruta al archivo JAR MO
            String Mo = "multimodal-observer-server-5-0.0.0"; // Reemplaza con la ruta correcta

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


