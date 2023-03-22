package edu.uob.DBCommand;

import edu.uob.Exceptions.interpException;

public abstract class BasicCommand extends Command{
    public BasicCommand(String DBName, String tableName) {
        super(DBName, tableName);
    }

    @Override
    public abstract String execute() throws interpException;
}
