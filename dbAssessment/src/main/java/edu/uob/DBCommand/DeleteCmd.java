package edu.uob.DBCommand;

import edu.uob.ConditionDealer;
import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
import edu.uob.FileDealer;
import edu.uob.Table;

import java.util.*;

public class DeleteCmd extends ComplexCommand {

    List<String> conditionTokens;

    public DeleteCmd(String DBName, String tableName, List<String> ConditionTokens) throws interpException {
        super(DBName, tableName);
        this.conditionTokens = ConditionTokens;
    }

    @Override
    public String execute() throws interpException {
        try {
            List<String> deletedItem = ConditionDealer.conditionExecute(conditionTokens, table);
            List<String> all = table.getAllItems();
            for (String deleted: deletedItem){
                all.remove(deleted);
            }
            table.updateClass(all);
            table.write2File();
            return "[OK], items deleted successfully";
        } catch (Exception e) {
            throw new interpException("[ERROR], failed to delete items");

        }
    }

}
