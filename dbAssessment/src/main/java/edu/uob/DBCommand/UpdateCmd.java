package edu.uob.DBCommand;

import edu.uob.BoolOperation;
import edu.uob.ConditionDealer;
import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
import edu.uob.FileDealer;
import edu.uob.Table;

import java.util.ArrayList;
import java.util.List;


public class UpdateCmd extends Command {
    private List<List<String>> nameValueList;
    private List<String> ConditionTokens;

    private static final int indexofATTRIBUTE = 0;
    private static final int indexofVALUE = 2;


    public UpdateCmd(String DBName, String tableName, List<List<String>> nameValueList, List<String> ConditionTokens) {
        super(DBName, tableName);
        this.nameValueList = nameValueList;
        this.ConditionTokens = ConditionTokens;
    }


    @Override
    public String execute()  throws interpException{
        try{
            FileDealer fd = new FileDealer(getDBName(), getTableName());
            Table table = fd.file2Table();
            List<String> qualifiedItems = ConditionDealer.conditionExecute(ConditionTokens, table);
            List<String> others = BoolOperation.AMinusB(table.getAllItems(), qualifiedItems);

            for (List<String> nameValuePair: nameValueList){
                String attribute = nameValuePair.get(indexofATTRIBUTE);
                int attributeIndex = table.getAttributesName().indexOf(attribute);
                String newValue = nameValuePair.get(indexofVALUE);
                for (int i = 0; i < qualifiedItems.size(); i++){
                    String[] toarray = qualifiedItems.get(i).split("\t");
                    toarray[attributeIndex] = newValue;
                    String modified = String.join("\t", toarray);
                    qualifiedItems.set(i, modified);
                }
            }

            List<String> all = BoolOperation.or(others, qualifiedItems);
            table.updateClass(all);
            table.write2File();
            return "[OK], update succeed";
        } catch (Exception e){
            throw new interpException("[ERROR], failed to update");
        }
    }
}
