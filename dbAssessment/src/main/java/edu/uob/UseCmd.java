package edu.uob;

import java.io.File;
import java.nio.file.Files;

public class UseCmd extends Command{

    public UseCmd(String DBname){
        super(DBname, null);
        execute();
    }

    @Override
    public String execute() {
        File file = new File(getDBName());
        if (file.isDirectory()){
            return getDBName() + " exists";
        }else {
            throw new RuntimeException(getDBName() + " doesn't exist");
        }
    }
}
