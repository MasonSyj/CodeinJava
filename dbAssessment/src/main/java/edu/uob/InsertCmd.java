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
			FileDealer fileContaningTable = new FileDealer(getDBName(), getTableName());
			Table table = fileContaningTable.file2Table();

			if (table.getNumofAttributes() != valueList.size() + 1){
				return "[ERROR], A table will n attribute must insert into n values.";
			}

			table.addItem(valueList, ItemType.NEW);
			table.write2File();
			return "[OK]";

		}catch(Exception e){
			return "[ERROR], Insert Command Failed.";
		}

	}


}
