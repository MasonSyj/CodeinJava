package edu.uob.DBCommand;

import edu.uob.ConditionDealer;
import edu.uob.DBCommand.Command;
import edu.uob.FileDealer;
import edu.uob.Table;

import java.util.*;

public class DeleteCmd extends Command {

    List<String> conditionTokens;

    public DeleteCmd(String DBName, String tableName, List<String> ConditionTokens) {
        super(DBName, tableName);
        this.conditionTokens = ConditionTokens;
    }

    @Override
    public String execute() {
        try {
            FileDealer fd = new FileDealer(getDBName(), getTableName());
            Table table = fd.file2Table();
            List<String> deletedItem = ConditionDealer.conditionExecute(conditionTokens, table);
            List<String> all = table.getAllItems();
            for (String deleted: deletedItem){
                all.remove(deleted);
            }
            table.updateClass(all);
            table.write2File();
            return "[OK], items deleted successfully";
        } catch (Exception e) {
            return "[ERROR], failed to delete items";
        }
    }

}
