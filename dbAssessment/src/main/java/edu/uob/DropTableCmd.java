package edu.uob;

import java.io.File;

public class DropTableCmd extends Command{
	public DropTableCmd(String DBName, String tableName) {
		super(DBName, tableName);
	}

	@Override
	public String execute() {
		File file = new File( "databases" + File.separator + getDBName() + File.separator + getTableName());

		if (file.delete()) {
			return "[OK]， Table " + getTableName() + " droped successfully";
		} else {
			return "[ERROR]， Table " + getTableName() + "failed to drop";
		}
	}
}
