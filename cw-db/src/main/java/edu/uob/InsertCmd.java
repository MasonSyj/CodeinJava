package edu.uob;

import java.util.List;

public class InsertCmd {
	private String tableName;
	private List<String> valueList;

	public InsertCmd(String tableName, List<String> valueList){
		this.tableName = tableName;
		this.valueList = valueList;
		execute();
	}

	private void execute() {
		//1. find the file containing the table matched by tablName;

		//2. instan
	}


}
