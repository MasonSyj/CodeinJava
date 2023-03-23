package edu.uob;

import edu.uob.DBCommand.*;
import edu.uob.Enums.AlterationType;
import edu.uob.Exceptions.DBException;
import edu.uob.Exceptions.parseException;

import java.io.File;
import java.text.ParseException;
import java.util.*;

public class Parser {

	private static String currentDBName;
	private List<String> tokens;
//	private CommandType cmdType;
	private String execResult;

	public static String getCurrentDBName() {
		return currentDBName;
	}

	public Parser(List<String> tokens) {
		this.tokens = tokens;
	}

	public String getExecResult() {
		return execResult;
	}

	public void cmdSearch() throws DBException {
		if (tokens.contains(Token.wrongComparator)){
			throw new parseException("[ERROR] Query contains wrong comparator.");
		}
		String cmd = tokens.get(0);
		if (!tokens.get(tokens.size() - 1).equals(";")){
			throw new parseException("[ERROR] Query doesn't end with ';'");
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
			case "JOIN": parseJOIN(); break;
			default: throw new parseException("[ERROR], Don't know what operation you want to do;");
		}
	}

	private void parseUPDATE() throws DBException {
		try{
			if (!tokens.get(2).toLowerCase().equals("set")){
				throw new parseException("Update operation needs to have a set");
			}

			int ConditionBeginIndex = -1;
			for (int i = 0; i < tokens.size(); i++){
				if (tokens.get(i).toLowerCase().equals("where")){
					ConditionBeginIndex = i + 1;
					break;
				}
			}

			if (ConditionBeginIndex == -1){
				throw new parseException("Update operation needs to have a where");
			}

			if (ConditionBeginIndex == tokens.size() - 1){
				throw new parseException("Update operation must have condition(s)");
			}

			parseConditions(ConditionBeginIndex);
			List<String> conditionTokens = tokens.subList(ConditionBeginIndex, tokens.size());
			UpdateCmd updateCmd = new UpdateCmd(currentDBName, tokens.get(1), parseNameValueList(tokens), conditionTokens);
			execResult = updateCmd.execute();
		} catch (Exception e){
			throw new parseException("[ERROR] Update cmd is incomplete. " + e.getMessage());
		}


	}

	private void parseDELETE() throws DBException {
		try{
			if (!tokens.get(1).toLowerCase().equals("from")){
				throw new parseException("DELETE operation must come with a FROM");
			}

			if (!tokens.get(3).toLowerCase().equals("where")){
				throw new parseException("DELETE operation must hava a WHERE");
			}

			parseConditions(4);
			List<String> conditionTokens = tokens.subList(4, tokens.size());
			DeleteCmd deleteCmd = new DeleteCmd(currentDBName, tokens.get(2), conditionTokens);
			execResult = deleteCmd.execute();
		} catch (Exception e){
			throw new parseException("[ERROR] Delete cmd is incomplete. " + e.getMessage());
		}

	}

	private void parseALTER() throws DBException {
		try{
			if (!tokens.get(1).toLowerCase().equals("table")){
				throw new parseException("ALTER operation must come with a TABLE");
			}else{
				AlterationType type;
				if (tokens.get(3).toLowerCase().equals("add")){
					type = AlterationType.ADD;
				}else if (tokens.get(3).toLowerCase().equals("drop")){
					type = AlterationType.DROP;
				}else{
					throw new parseException("[ERROR], Alter operation doesn't know to do add or drop.");
				}

				AlterCmd alterCmd = new AlterCmd(currentDBName, tokens.get(2), type, tokens.get(4));
				execResult = alterCmd.execute();
			}
		} catch (Exception e){
			throw new parseException("[ERROR] Alter cmd is incomplete. " + e.getMessage());
		}

	}

	private void parseDROP() throws DBException {
		try {
			if (tokens.get(1).toLowerCase().equals("database")){
				DropDBCmd dropDBCmd = new DropDBCmd(tokens.get(2), null);
				execResult = dropDBCmd.execute();
				File dir = new File("databases" + File.separator + tokens.get(2));
				dir.delete();
			}else if (tokens.get(1).toLowerCase().equals("table")){
				DropTableCmd dropTableCmd = new DropTableCmd(currentDBName, tokens.get(2));
				execResult = dropTableCmd.execute();
			}else{
				throw new parseException("Drop Query needs to know it's database or table");
			}
		} catch (Exception e){
			throw new parseException("[ERROR] Drop cmd is incomplete. " + e.getMessage());
		}
	}

