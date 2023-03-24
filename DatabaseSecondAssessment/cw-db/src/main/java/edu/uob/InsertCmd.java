package edu.uob;

import java.util.List;

public class InsertCmd extends Command{
//	private String DBName;
//	private String tableName;
	private List<String> valueList;

	public InsertCmd(String DBName, String tableName, List<String> valueList){
		super(DBName, tableName);
		execute();
	}

	@Override
	public String execute() {
		try{
			//1. find the file containing the table matched by tablName;
			FileDealer fileContaningTable = new FileDealer(getDBName(), getTableName());
			//2. instantiate the table
			Table table = fileContaningTable.file2Table();
			//3. add new value into the table
			table.addValue(valueList);
			return "OK";
		}catch(Exception e){
			String str = "Insert Command Failed.";
			return str;
		}

	}


}
