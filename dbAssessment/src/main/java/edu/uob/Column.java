package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Column implements Predicate<String> {
	private String columnName;
	private List<String> columnBody;

	public Column(String columnName){
		this.columnName = columnName;
		this.columnBody = new ArrayList<String>();
	}

	public List getColumnBody() {
		return columnBody;
	}

	public String getColumnName() {
		return columnName;
	}

	public void addValue(String value){
		columnBody.add(value);
	}

	public void set(int index, String t){
		columnBody.set(index, t);
	}

	public void clean(){
		while (columnBody.size() != 0){
			columnBody.remove(0);
		}
	}

	@Override
	public boolean test(String t) {
		String attribute = t.split(" ")[0];
		String operator = t.split(" ")[1];
		String value = t.split(" ")[2];

		if (operator.equals("==")){
			return Integer.parseInt(attribute) == Integer.valueOf(value);
		}else if (operator.equals(">")){
			return Integer.parseInt(attribute) > Integer.valueOf(value);
		}else if (operator.equals("<")){
			return Integer.parseInt(attribute) < Integer.valueOf(value);
		}else if (operator.equals(">=")){
			return Integer.parseInt(attribute) >= Integer.valueOf(value);
		}else if (operator.equals("<=")){
			return Integer.parseInt(attribute) <= Integer.valueOf(value);
		}else if (operator.equals("!=")){
			return Integer.parseInt(attribute) != Integer.valueOf(value);
		}else if (operator.equals("LIKE")){
			return attribute.contains(value);
		}else{
			return false;
		}
	}
}
