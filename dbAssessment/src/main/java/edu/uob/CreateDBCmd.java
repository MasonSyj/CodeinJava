package edu.uob;

import java.io.File;

public class CreateDBCmd extends Command{
    public CreateDBCmd(String DBname){
        super(DBname, null);
        this.execute();
    }

    public String execute(){
        try{
            File theDir = new File("databases" + File.separator + getDBName());
//            File theDir = new File(getDBName());
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            return "[OK], Database created successfully";
        }catch (Exception e){
            return "[ERROR], Database failed to create.";
        }
    }

}

