package edu.uob;

import java.util.*;

public class Parser {

	private static String currentDBName;
	private List<String> tokens;
//	private CommandType cmdType;
	private String execResult;

	public Parser(List<String> tokens) {
		this.tokens = tokens;
	}

	public String getExecResult() {
		return execResult;
	}

	public void cmdSearch(){
		String cmd = tokens.get(0);
		if (!tokens.get(tokens.size() - 1).equals(";")){
			throw new IllegalArgumentException("Query doesn't end with ';'");
		}

		switch(cmd.toUpperCase()){
			case "USE": parseUSE(); break;
			case "CREATE": parseCREATE(); break;
			case "INSERT": parseINSERT(); break;
			case "SELECT": parseSELECT(); break;
//			case "UPDATE": parseUPDATE(); break;
			case "ALTER": parseALTER(); break;
//			case "DELETE": parseDELETE(); break;
			case "DROP": parseDROP(); break;
//			case "JOIN": parseJOIN(); break;
		}
	}

	private void parseALTER() {
		if (!tokens.get(1).toLowerCase().equals("table")){
			throw new IllegalArgumentException("ALTER operation must come with a TABLE");
		}else{
			AlterationType type;
			if (tokens.get(3).toLowerCase().equals("add")){
				type = AlterationType.ADD;
			}else if (tokens.get(3).toLowerCase().equals("drop")){
				type = AlterationType.DROP;
			}else{
				throw new IllegalArgumentException("Alter operation doesn't do add or drop.");
			}

			AlterCmd alterCmd = new AlterCmd(currentDBName, tokens.get(2), type, tokens.get(4));
			execResult = alterCmd.execute();
		}
	}

	private void parseDROP() {
		if (tokens.get(1).toLowerCase().equals("database")){
			DropDBCmd dropDBCmd = new DropDBCmd(tokens.get(2), null);
			execResult = dropDBCmd.execute();
		}else if (tokens.get(1).toLowerCase().equals("table")){
			DropTableCmd dropTableCmd = new DropTableCmd(currentDBName, tokens.get(2));
			execResult = dropTableCmd.execute();
		}else{
			throw new IllegalArgumentException("Drop Query needs to know it's database or table");
		}
	}

	private void parseSELECT() {
		if (tokens.get(1).equals("*")){
			if (!tokens.get(2).toLowerCase().equals("from")){
				throw new IllegalArgumentException("Select Query needs to have word: 'from'\n");
			}

			SelectCmd selectCmd = new SelectCmd(currentDBName, tokens.get(3), null);
			execResult = selectCmd.execute();
		}else{
			// find the index of "from"
			int indexofFrom = 2;
			for (int i = 0; i < tokens.size(); i++){
				if (tokens.get(i).toLowerCase().equals("from")){
					indexofFrom = i;
					break;
				}
			}
			// the next one is tablename
			String tableName = tokens.get(indexofFrom + 1);
			// from 1 to n is attributslist
			List<List<String>> attributesList = parseAttributeList(tokens, tableName, 1);
			SelectCmd selectCmd = new SelectCmd(currentDBName, tableName, attributesList);
			execResult = selectCmd.execute();

		}
	}

	private void parseUSE() {
		UseCmd useCmd = new UseCmd(tokens.get(1));
		if (useCmd.execute().contains("exists")){
			currentDBName = useCmd.getDBName().toString();
		}
	}

	private void parseJOIN() {

	}

	private void parseCREATE() {
		if (tokens.get(1).toLowerCase().equals("database")){
//			currentDBName = tokens.get(2);
			CreateDBCmd createDBCmd = new CreateDBCmd(tokens.get(2));
		} else if (tokens.get(1).toLowerCase().equals("table")){
			if (tokens.get(3).equals("(")){
				List<List<String>> ref = parseAttributeList(tokens, tokens.get(2), 4);
				CreateTableCmd createTableCmd = new CreateTableCmd(currentDBName, tokens.get(2), ref);
			}else{
				CreateTableCmd createTableCmd = new CreateTableCmd(currentDBName, tokens.get(2), null);
			}

		} else{
			throw new IllegalArgumentException("CREATE cmd should come with DATABASE or TABLE");
		}

	}

	private void parseINSERT() {
		if (!tokens.get(1).toLowerCase().equals("into")){
			throw new IllegalArgumentException("INSERT cmd should come with a INTO");
		}
		String tableName = tokens.get(2);

		if (!tokens.get(3).toLowerCase().equals("values")){
			throw new IllegalArgumentException("INSERT cmd should come with a VALUES");
		}

		if (!tokens.get(4).equals("(")){
			throw new IllegalArgumentException("INSERT cmd should come with a ( after VALUES)");
		}

		List<String> valueList = parseValueList(tokens, 5);

		InsertCmd insertCmd = new InsertCmd(currentDBName, tableName, valueList);
	}

	private List<String> parseValueList(List<String> tokens, int index) {
		List<String> ans = new ArrayList<String>();
		try{
			while (!tokens.get(index).equals(")")){
				if (!tokens.get(index).equals(",")){
					ans.add(tokens.get(index));
				}
				index++;
			}
		}catch (Exception e){
			throw new RuntimeException("ValueList didn't end correctly");
		}

		return ans;
	}

	private List<List<String>> parseAttributeList(List<String> tokens, String DefaultTableName, int index) {
		List<List<String>> ans = new ArrayList<>();
		try{
			while (!tokens.get(index).equals(")") && !tokens.get(index).toLowerCase().equals("from")){
				if (!tokens.get(index).equals(",")){
					if (tokens.get(index).contains(".")){
						ans.add(Arrays.stream(tokens.get(index).split(".")).toList());
					}else{
						List<String> curAttribute = new ArrayList<String>();
						curAttribute.add(DefaultTableName);
						curAttribute.add(tokens.get(index));
						ans.add(curAttribute);
					}
				}
				index++;
			}
		}catch (Exception e){
			throw new RuntimeException("ValueList didn't end correctly");
		}

		return ans;
	}
}
