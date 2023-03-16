package edu.uob;

import java.util.ArrayList;
import java.util.List;

public class SelectCmd extends Command{

    List<List<String>> attributeList;

    public SelectCmd(String DBName, String tableName, List<List<String>> attributeList) {
        super(DBName, tableName);
        this.attributeList = attributeList;
    }

    @Override
    public String execute() {
        try{
            if (attributeList == null){
                FileDealer fd = new FileDealer(getDBName(), getTableName());
                Table table = fd.file2Table();
                List<String> all = table.getAllItems();
                String allItemsinString = all.toString();
                return "[OK] " + allItemsinString;
            }else{
                List<String> attributesNames = new ArrayList<String>();
                for (List<String> list: attributeList){
                    String name = list.get(1);
                    attributesNames.add(name);
                }

                FileDealer fd = new FileDealer(getDBName(), getTableName());
                Table table = fd.file2Table();
                List<String> all = table.getParialColumn(attributesNames);
                String allItemsinString = all.toString();
                return "[OK] " + allItemsinString;
            }

        } catch (Exception e){
            return "[ERROR], failed to get all items.\n";
        }

    }
}
