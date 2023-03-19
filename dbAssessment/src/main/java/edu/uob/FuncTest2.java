package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FuncTest2 {
	public static void main(String[] args) {
		Table testTable = new Table(".","Student");
//		Column id = new Column<Integer>("id");
//		id.addValue(0);
//		id.addValue(1);
//		id.addValue(2);
//		System.out.println(id.getColumnName());
//		id.getColumnBody().stream().forEach(System.out::println);

//		Column name = new Column<String>("name");
//		name.addValue("Peter");
//		name.addValue("Sam");
//		name.addValue("Mary");
//		System.out.println(name.getColumnName());
//		name.getColumnBody().stream().forEach(System.out::println);

//		testTable.addNewColumn(id);
//		testTable.addNewColumn(name);
//		testTable.updateFile();

		System.out.println();

		Table robotTable = new Table(".", "Robot");
//		robotTable.addNewColumn(new Column<Integer>("id"));
//		robotTable.addNewColumn(new Column<String>("name"));
//		robotTable.addNewColumn(new Column<Integer>("price"));

		List c3po = new ArrayList();
		c3po.add(1);
		c3po.add("c3po");
		c3po.add(5000);

//		robotTable.addValue(c3po);

		robotTable.addNewColumn(new Column("speed"));

//		robotTable.updateFile();
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
}














