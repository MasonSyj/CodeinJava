package edu.uob.DBCommand;

import edu.uob.Exceptions.interpException;
import edu.uob.Enums.ItemType;

import java.util.List;

public class InsertCmd extends ComplexCommand {
	private List<String> valueList;

	public InsertCmd(String DBName, String tableName, List<String> valueList) throws interpException {
		super(DBName, tableName);
		this.valueList = valueList;

	}

	@Override
	public String execute() throws interpException {
		try{
			if (table.getNumofAttributes() != valueList.size() + 1 || valueList.size() == 0){
				throw new interpException("[ERROR], A table will n attribute must insert into n values.");
			}

			table.addItem(valueList, ItemType.NEW);
			table.write2File();
			return "[OK]";

		}catch(Exception e){
			throw new interpException("[ERROR], Insert Command Failed.");

		}

	}


}
