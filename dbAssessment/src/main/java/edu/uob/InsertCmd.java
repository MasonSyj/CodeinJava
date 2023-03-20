package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class InsertCmd extends Command{
	private List<String> valueList;

	public InsertCmd(String DBName, String tableName, List<String> valueList){
		super(DBName, tableName);
		this.valueList = valueList;

	}

	@Override
	public String execute() {
		try{
			//1. find the file containing the table matched by tablName;
			FileDealer fileContaningTable = new FileDealer(getDBName(), getTableName());
			//2. instantiate the table
			Table table = fileContaningTable.file2Table();
			if (table.getNumofAttributes() != valueList.size()){
				throw new IllegalArgumentException("A table will n attribute must insert into n values.");
			}
			//3. add new value into the table
			table.addItem(valueList);
			//4. update the file
			table.write2File();

//			String newValueLine = "";
//			for (String str: valueList){
//				newValueLine = newValueLine + str + "\t";
//			}
//			table
//
//			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getDBName() + File.separator + getTableName()), true));
//
//			writer.write(newValueLine);
//			writer.newLine();
//			writer.flush();

			return "[OK]";
		}catch(Exception e){
			String str = "[ERROR], Insert Command Failed.";
			return str;
		}

	}


}
