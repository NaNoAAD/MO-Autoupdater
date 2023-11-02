package mo.updating;

import java.util.Date;


//Clase publica que permite el manejo de lso archivos que se procesan desde el arreglo de archivos obtenidos desde los registros txt
public class fileClass {
    private String name;
    private Date modificationDate;

    public fileClass(String newFileName, Date newFileModificationDate){
        this.name = newFileName;
        this.modificationDate = newFileModificationDate;
    }

    public String getName(){
        return name;
    }

    public Date getDate(){
        return modificationDate;
    }

    public static boolean isSameFile(fileClass file1, fileClass file2){
        if(file1.getName().equals(file2.getName()) && file1.getDate().equals(file2.getDate())){
            return true;
        } else {
            return false;
        }
    }

}
