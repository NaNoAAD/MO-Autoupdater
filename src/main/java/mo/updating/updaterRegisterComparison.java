package mo.updating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import mo.updating.fileClass;

public class updaterRegisterComparison {

    //METODO
    //Compara 2 archivos con igual formato, para obtener respuesta a si los archivos remotos han sido modificados
    //Retorna un boolean que permite proceder con posterioridad
    ////False si son diferentes
    ////True si son iguales
    public static boolean compareRegisterFiles(Path localFile, Path remoteFile) throws IOException {
        //Declaro variable de salida
        boolean answer = false;
        //Variables limitadoras
        int evenOdd = 0;
        int index = 0;
        String fileName = "";
        //Se guarda todo el contenido respectivo de los archivos en bruto como strings
        List<String> localFileContent = Files.readAllLines(localFile);
        List<String> remoteFileContent = Files.readAllLines(remoteFile);

        //Se inicializan listas date para guardar las fechas de los registros
        List<Date> localFileDates = new ArrayList<>();
        List<Date> remoteFileDates = new ArrayList<>();

        //Se inicializan listas de string para guardar los nombres los archivos
        List<String> localFileNames = new ArrayList<>();
        List<String> remoteFileNames = new ArrayList<>();

        //Se inicializan listas con indices para procesado
        List<Integer> localIndexs = new ArrayList<>();
        List<Integer> remoteIndexs = new ArrayList<>();

        //Se inicializa una lista con objetos File
        ArrayList<fileClass> localFileArray = new ArrayList<>();
        ArrayList<fileClass> remoteFileArray = new ArrayList<>();

        //Se inicializa una lista con los formatos de fecha para su procesado
        Date date = new Date();
        //Se inicializa una variable que permita procesar las fechas que estan como strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //Para cada registro, se tomaran los pares como archivos, y los impares como ultimas fechas de modificacion siguiendo el formato de los registros
        //Se usara la clase fileclass propia de updater para manejar y procesar la informacion
        System.out.println("Se procesa el registro local en arreglos\n");
        try {
            for (String content : localFileContent) {
                if(evenOdd == 0){
                    fileName = content;
                    evenOdd += 1;
                }else {                    
                    date = dateFormat.parse(content);
                    evenOdd -= 1;
                    fileClass file = new fileClass(fileName, date);
                    localFileArray.add(file);
                    index += 1;
                }
            } 

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //IDEM con el registro local, pero ahora ocupando lo remoto
        System.out.println("Se procesa el registro remoto en arreglos\n");
        index = 0;
        try {
            for (String content : remoteFileContent) {
                if(evenOdd == 0){
                    fileName = content;
                    evenOdd += 1;
                }else {                    
                    date = dateFormat.parse(content);
                    evenOdd -= 1;
                    fileClass file = new fileClass(fileName, date);
                    localFileArray.add(file);
                    index += 1;
                }
            } 

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Primera regla: Los registros deben llevar la misma cantidad de archivos
        //Caso contrario: (por ahora) no tenemos formas de saber como afectara a la compilacion de mo
        if(!(localFileArray.size() == remoteFileArray.size())){
            return answer = true;
        }

        //Segunda regla: Si anteriormente los largos son iguales, se debe revisar que todos los archivos locales estan en el repositorio
        for (fileClass fileRevisor :localFileArray) {
            if(remoteFileArray.contains(fileRevisor)){
                continue;
            } else{
                return answer = true;
            }
        }

        //Tercera regla; Si los largo son iguales, si todos los archivos locales estan, entonces 
        //Con la informacion de los arreglos de fileclass en posesion, se procede a generar la comparacion de los archivos segun su nombre y fecha de modificacion 
        //Aprovechando que estan en arreglos, tienen metodos get y los arreglos les dan indice
        //index = 0;
        //for (fileClass fileRevisor : localFileArray) {

        //}
    

        return answer;
    }
}
