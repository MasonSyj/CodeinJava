package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class InsertCmd extends Command{
//	private String DBName;
//	private String tableName;
	private List<String> valueList;

	public InsertCmd(String DBName, String tableName, List<String> valueList){
		super(DBName, tableName);
		this.valueList = valueList;
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
			table.addItem(valueList);
			//4. update the file
			String newValueLine = "";
			for (String str: valueList){
				newValueLine = newValueLine + str + "\t";
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getDBName() + File.separator + getTableName()), true));

			writer.write(newValueLine);
			writer.newLine();
			writer.flush();

			return "[OK]";
		}catch(Exception e){
			String str = "[ERROR], Insert Command Failed.";
			return str;
		}

	}


}
