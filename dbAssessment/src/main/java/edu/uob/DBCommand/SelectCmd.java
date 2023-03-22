package edu.uob.DBCommand;

import edu.uob.ConditionDealer;
import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
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

    public List<String> getAttributeName() throws interpException {
        List<String> attributesNames = new ArrayList<String>();
        for (String[] list: attributeList){
            if (!list[0].equals(getTableName())){
                throw new interpException("In your case it's due to you cannot select attributes from other tables.");
            }
            String attributeName = list[1];
            attributesNames.add(attributeName);
        }
        return attributesNames;
    }
    @Override
    public String execute() throws interpException {
        try{
            FileDealer fd = new FileDealer(getDBName(), getTableName());
            Table table = fd.file2Table();

            List<String> items;
            if (ConditionTokens == null){
                items = table.getAllItems();
            }else{
                items = ConditionDealer.conditionExecute(ConditionTokens, table);
            }
            table.updateClass(items);

            List<String> attributesNames;
            if (attributeList == null){
                attributesNames = table.getAttributesName();
            }else{
                attributesNames = getAttributeName();
            }

            String res = "[OK]\n" + FileDealer.transform2csvLine(attributesNames) + "\n";
            for (String item: table.getParialColumn(attributesNames)){
                res = res + item + "\n";
            }
            return res;
        } catch (Exception e){
            throw new interpException("[ERROR], failed to get items. " + e.getMessage());
        }
    }

}
