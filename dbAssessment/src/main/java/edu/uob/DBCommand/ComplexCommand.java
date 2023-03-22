package edu.uob.DBCommand;

import edu.uob.Exceptions.interpException;
import edu.uob.FileDealer;
import edu.uob.Table;

public abstract class ComplexCommand extends Command{

    FileDealer fileDealer;
    Table table;

    public ComplexCommand(String DBName, String tableName) throws interpException {
        super(DBName, tableName);
        fileDealer = new FileDealer(DBName, tableName);
        table = fileDealer.file2Table();
    }

    @Override
    public abstract String execute() throws interpException;
}
