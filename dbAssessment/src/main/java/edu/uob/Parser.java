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
			case "UPDATE": parseUPDATE(); break;
			case "ALTER": parseALTER(); break;
			case "DELETE": parseDELETE(); break;
			case "DROP": parseDROP(); break;
//			case "JOIN": parseJOIN(); break;
		}
	}

	private void parseUPDATE() {
		if (!tokens.get(2).toLowerCase().equals("set")){

		}

		int ConditionBeginIndex = -1;
		for (int i = 6; i < tokens.size(); i++){
			if (tokens.get(i).toLowerCase().equals("where")){
				ConditionBeginIndex = i + 1;
				break;
			}
		}

		if (ConditionBeginIndex == -1){
			throw new IllegalArgumentException("Update operation needs to have a where");
		}

		if (ConditionBeginIndex == tokens.size()){
			throw new IllegalArgumentException("Update operation must have condition(s)");
		}

		parseConditions(ConditionBeginIndex);
		List<String> conditionTokens = tokens.subList(ConditionBeginIndex, tokens.size());
		UpdateCmd updateCmd = new UpdateCmd(currentDBName, tokens.get(1), parseNameValueList(tokens), conditionTokens);
		execResult = updateCmd.execute();

	}

	private void parseDELETE() {
		if (!tokens.get(1).toLowerCase().equals("from")){
			throw new IllegalArgumentException("DELETE operation must come with a FROM");
		}

		if (!tokens.get(3).toLowerCase().equals("where")){
			throw new IllegalArgumentException("DELETE operation must hava a WHERE");
		}

		parseConditions(4);
		List<String> conditionTokens = tokens.subList(4, tokens.size());
		DeleteCmd deleteCmd = new DeleteCmd(currentDBName, tokens.get(2), conditionTokens);
		execResult = deleteCmd.execute();
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
		List<String> conditionTokens = null;
		for (int i = 0; i < tokens.size(); i++){
			if (tokens.get(i).toLowerCase().equals("where")){
				parseConditions(i + 1);
				conditionTokens = tokens.subList(i + 1, tokens.size());
			}
		}


		if (tokens.get(1).equals("*")){
			if (!tokens.get(2).toLowerCase().equals("from")){
				throw new IllegalArgumentException("Select Query needs to have word: 'from'\n");
			}

			SelectCmd selectCmd = new SelectCmd(currentDBName, tokens.get(3), null, conditionTokens);
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
			String tableName = tokens.get(indexofFrom + 1);
			List<List<String>> attributesList = parseAttributeList(tokens, tableName, 1);
			SelectCmd selectCmd = new SelectCmd(currentDBName, tableName, attributesList, conditionTokens);
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

	public void parseConditions(int index){
		for (int i = index; i < tokens.size() - 1; i++){
			for (String specialChar: ConditionTest.operator){
				if (tokens.get(i).equals(specialChar)){
					String singleCondition = tokens.get(i - 1) + " " + tokens.get(i) + " " + tokens.get(i + 1);
					tokens.remove(i);
					tokens.remove(i);
					tokens.set(i - 1, singleCondition);
				}
			}
		}
		tokens.remove(tokens.size() - 1);
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

	public List<List<String>> parseNameValueList(List<String> tokens){
		List<List<String>> res = new ArrayList<>();

		int i = 3;
		while (!tokens.get(i).toLowerCase().equals("where")){
			List<String> curNameValuePair = new ArrayList<>();
			try{
				for (int cnt = 0; cnt < 3; cnt++){
					if (cnt == 1 && !tokens.get(i).equals("=")){
						throw new IllegalArgumentException("Set Name Value List needs to have a equation including a '='");
					}
					curNameValuePair.add(tokens.get(i++));

				}
				res.add(curNameValuePair);
				// maybe improved here
				if (tokens.get(i).equals(",")){
					i++;
				}
			} catch (Exception e){
				throw new IllegalArgumentException("NameValueList is incomplete");
			}

		}

		return res;
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
