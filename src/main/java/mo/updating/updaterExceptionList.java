package mo.updating;

/**
 * Clase que sirve para contener las palabras que los registros de archivos deben evitar obtener
 */
public class updaterExceptionList {
    
    public static final String[] fileNamesToAvoid = {"java\\mo\\updating",
    "Register.txt",
    "RemoteRegister.txt",
    "FileRegister.txt",
    "Repo.zip",
    ".gradle",
    ".git",
    "\\bin",
    "\\build",
    ".vscode",
    "preferences.xml"
    };
}
