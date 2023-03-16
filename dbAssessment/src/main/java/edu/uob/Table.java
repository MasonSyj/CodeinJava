package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
	private String DBName;
	private String tableName;
	private List<String> attributesName;
	private List<Column> columns;

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

	public void addNewColumn(Column col){
		attributesName.add(col.getColumnName());
		columns.add(col);
		numofAttributes++;
	}

	public void addColumns(List<String> attributes){
		for (String attribute: attributes){
			addNewColumn(new Column<>(attribute));
		}
	}

	public void printTable(){
		try {
			BufferedWriter writer = getAccordingFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		int row = columns.get(0).getColumnBody().size();
		int col = columns.size();
		String AttributesLine = "";
		for (String attribute: attributesName){
			System.out.print(attribute + "\t");
			AttributesLine = AttributesLine + "\t";
		}
		System.out.println();

		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
				System.out.print(columns.get(j).getColumnBody().get(i) + "\t");
			}
			System.out.println();
		}
	}

	// this function is here and not finished.
//	public void printTablePartially(){
//		int row = columns.get(0).getColumnBody().size();
//		int col = columns.size();
//		for (String attribute: attributesName){
//			System.out.print(attribute + "\t");
//		}
//		System.out.println();
//
//		for (int i = 0; i < row; i++){
//			for (int j = 0; j < col; j++){
//				System.out.print(columns.get(j).getColumnBody().get(i) + "\t");
//			}
//			System.out.println();
//		}
//	}

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
