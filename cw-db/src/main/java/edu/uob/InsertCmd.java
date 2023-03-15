package edu.uob;

import java.io.File;
import java.util.List;

public class InsertCmd {
	private String DBName;
	private String tableName;
	private List<String> valueList;

	public InsertCmd(String DBName, String tableName, List<String> valueList){
		this.tableName = tableName;
		this.valueList = valueList;
		execute();
	}

	private void execute() {
		//1. find the file containing the table matched by tablName;
		FileDealer fileContaningTable = new FileDealer(DBName, tableName);
		//2. instantiate the table
		Table table = fileContaningTable.file2Table();
		//3. add new value into the table
		table.addValue(valueList);
	}


}
