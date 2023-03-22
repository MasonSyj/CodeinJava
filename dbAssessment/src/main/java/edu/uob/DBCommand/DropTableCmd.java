package edu.uob.DBCommand;

import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;

import java.io.File;

public class DropTableCmd extends BasicCommand {
	public DropTableCmd(String DBName, String tableName) {
		super(DBName, tableName);
	}

	@Override
	public String execute() throws interpException {
		File file = new File( "databases" + File.separator + getDBName() + File.separator + getTableName());

		if (file.delete()) {
			return "[OK]， Table " + getTableName() + " droped successfully";
		} else {
			throw new interpException("[ERROR]， Table " + getTableName() + "failed to drop");
		}
	}
}
