package edu.uob;

import java.io.File;

public class CreateDBCmd extends Command{
    public CreateDBCmd(String DBname){
        super(DBname, null);
        this.execute();
    }

    public String execute(){
        try{
            File theDir = new File(getDBName());
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            return "[OK]";
        }catch (Exception e){
            String str = "[ERROR], Create Database Command Failed.";
            return str;
        }
    }

}

