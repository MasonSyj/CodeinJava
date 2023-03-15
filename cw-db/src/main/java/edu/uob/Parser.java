package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	private List<String> tokens;
//	private CommandType cmdType;

	public Parser(List<String> tokens) {
		this.tokens = tokens;
	}

	public void cmdSearch(){
		String cmd = tokens.get(0);
		switch(cmd){
			case "INSERT": parseINSERT(); break;
//			case "USE": parseUSE(); break;
//			case "CREATE": parseCREATE(); break;
//			case "UPDATE": parseUPDATE(); break;
//			case "ALTER": parseALTER(); break;
//			case "DELETE": parseDELETE(); break;
//			case "DROP": parseDROP(); break;
//			case "JOIN": parseJOIN(); break;
//			case "SELECT": parseSELECT(); break;
		}
	}

	private void parseINSERT() {
		if (!tokens.get(1).toLowerCase().equals("into")){
			throw new IllegalArgumentException("INSERT cmd should come with a INTO")
		}
		String tableName = tokens.get(2);

		if (!tokens.get(3).toLowerCase().equals("values")){
			throw new IllegalArgumentException("INSERT cmd should come with a VALUES");
		}

		if (!tokens.get(4).toLowerCase().equals("values")){
			throw new IllegalArgumentException("INSERT cmd should come with a VALUES");
		}

		if (!tokens.get(5).equals("(")){
			throw new IllegalArgumentException("INSERT cmd should come with a ( after VALUES)");
		}

		List<String> valueList = parseValueList(tokens);

		InsertCmd insertCmd = new InsertCmd(tableName, valueList);
	}

	private List<String> parseValueList(List<String> tokens) {
		List<String> ans = new ArrayList<String>();
		int i = 6;
		try{
			while (!tokens.get(i).equals(")")){
				ans.add(tokens.get(i));
			}
		}catch (Exception e){
			throw new RuntimeException("ValueList didn't end correctly");
		}

		return ans;
	}
}
