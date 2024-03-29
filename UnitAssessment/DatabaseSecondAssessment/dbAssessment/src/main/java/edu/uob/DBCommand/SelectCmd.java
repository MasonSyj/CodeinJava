package edu.uob.DBCommand;

import edu.uob.ConditionDealer;
import edu.uob.Exceptions.interpException;
import edu.uob.FileDealer;

import java.util.ArrayList;
import java.util.List;

public class SelectCmd extends ComplexCommand {

    List<String[]> attributeList;
    List<String> conditionTokens;

    public SelectCmd(String DBName, String tableName, List<String[]> attributeList, List<String> ConditionTokens) throws interpException {
        super(DBName, tableName);
        this.attributeList = attributeList;
        this.conditionTokens = ConditionTokens;
    }

    public List<String> getAttributeName() throws interpException {
        List<String> attributesNames = new ArrayList<String>();
        for (String[] list: attributeList){
            if (!list[0].equals(getTableName())){
                throw new interpException("In your case it's due to you cannot select attributes from other tables.");
            }
            String attributeName = table.getAttributesName().get(table.indexofAttribute(list[1]));
            attributesNames.add(attributeName);
        }
        return attributesNames;
    }
    @Override
    public String execute() throws interpException {
        try{
            List<String> items;
            if (conditionTokens == null){
                items = table.getAllItems();
            }else{
                items = ConditionDealer.conditionExecute(conditionTokens, table);
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
