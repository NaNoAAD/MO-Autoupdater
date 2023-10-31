package mo.updating;

import java.util.Date;


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

}
