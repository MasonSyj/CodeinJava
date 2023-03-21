package edu.uob.DBCommand;

import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
import edu.uob.SQLKeywords;

import java.io.File;

public class CreateDBCmd extends Command {
    public CreateDBCmd(String DBname){
        super(DBname, null);
    }

    public String execute() throws interpException {
        try{
            if (SQLKeywords.SQLKeyWords.contains(getDBName().toUpperCase())){
                throw new interpException("[ERROR], Database Name cannot be SQL Keywords.");
            }
            File theDir = new File("databases" + File.separator + getDBName());
//            File theDir = new File(getDBName());
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            return "[OK], Database created successfully";
        }catch (Exception e){
            throw new interpException("[ERROR], Database failed to create.");
        }
    }

}

