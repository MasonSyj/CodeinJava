package edu.uob.DBCommand;

import edu.uob.Exceptions.interpException;

import java.io.File;

public class UseCmd extends BasicCommand {

    public UseCmd(String DBname){
        super(DBname, null);
    }

    @Override
    public String execute() throws interpException {
        File file = new File("databases" + File.separator + getDBName());
        if (file.isDirectory()){
            return "[OK]" + getDBName() + " exists and using;";
        }else {
            throw new interpException("[ERROR]" + getDBName() + " doesn't exist so can't use");
        }
    }
}
