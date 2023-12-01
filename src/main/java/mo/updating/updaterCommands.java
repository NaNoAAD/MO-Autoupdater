package mo.updating;

import java.io.IOException;

/**
 * Clase publica que permite la ejecucion de comandos en el sistema operativo presente
 */
public class updaterCommands {
    
    /**
     * Metodo que permite la ejecucion del comando build del wrapper de gradle dependiendo de que sistema operativo este en uso
     * @return void
     */
    static void gradleBuildCommand(){
        //Se obtiene el sistema operativo
        String operativeSystem = System.getProperty("os.name").toLowerCase();

        //Caso 1: el OS es Windows
        if(operativeSystem.contains("win")){
            //Se genera una instancia que se encarga de ejecutar cmd y accionar el .bat
            try {
            ProcessBuilder processB = new ProcessBuilder("cmd.exe", "/c", "gradlew.bat build");
            System.out.println("Ejecutando : gradlew.bat build en OS: " + operativeSystem + " en directorio: " + System.getProperty("user.dir"));
            Process process = processB.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            }
        } else if (operativeSystem.contains("nix") || operativeSystem.contains("nux") || operativeSystem.contains("mac")){
            //comando para Linux o Unix
            try {
                ProcessBuilder process = new ProcessBuilder("bash", "-c", "gradlew build");
                System.out.println("Ejecutando : gradlew build en OS: " + operativeSystem);
                Process proceso = process.start();
                proceso.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Sistema operativo no compatible con el comando");
        }
    }


}
