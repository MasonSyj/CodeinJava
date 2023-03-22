package edu.uob.DBCommand;

import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
import edu.uob.SQLKeywords;

import java.io.File;

public class CreateDBCmd extends BasicCommand {
    public CreateDBCmd(String DBname){
        super(DBname, null);
    }

    public String execute() throws interpException {

        if (SQLKeywords.SQLKeyWords.contains(getDBName().toUpperCase())){
            throw new interpException("[ERROR], Database Name cannot be SQL Keywords.");
        }
        File DBDir = new File("databases" + File.separator + getDBName());

        if (DBDir.exists()){
            throw new interpException("[ERROR], Database already exist");
        }

        try{
            DBDir.mkdir();
            return "[OK], Database created successfully";
        }catch (Exception e){
            throw new interpException("[ERROR], Database failed to have its physical place in hard drive.");
        }


    }

}

