package edu.uob.DBCommand;

import edu.uob.DBCommand.Command;

import java.io.File;

public class UseCmd extends Command {

    public UseCmd(String DBname){
        super(DBname, null);
        execute();
    }

    @Override
    public String execute() {
        File file = new File("databases" + File.separator + getDBName());
        if (file.isDirectory()){
            return getDBName() + " exists";
        }else {
            throw new RuntimeException(getDBName() + " doesn't exist");
        }
    }
}