	private void parseSELECT() throws DBException {
		try {
			List<String> conditionTokens = null;
			for (int i = 0; i < tokens.size(); i++){
				if (tokens.get(i).toLowerCase().equals("where")){
					parseConditions(i + 1);
					conditionTokens = tokens.subList(i + 1, tokens.size());
					break;
				}
			}

			int indexofFrom = -1;
			for (int i = 0; i < tokens.size(); i++){
				if (tokens.get(i).toLowerCase().equals("from")){
					indexofFrom = i;
					break;
				}
			}

			if (indexofFrom == -1){
				throw new parseException("Select Query needs to have FROM");
			}

			String tableName = tokens.get(indexofFrom + 1);
			List<String[]> attributesList = parseAttributeList(tokens, tableName, 1);
			SelectCmd selectCmd = new SelectCmd(currentDBName, tableName, attributesList, conditionTokens);
			execResult = selectCmd.execute();
		} catch (Exception e){
			throw new parseException("[ERROR] Select cmd is incomplete. " + e.getMessage());
		}

	}

	private void parseUSE() throws DBException{
		if (!tokens.get(1).matches("[a-zA-Z0-9]+")){
			throw new parseException("[ERROR] DBName invalidated.");
		}
		UseCmd useCmd = new UseCmd(tokens.get(1));
		execResult = useCmd.execute();
		if (execResult.contains("exists")){
			currentDBName = useCmd.getDBName();
		}
	}

	private void parseJOIN() throws DBException {
		try{
			if (!tokens.get(2).toLowerCase().equals("and")){
				throw new parseException("Join operation needs an AND after first tablename");
			}else if (!tokens.get(4).toLowerCase().equals("on")){
				throw new parseException("Join operation needs an ON after second tablename");
			}else if (!tokens.get(6).toLowerCase().equals("and")){
				throw new parseException("Join operation needs an AND after first Attribute name");
			}

			if (!tokens.get(1).matches("[a-zA-Z0-9]+")) {
				throw new parseException("First table name invalidated");
			}
			if (!tokens.get(3).matches("[a-zA-Z0-9]+")) {
				throw new parseException("Second table name invalidated");
			}
			if (!tokens.get(5).matches("[a-zA-Z][a-zA-Z0-9.]*")){
				throw new parseException("First Attribute name invalidated");
			}
			if (!tokens.get(7).matches("[a-zA-Z][a-zA-Z0-9.]*")){
				throw new parseException("Second Attribute name invalidated");
			}

			JoinCmd joinCmd = new JoinCmd(currentDBName, tokens.get(1), tokens.get(3), tokens.get(5), tokens.get(7));
			execResult = joinCmd.execute();
		} catch (DBException e){
			throw new parseException("[ERROR], Join operation is incomplete. " + e.getMessage());
		}
	}

	private void parseCREATE() throws DBException {
		try{
			if (!tokens.get(2).matches("[a-zA-Z0-9]+")){
				throw new parseException("Name format invalidated");
			}
			if (tokens.get(1).toLowerCase().equals("database")){
				if (tokens.get(2).equals(";")){
					throw new parseException("create database without giving name.");
				}
				CreateDBCmd createDBCmd = new CreateDBCmd(tokens.get(2));
				execResult = createDBCmd.execute();
			} else if (tokens.get(1).toLowerCase().equals("table")) {
				if (tokens.get(2).equals(";")){
					throw new parseException("create table without giving name.");
				}
				if (!tokens.get(3).equals("(")){
					CreateTableCmd createTableCmd = new CreateTableCmd(currentDBName, tokens.get(2), null);
					execResult = createTableCmd.execute();
				}else if (tokens.get(3).equals("(")){
					List<String[]> attributeList = parseAttributeList(tokens, tokens.get(2), 4);
					CreateTableCmd createTableCmd = new CreateTableCmd(currentDBName, tokens.get(2), attributeList);
					execResult = createTableCmd.execute();
				}
			} else{
				throw new parseException("CREATE cmd should come with DATABASE or TABLE");
			}
		}catch (Exception e){
			throw new parseException("[ERROR], Create operation is incomplete. " + e.getMessage());
		}


	}

	private void parseINSERT() throws DBException {
		try {
			if (!tokens.get(1).toLowerCase().equals("into")){
				throw new parseException("INSERT cmd should come with a INTO");
			}

			if (!tokens.get(2).matches("[a-zA-Z0-9]+")){
				throw new parseException("Table name format invalidated");
			}

			if (!tokens.get(3).toLowerCase().equals("values")){
				throw new parseException("INSERT cmd should come with a VALUES");
			}

			if (!tokens.get(4).equals("(")){
				throw new parseException("INSERT cmd should come with a ( after VALUES)");
			}

			List<String> valueList = parseValueList(tokens, 5);

			InsertCmd insertCmd = new InsertCmd(currentDBName, tokens.get(2), valueList);
			execResult = insertCmd.execute();
		} catch (Exception e){
			throw new parseException("[ERROR] INSERT operation is incomplete." + e.getMessage());
		}

	}

