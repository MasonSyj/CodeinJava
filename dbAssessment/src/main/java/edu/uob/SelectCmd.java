package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class SelectCmd extends Command{

    List<String[]> attributeList;
    List<String> ConditionTokens;

    public SelectCmd(String DBName, String tableName, List<String[]> attributeList, List<String> ConditionTokens) {
        super(DBName, tableName);
        this.attributeList = attributeList;
        this.ConditionTokens = ConditionTokens;
    }

    @Override
    public String execute() {
        try{
            // this function must chnage heavily!!!
            if (attributeList == null){
                FileDealer fd = new FileDealer(getDBName(), getTableName());
                Table table = fd.file2Table();
//                List<String> suffixCondition = ConditionTest.convertToSuffix(ConditionTokens);
                if (ConditionTokens == null){
                    List<String> all = table.getAllItems();
                    String allItemsinString = all.toString();
                    //when output, it should include attributesName

                    return "[OK] " + table.getAttributesName() + allItemsinString;
                }else{
                    return "[OK] " + table.getAttributesName() + ConditionTest.conditionExecute(ConditionTokens, table);
                }

            }else{
                // need to sort heavily here
                List<String> attributesNames = new ArrayList<String>();
                for (String[] list: attributeList){
                    String attributeName = list[1];
                    attributesNames.add(attributeName);
                }
                if (ConditionTokens == null){

                    FileDealer fd = new FileDealer(getDBName(), getTableName());
                    Table table = fd.file2Table();
                    List<String> all = table.getParialColumn(attributesNames);
                    all.remove(0);
                    String allItemsinString = all.toString();
                    return "[OK] " + attributesNames + allItemsinString;
                }else{
                    FileDealer fd = new FileDealer(getDBName(), getTableName());
                    Table table = fd.file2Table();
                    List<String> values = ConditionTest.conditionExecute(ConditionTokens, table);

                    Table clone = (Table) table.clone();

                    clone.updateClass(values);
                    List<String> part = clone.getParialColumn(attributesNames);
                    part.remove(0);
                    return "[OK] " + attributesNames + part;
                }

            }

        } catch (Exception e){
            return "[ERROR], failed to get items.\n";
        }

    }
}
