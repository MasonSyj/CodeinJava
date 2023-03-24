package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class Column {
	private String columnName;
	private List<String> columnBody;

	public Column(String columnName){
		this.columnName = columnName;
		this.columnBody = new ArrayList<String>();
	}

	public List<String> getColumnBody() {
		return columnBody;
	}

	public String getColumnName() {
		return columnName;
	}

	public void addValue(String value){
		columnBody.add(value);
	}

	public void clean(){
		while (columnBody.size() != 0){
			columnBody.remove(0);
		}
	}
}
