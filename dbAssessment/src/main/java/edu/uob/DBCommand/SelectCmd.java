package edu.uob.DBCommand;

import edu.uob.ConditionDealer;
import edu.uob.DBCommand.Command;
import edu.uob.FileDealer;
import edu.uob.Table;

import java.util.ArrayList;
import java.util.List;

public class SelectCmd extends Command {

    List<String[]> attributeList;
    List<String> ConditionTokens;

    public SelectCmd(String DBName, String tableName, List<String[]> attributeList, List<String> ConditionTokens) {
        super(DBName, tableName);
        this.attributeList = attributeList;
        this.ConditionTokens = ConditionTokens;
    }

    public List<String> getAttributeName(){
        List<String> attributesNames = new ArrayList<String>();
        for (String[] list: attributeList){
            String attributeName = list[1];
            attributesNames.add(attributeName);
        }
        return attributesNames;
    }


    @Override
    public String execute() {
        try{
            // maube just split into four methods
            FileDealer fd = new FileDealer(getDBName(), getTableName());
            Table table = fd.file2Table();
            if (attributeList == null){
                String res = "[OK]\n" + FileDealer.transform2csvLine(table.getAttributesName()) + "\n";
                if (ConditionTokens == null){
                    for (String item: table.getAllItems()){
                        res = res + item + "\n";
                    }
                    return res;
                }else{
                    List<String> itemsAfterCondition = ConditionDealer.conditionExecute(ConditionTokens, table);
                    for (String item: itemsAfterCondition){
                        res = res + item + "\n";
                    }
                    return res;
                }
            }else{
                List<String> attributesNames = getAttributeName();
                String res = "[OK]\n";
                if (ConditionTokens == null){
                    List<String> all = table.getParialColumn(attributesNames);
                    for (String item: all){
                        res = res + item + "\n";
                    }
                    return res;
                }else{
                    List<String> itemsAfterCondition = ConditionDealer.conditionExecute(ConditionTokens, table);
                    Table clone = (Table) table.clone();
                    clone.updateClass(itemsAfterCondition);
                    List<String> part = clone.getParialColumn(attributesNames);
                    for (String item: part){
                        res = res + item + "\n";
                    }
                    return res;
                }
            }
        } catch (Exception e){
            return "[ERROR], failed to get items.\n";
        }

    }
}
