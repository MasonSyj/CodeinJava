package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class UpdateCmd extends Command{
    List<List<String>> nameValueList;
    List<String> ConditionTokens;
    public UpdateCmd(String DBName, String tableName, List<List<String>> nameValueList, List<String> ConditionTokens) {
        super(DBName, tableName);
        this.nameValueList = nameValueList;
        this.ConditionTokens = ConditionTokens;
    }

    @Override
    public String execute() {
        try{
            FileDealer fd = new FileDealer(getDBName(), getTableName());
            Table table = fd.file2Table();

            List<String> others = table.getAllItems();

            List<String> qualifiedItems = ConditionTest.conditionExecute(ConditionTokens, table);

            List<String> revisied = new ArrayList<String>();



            for (int i = 0; i < others.size(); i++){
                if (qualifiedItems.contains(others.get(i))){
                    others.remove(i);
                    i--;
                }
            }

            for (List<String> nameValuePair: nameValueList){
                String attribute = nameValuePair.get(0);
                int attributeIndex = table.getAttributesName().indexOf(attribute);
                String newValue = nameValuePair.get(2);
                for (String item: qualifiedItems){
                    String[] toarray = item.split("\t");
                    for (int i = 0; i < toarray.length; i++){
                        if (i == attributeIndex){ //replace 2 by attributeindex
                            toarray[i] = newValue; //replace 8000 by the value
                            break;
                        }
                    }
                    String modified = String.join("\t", toarray);
                    revisied.add(modified);
                }
            }

            List<String> all = table.or(others, revisied);
            table.cleanAll();
            table.updateClass(all);
            table.updateFile();
            return "[OK], update succeed";
        } catch (Exception e){
            return "[ERROR], failed to update";
        }
    }
}
