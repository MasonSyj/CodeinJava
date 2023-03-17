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

public class Table implements Predicate<String>, Cloneable{
	private String DBName;
	private String tableName;
	private List<String> attributesName;
	private List<Column> columns;

	private String value;
	private String operator;
	private String attribute;
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

//		String firstRow = "";
//		for (String name: getAttributesName()){
//			firstRow = firstRow + name + "\t";
//		}
//		ans.add(firstRow);

		int row = columns.get(0).getColumnBody().size();
		int col = columns.size();

		for (int i = 0; i < row; i++){
			String item = "";
			for (int j = 0; j < col; j++){
				item = item + columns.get(j).getColumnBody().get(i) + "\t";
			}
			ans.add(item);
		}
		return ans;
	}

	public List<String> getParialColumn(List<String> attributesName){
		List<String> ans = new ArrayList<>();

		String firstRow = "";
		for (String name: attributesName){
			firstRow = firstRow + name + "\t";
		}
		ans.add(firstRow);

		int row = columns.get(0).getColumnBody().size();
		int col = columns.size();

		for (int i = 0; i < row; i++){
			String item = "";
			for (int j = 0; j < col; j++){
				if (!attributesName.contains(getAttributesName().get(j))){
					continue;
				}

				item = item + columns.get(j).getColumnBody().get(i) + "\t";

			}
			ans.add(item);

		}


		return ans;
	}

	public void addNewColumn(Column col){
		attributesName.add(col.getColumnName());
		columns.add(col);
		numofAttributes++;

		for (int i = 0; i < numofItems; i++){
			columns.get(columns.size() - 1).addValue(null);
		}
		updateFile();
	}

	public boolean dropExistedColumn(String attributeName){
		if (!getAttributesName().toString().contains(attributeName)){
			return false;
		}else{
			int index = attributesName.indexOf(attributeName);
			attributesName.remove(index);
			columns.remove(index);
			updateFile();
			return true;
		}

	}

	public void addColumns(List<String> attributes){
		for (String attribute: attributes){
			addNewColumn(new Column<>(attribute));
		}
	}

	public List<String> csvLineParse(String line) {
		List<String> ans = new ArrayList<String>();
		ans = Arrays.stream(line.split("\t")).toList();
		return ans;
	}

	public void updateFile(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getDBName() + File.separator + getTableName()), false));
			writer.write("");
			writer = new BufferedWriter(new FileWriter(new File(getDBName() + File.separator + getTableName()), true));
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

	public void addValue(List value) {
		numofItems++;

		if (value.size() != this.numofAttributes){
			return;
		}

		for (int j = 0; j < numofAttributes; j++){
			columns.get(j).addValue(value.get(j));
		}
	}

	public void write2File(File file){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			String line = "";
			for (String attribute: attributesName){
				line = line + attribute + "\t";
			}
			writer.write(line);

			int row = columns.get(0).getColumnBody().size();
			int col = columns.size();

			for (int i = 0; i < row; i++){
				line = "";
				for (int j = 0; j < col; j++){
					line = line + columns.get(j).getColumnBody().get(i) + "\t";
				}
				writer.write(line);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedWriter getAccordingFile() throws IOException {
		return new BufferedWriter(new FileWriter(new File(getDBName() + File.separator + getTableName()), true));
	}

	public void cleanAll(){
		for (int j = 0; j < numofAttributes; j++){
			columns.get(j).clean();
		}
		numofItems = 0;
	}

	public List<String> or(List<String> a, List<String> b){
		List<String> res = new ArrayList<>();
		for (String str: a){
			res.add(str);
		}
		for (String str: b){
			res.add(str);
		}

		return res.stream().distinct().collect(Collectors.toList());
	}

	public List<String> and(List<String> a, List<String> b){
		List<String> res = new ArrayList<>();
		for (String str: a){
			if (b.contains(str)){
				res.add(str);
			}
		}

		return res;
	}

	public void updateClass(List<String> ref){
		cleanAll();
		int j = 0;
		for (String entry: ref){
			List<String> splited = Arrays.stream(entry.split("\t")).toList();
			for (int i = 0; i < numofAttributes; i++){
				columns.get(i).addValue(splited.get(i));
//				columns.get(i).set(j, splited.get(i));
			}
		}
//		updateFile();

		numofItems = ref.size();
	}



	public List<String> predicate(String t){
		attribute = t.split(" ")[0];
		operator = t.split(" ")[1];
		value = t.split(" ")[2];

		attributeIndex = attributesName.indexOf(attribute);

		return getAllItems().stream().filter(this).collect(Collectors.toList());
	}

	@Override
	public boolean test(String t) {
		if (operator.equals("==")){
			return Integer.parseInt(t.split("\t")[attributeIndex]) == Integer.valueOf(value);
		}else if (operator.equals(">")){
			return Integer.parseInt(t.split("\t")[attributeIndex]) > Integer.valueOf(value);
		}else if (operator.equals("<")){
			return Integer.parseInt(t.split("\t")[attributeIndex]) < Integer.valueOf(value);
		}else if (operator.equals(">=")){
			return Integer.parseInt(t.split("\t")[attributeIndex]) >= Integer.valueOf(value);
		}else if (operator.equals("<=")){
			return Integer.parseInt(t.split("\t")[attributeIndex]) <= Integer.valueOf(value);
		}else if (operator.equals("!=")){
			return Integer.parseInt(t.split("\t")[attributeIndex]) != Integer.valueOf(value);
		}else if (operator.toUpperCase().equals("LIKE")){
			return t.split("\t")[attributeIndex].equals(value);
		}else{
			return false;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
