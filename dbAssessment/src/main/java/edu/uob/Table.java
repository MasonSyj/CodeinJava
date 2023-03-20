package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Table implements Predicate<String>, Cloneable, Writeable{
	private String DBName;
	private String tableName;

	//numof AttributesName is equal to numof columns
	private List<String> attributesName;
	private List<Column> columns;

	Condition condition;
	private int attributeIndex;

	private int numofAttributes;
	private int numofItems;

	private static HashMap<String, Integer> ref;

	public Table(String DBName, String tableName){
		this.DBName = DBName;
		this.tableName = tableName;
		attributesName = new ArrayList<String>();
		columns = new ArrayList<Column>();
		numofAttributes = 0;
		numofItems = 0;
	}

	public String getDBName() {
		return DBName;
	}

	public String getTableName() {
		return tableName;
	}

	public List<String> getAttributesName(){
		return attributesName;
	}

	public int getNumofItems() {
		return numofItems;
	}

	public int getNumofAttributes() {
		return numofAttributes;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public Column getColumnbyNames(String name) {
		for (Column col: columns){
			if (col.getColumnName().equals(name)){
				return col;
			}
		}
		return null;
	}

	public List<String> getAllItems(){
		List<String> ans = new ArrayList<>();

		int row = getNumofItems();
		int col = columns.size();

		for (int j = 0; j < row; j++){
			List<String> currentItem = new ArrayList<>();
			for (int i = 0; i < col; i++){
				currentItem.add((String) columns.get(i).getColumnBody().get(j));
			}
			ans.add(FileDealer.transform2csvLine(currentItem));
		}

		return ans;
	}

	//get all items but partially by give attributeNames
	public List<String> getParialColumn(List<String> attributesNameList){
		List<String> ans = new ArrayList<>();

		ans.add(FileDealer.transform2csvLine(attributesNameList));

		int row = getNumofItems();

		int[] index = new int[attributesNameList.size()];
		int cnt = 0;
		for (String attribute: attributesNameList){
			index[cnt++] = getAttributesName().indexOf(attribute);
		}

		for (int j = 0; j < row; j++){
			List<String> currentItem = new ArrayList<>();
			for (int i = 0; i < index.length; i++){
				currentItem.add((String) columns.get(index[i]).getColumnBody().get(j));
			}
			ans.add(FileDealer.transform2csvLine(currentItem));
		}


		return ans;
	}

	public void addNewColumn(String columnName){
		Column col = new Column(columnName);
		attributesName.add(col.getColumnName());
		columns.add(col);
		numofAttributes++;

		for (int i = 0; i < numofItems; i++){
			columns.get(columns.size() - 1).addValue(null);
		}
	}

	public boolean dropColumn(String attributeName){
		if (!getAttributesName().toString().contains(attributeName)){
			return false;
		}else{
			int index = attributesName.indexOf(attributeName);
			attributesName.remove(index);
			columns.remove(index);
			write2File();
			return true;
		}
	}

	@Override
	public void write2File(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("databases" + File.separator + getDBName() + File.separator + getTableName()), false));
//			writer.write("");
			int row = columns.get(0).getColumnBody().size();
			int col = columns.size();
			String AttributesLine = "";
			for (String attribute: attributesName){

				AttributesLine = AttributesLine + attribute + "\t";
			}
			writer.write(AttributesLine);
			writer.newLine();

			for (int i = 0; i < row; i++){
				String entry = "";
				for (int j = 0; j < col; j++){
					entry = entry + columns.get(j).getColumnBody().get(i) + "\t";
				}
				writer.write(entry);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addItem(List value) {
		numofItems++;

		if (value.size() != this.numofAttributes){
			return;
		}

		for (int j = 0; j < numofAttributes; j++){
			columns.get(j).addValue(String.valueOf(value.get(j)));
		}
	}

	public void cleanAll(){
		for (int j = 0; j < numofAttributes; j++){
			columns.get(j).clean();
		}
		numofItems = 0;
	}

	public void updateClass(List<String> ref){
		cleanAll();
		int j = 0;
		for (String entry: ref){
			List<String> splited = Arrays.stream(entry.split("\t")).toList();
			for (int i = 0; i < numofAttributes; i++){
				columns.get(i).addValue(splited.get(i));
			}
		}

		numofItems = ref.size();
	}

	public List<String> predicate(String t){

		condition = new Condition(t);

		return getAllItems().stream().filter(this).collect(Collectors.toList());
	}

	@Override
	public boolean test(String t) {
		String operator = condition.getOperator();
		String value = condition.getValue();
		int attributeIndex = attributesName.indexOf(condition.getAttribute());

		if (operator.equals("==")){
			return Double.valueOf(t.split("\t")[attributeIndex]) == Double.valueOf(value);
		}else if (operator.equals(">")){
			return Double.valueOf(t.split("\t")[attributeIndex]) > Double.valueOf(value);
		}else if (operator.equals("<")){
			return Double.valueOf(t.split("\t")[attributeIndex]) < Double.valueOf(value);
		}else if (operator.equals(">=")){
			return Double.valueOf(t.split("\t")[attributeIndex]) >= Double.valueOf(value);
		}else if (operator.equals("<=")){
			return Double.valueOf(t.split("\t")[attributeIndex]) <= Double.valueOf(value);
		}else if (operator.equals("!=")){
			return Double.valueOf(t.split("\t")[attributeIndex]) != Double.valueOf(value);
		}else if (operator.toUpperCase().equals("LIKE")){
			return t.split("\t")[attributeIndex].contains(value);
		}else{
			return false;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
