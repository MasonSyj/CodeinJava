package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Table {
	private String DBName;
	private String tableName;
	private List<String> attributesName;
	private List<Column> columns;

	private static HashMap<String, Integer> ref;

	private int numofAttributes;
	private int numofItems;

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

		String firstRow = "";
		for (String name: getAttributesName()){
			firstRow = firstRow + name + "\t";
		}
		ans.add(firstRow);

		int row = columns.get(0).getColumnBody().size();
		int col = columns.size();

		for (int i = 0; i < row; i++){
			String item = "";
			for (int j = 0; j < col; j++){
				item = item + columns.get(j).getColumnBody().get(i) + "\t";
				System.out.print(columns.get(j).getColumnBody().get(i) + "\t");
			}
			ans.add(item);
			System.out.println();
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
				System.out.print(columns.get(j).getColumnBody().get(i) + "\t");
			}
			ans.add(item);
			System.out.println();
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
	}

	public boolean dropExistedColumn(Column col){
		if (!getAttributesName().contains(col.toString())){
			return false;
		}else{
			String colName = col.toString();
			Column col =
		}
	}

	public void addColumns(List<String> attributes){
		for (String attribute: attributes){
			addNewColumn(new Column<>(attribute));
		}
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
}
