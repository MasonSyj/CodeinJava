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

	public Table file2Table(){
		File file = new File(DBName + File.separator + TableName);

		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		String line = null;
		String attributeLine = null;
		Table ans;

		List<String> attributesList = new ArrayList<String>();

		try {
			attributeLine = reader.readLine();
			attributesList = csvLineParse(attributeLine);

			line = reader.readLine();

			ans = new Table(DBName, TableName);
			ans.addColumns(attributesList);

			while (line != null && line.length() > 0){
				List valueList = new ArrayList();
				valueList = csvLineParse(line);
				ans.addValue(valueList);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ans.updateFile();
		return ans;
	}

	public List<String> csvLineParse(String line) {
		List<String> ans = new ArrayList<String>();
		ans = Arrays.stream(line.split("\t")).toList();
		return ans;
	}



}
