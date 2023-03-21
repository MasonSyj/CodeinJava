package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileDealer {
	public String DBName;
	public String TableName;

	public FileDealer(String DBName, String TableName){
		this.DBName = DBName;
		this.TableName = TableName;
	}

	// locate the file representing the table, then use the data to instanitise a table object
	public Table file2Table(){
		File file = new File("databases" + File.separator + DBName + File.separator + TableName);

		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		String line = "";
		Table ans;
		List<String> attributesList = new ArrayList<String>();

		try {
			line = reader.readLine();
			attributesList = csvLineParse(line);

			line = reader.readLine();

			ans = new Table(DBName, TableName);
			for (String attribute: attributesList){
				ans.addNewColumn(attribute);
			}

			while (line != null && line.length() > 0){
				List valueList = new ArrayList();
				valueList = csvLineParse(line);
				ans.addItem(valueList, ItemType.OLD);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// wrong direction, why write things back to a file?
//		ans.write2File();
		return ans;
	}

	// change tab-separated data per line into List<String>
	public static List<String> csvLineParse(String line) {
		List<String> ans = new ArrayList<String>();
		ans = Arrays.stream(line.split("\t")).toList();
		return ans;
	}

	// change List<String> back into tab-separated txt
	public static String transform2csvLine(List<String> values){
		String ans = "";
		for (String str: values){
			ans = ans + str + "\t";
		}
		return ans.substring(0, ans.length() - 1);
	}
}