	public void parseConditions(int index) throws parseException {
		try{

			for (int i = index; i < tokens.size() - 1; i++){
				if (ConditionDealer.operator.contains(tokens.get(i))){
					if (!tokens.get(i - 1).matches("[a-zA-Z][a-zA-Z0-9.]*")){
						throw new parseException("Condition's attributeName invalidated.");
					}
					parseValidValue(tokens.get(i+1));
					String singleCondition = tokens.get(i - 1).toLowerCase() + " " + tokens.get(i) + " ";
					singleCondition = singleCondition + tokens.get(i + 1).replaceAll("'(.*?)'", "$1");
					tokens.remove(i);
					tokens.remove(i);
					tokens.set(i - 1, singleCondition);
				}
			}
			tokens.remove(tokens.size() - 1);
			parseValidConditions(index);
		} catch (Exception e){
			throw new parseException("[ERROR] Condition(s) incomplete " + e.getMessage());
		}

	}

	private void parseValidConditions(int index) throws parseException {
		Deque<String> stack = new ArrayDeque<>();
		for (int i = index; i < tokens.size(); i++){
			if (tokens.get(i).equals("(")){
				stack.add("(");
			}else if (tokens.get(i).equals(")")){
				if (stack.isEmpty() || !stack.pop().equals("(")){
					throw new parseException("Conditions is missing ( or )");
				}
			}
		}
		int opcodeOcccurence = 0;
		int operandOcccurence = 0;
		for (int i = index; i < tokens.size(); i++){
			if (tokens.get(i).toLowerCase().equals("and") || tokens.get(i).toLowerCase().equals("or")){
				opcodeOcccurence++;
			}else if (tokens.get(i).length() > 1){  // not ( or ), so is operand
				operandOcccurence++;
			}
		}
		if (opcodeOcccurence + 1 < operandOcccurence){
			throw new parseException("Conditions is missing opcode");
		}else if (opcodeOcccurence + 1 > operandOcccurence){
			throw new parseException("Conditions is missing operand");
		}
	}

	private List<String> parseValueList(List<String> tokens, int index) throws parseException {
		List<String> ans = new ArrayList<String>();
		try{
			while (!tokens.get(index).equals(")")){
				if (!tokens.get(index).equals(",")){
					parseValidValue(tokens.get(index));
					ans.add(tokens.get(index).replaceAll("'(.*?)'", "$1"));
				}
				index++;
			}
		}catch (Exception e){
			throw new parseException("[ERROR], ValueList didn't end correctly");
		}

		return ans;
	}

	public List<List<String>> parseNameValueList(List<String> tokens) throws parseException {
		List<List<String>> res = new ArrayList<>();
		int i = 3;
		while (i < tokens.size() && !tokens.get(i).toLowerCase().equals("where")){
			List<String> curNameValuePair = new ArrayList<>();
			try{
				for (int cnt = 0; cnt < 3; cnt++){

					if (cnt == 1 && !tokens.get(i++).equals("=")){
						throw new parseException("In your case, Set Name Value List needs to have a equation including a '='");
					} else if (cnt == 0){
						curNameValuePair.add(tokens.get(i++).toLowerCase());
						continue;
					} else if (cnt == 2){
						curNameValuePair.add(tokens.get(i++).replaceAll("'(.*?)'", "$1"));
					}

				}
				res.add(curNameValuePair);

				if (tokens.get(i).equals(",")){
					i++;
				}
			} catch (Exception e){
				throw new parseException("NameValueList is incomplete");
			}

		}

		return res;
	}

	private List<String[]> parseAttributeList(List<String> tokens, String defaultTableName, int index) throws parseException {
		if (index == 1 && tokens.get(index).equals("*")){
			return null;
		}

		List<String[]> ans = new ArrayList<>();
		try{
			while (!tokens.get(index).equals(")") && !tokens.get(index).toLowerCase().equals("from")){
				if (tokens.get(index).equals(",")){
					index++;
					continue;
				}

				String[] curAttributeName = new String[2];
				if (tokens.get(index).contains(".")){
					curAttributeName[0] = tokens.get(index).split("\\.")[0];
					curAttributeName[1] = tokens.get(index).split("\\.")[1];
				}else{
					curAttributeName[0] = defaultTableName;
					curAttributeName[1] = tokens.get(index);
				}

				if (!curAttributeName[0].matches("[a-zA-Z0-9]+")){
					throw new parseException("[ERROR] TableName's format invalidated.");
				}

				if (!curAttributeName[1].matches("[a-zA-Z0-9]+")){
					throw new parseException("[ERROR] AttributeName's format invalidated.");
				}

				ans.add(curAttributeName);

				index++;
			}
		}catch (Exception e){
			throw new parseException("[ERROR] AttributeList didn't build correctly.");
		}

		return ans;
	}

	public void parseValidValue(String token) throws parseException {
		if(token.matches("^[0-9.]+$")){

		}else if (token.toLowerCase().equals("true") || token.toLowerCase().equals("false") || token.toLowerCase().equals("null")){

		}else if (!(token.matches(".*[a-zA-Z\\s\\p{P}\\d].*") && token.startsWith("'") && token.endsWith("'"))){
			throw new parseException("value invalidated.");
		}
	}
}
