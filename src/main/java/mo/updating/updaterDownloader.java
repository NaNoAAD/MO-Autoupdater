package mo.updating;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


//Clase que permite la descarga de los archivos remotos de Multimodal observer
public class updaterDownloader {
    //Variables
    //static String fileURL = "https://github.com/NaNoAAD/MO-Autoupdater/archive/refs/heads/master.zip";
    
    public static void downloadFilesFromRepository(boolean equalsVersionsTxt, boolean comparissonAnswer) throws IOException{
        //Se revisan los permisos dados por los resultados de las comparaciones
        if(equalsVersionsTxt == false && comparissonAnswer == true){
            System.out.println("No se cumplen los requisitos para actualizar\nSe vuelve a Updater main");
            return;
        } else {
            try {
                //Se descarga el codigo fuente del repositorio de github desde el link predeterminado
                System.out.println("Se descarga el zip desde el repositorio\n");
                URL website = new URL("https://github.com/NaNoAAD/MO-Autoupdater/archive/refs/heads/master.zip");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("Repo.zip");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                // Aca es probable colocar codigo para mover el archivo si es necesario
            } catch (IOException e){
                e.printStackTrace();
            }
            
        }
    }
}
