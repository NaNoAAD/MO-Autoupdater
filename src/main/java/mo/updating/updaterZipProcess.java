package mo.updating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class updaterZipProcess {
    //Variables iniciales
    //String zipPath = "ruta/del/archivo.zip"; 
    //static String targetDirectory = "ruta/de/destino";

    //Metodo que descomprime l .zip descargado desde el repositorio
    //Sin retorno explicito
    public static void unzipFile(String zipPath, String targetDirectory, boolean permissionFromDownload) throws IOException {
        //Se revisa si el archivo .zip fue descargado
        if (permissionFromDownload == false){
            System.out.println("El proceso de unzipping no tiene los permisos\n");
            return;
        }

        // En caso de que por algun motivo no exista el directorio, se crea
        File dir = new File(targetDirectory);
        if(!dir.exists()) dir.mkdirs();

        //buffer para lectura y escritura de los contenidos del zip
        byte[] buffer = new byte[1024];

        // A traves de este bloque try, nos aseguramos que se cierren los recursos usados
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            //Obtenemos el contenido del zip
            ZipEntry zipEntry = zis.getNextEntry();
            //Uno por uno hasta que no sea null
            while (zipEntry != null) {
                //obteniendo su nombre se crea un nuevo File
                String fileName = zipEntry.getName();
                File newFile = new File(targetDirectory, fileName);

                if (zipEntry.isDirectory()) {
                    //Si lo procesado resulta ser un directorio, se crea un igual como tal
                    System.out.println("Unzipping Directorio: " + fileName);
                    newFile.mkdirs();
                } else {
                    //Caso contrario, es un archivo y con su nombre se procede a crear el mismo
                    //Primero, se creara un directorio padre en caso de que no exista
                    new File(newFile.getParent()).mkdirs();
                    //Y con necesario, se crea el nuevo archivo hasta agotar los bytes del buffer de lectura
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        System.out.println("Unzipping Archivo: " + fileName);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                //Se cierra el ZipEntry y se continua al siguiente archivo del .zip
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("El proceso de unzipping ha terminado\n");
    }

}
