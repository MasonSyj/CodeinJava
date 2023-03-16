package edu.uob;

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
            FileDealer fd = new FileDealer(getDBName(), getTableName());
            Table table = fd.file2Table();
            List<String> all = table.getAllItems();
            String allItemsinString = all.toString();
            System.out.print("all:");
            System.out.println(allItemsinString + "\n");
            return "[OK] " + allItemsinString;
        } catch (Exception e){
            return "[ERROR], failed to get all items.\n";
        }

    }
}
