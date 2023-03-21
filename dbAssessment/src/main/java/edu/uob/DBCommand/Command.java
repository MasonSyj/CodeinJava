package edu.uob.DBCommand;

import edu.uob.Exceptions.interpException;

public abstract class Command {
    private String DBName;
    private String tableName;

    public Command(String DBName, String tableName){
        this.DBName = DBName;
        this.tableName = tableName;
    }

    public String getDBName() {
        return DBName;
    }

    public String getTableName() {
        return tableName;
    }

    public abstract String execute() throws interpException;
}
