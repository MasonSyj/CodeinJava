package edu.uob;

import java.io.File;

public class CreateDBCmd extends Command{
    public CreateDBCmd(String DBname){
        super(DBname, null);
    }

    public String execute(){
        try{
            if (SQLKeywords.SQLKeyWords.contains(getDBName().toUpperCase())){
                return "[ERROR], Database Name cannot be SQL Keywords.";
            }
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

