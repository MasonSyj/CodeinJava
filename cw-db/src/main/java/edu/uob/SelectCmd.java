package edu.uob;

public class SelectCmd extends Command{

    public SelectCmd(String DBName, String tableName) {
        super(DBName, tableName);
    }

    @Override
    public String execute() {
        return null;
    }
}
