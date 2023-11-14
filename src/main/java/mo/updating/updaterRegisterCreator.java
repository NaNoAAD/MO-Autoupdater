package mo.updating;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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

public class updaterRegisterCreator {
    //METODO
    //obtiene los directorios de todos los archivos de MO presentes en carpeta "src"
    //Retorna una lista de PATH's
    public static List<Path> listPathFiles() {
        //Declaro variable de salida
        List<Path> pathList = new ArrayList<>();

        //formo variable con directorio de inicio
        //Path startDir = Paths.get("../../../..");  // Atencion, este directorio solo servia con el programa no modulado
        Path startDir = Paths.get("../../../");  // Nuevo directorio, para ejecutar usar el comando "java mo.updating.updater" desde la carpeta .../main/java, el programa "partira" aca y no donde este el .class
        
        //coleccionar todos los archivos desde inicio declarado
        try {
            pathList = Files.walk(startDir)
                                        .filter(Files::isRegularFile)
                                        .collect(Collectors.toList());
            //DEBUG: Se muestran todos los path recolectados via cmd
            //pathList.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathList;
    }

    //METODO
    //Escribe en un archivo todos los archivos y su fecha de modificacion, sin contar los excluidos en la funcion misma. Se utiliza un limitador para no escribir un \n al final
    public static void createRegisterFile(List<Path> pathList){
        Path file = Paths.get("Register.txt");

        try {
            // Crea el archivo si no existe
            if (!Files.exists(file)) {
                Files.createFile(file);
                System.out.println("El registro no existia, Se ha creado el archivo 'Register.txt' con lo necesario\n");
            } else {
                System.out.println("El archivo 'Register.txt' ya existe. Se borra para crear uno nuevo\n");
                Files.delete(file);
                Files.createFile(file);
                System.out.println("Se ha creado otro archivo 'Register.txt\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Abre el archivo en modo de agregar (APPEND)
            BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
    
            for (Path pathfile : pathList) {
                //Se anotan de manera local todos los directorios y fechas de modificacion mientras no contengan las frases en el IF siguiente
                try {
                    if ( !(pathfile.toString().contains("java\\mo\\updating")) && !(pathfile.toString().contains("Register.txt")) && !(pathfile.toString().contains("RemoteRegister.txt")) && !(pathfile.toString().contains("FileRegister.txt")) && !(pathfile.toString().contains("Repo.zip")) &&
                    !(pathfile.toString().contains(".gradle")) && !(pathfile.toString().contains(".git")) ){
                        BasicFileAttributes attributes = Files.readAttributes(pathfile, BasicFileAttributes.class);
                        long milisegundos = attributes.lastModifiedTime().toMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String modificationDate = sdf.format(milisegundos);

                        // Escribe en el archivo siguiendo el limitador
                        writer.write(pathfile.toString());
                        writer.newLine();
                        writer.write(modificationDate);
                        writer.newLine();
                    }                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    
            // Cierra el archivo para guardar cambios
            writer.close();
            System.out.println("Eliminando ultimo salto de linea\n");
            //Lo volvemos a abrir para eliminar el ultimo salto de linea y evitar insconsistencias
            File file2 = new File("Register.txt");
            RandomAccessFile raf = new RandomAccessFile(file2, "rw");
            long size = raf.length();
            if (size > 0) {
                raf.setLength(size - 2);
                }
            raf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
