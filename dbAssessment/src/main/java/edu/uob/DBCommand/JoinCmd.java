package edu.uob.DBCommand;

import edu.uob.BoolOperation;
import edu.uob.Exceptions.interpException;
import edu.uob.FileDealer;
import edu.uob.Table;

import java.util.*;

public class JoinCmd extends ComplexCommand {
    private String firstAttributeName;
    private String secondAttributeName;
    FileDealer fileDealer2;
    Table secondTable;

    int indexFirstTable;
    int indexSecondTable;


    public JoinCmd(String DBname, String firstTableName, String secondTableName, String firstAttributeName, String secondAttributeName) throws interpException {
        super(DBname, firstTableName);
        this.firstAttributeName = firstAttributeName;
        this.secondAttributeName = secondAttributeName;
        fileDealer2 = new FileDealer(getDBName(), secondTableName);
        secondTable = fileDealer2.file2Table();
        swapControl();
        indexFirstTable = getTableIndex(table, this.firstAttributeName);
        indexSecondTable = getTableIndex(secondTable, this.secondAttributeName);
        if (indexSecondTable == -1 || indexFirstTable == -1){
            throw new interpException("[ERROR] Join command can't find common thing to join");
        }
    }

    // swap if firstAttributeName belongs to the secondTable
    public void swapAttribute(){
        String temp = firstAttributeName;
        firstAttributeName = secondAttributeName;
        secondAttributeName = temp;
    }

    public void swapControl(){
        if (firstAttributeName.contains(".")){
            String tableNameFirstAttribute = firstAttributeName.split("\\.")[0];
            if (!tableNameFirstAttribute.equals(getTableName())){
                swapAttribute();
            }
        }else{
            if (!table.getAttributesName().contains(firstAttributeName)){
                swapAttribute();
            }
        }
    }

    private int getTableIndex(Table table, String attribute) throws interpException {
        if (firstAttributeName.split("\\.").length > 2){
            throw new interpException("[ERROR] Attribute name format");
        }
        if (attribute.split("\\.").length == 2){
            return table.indexofAttribute(attribute.split("\\.")[1]);
        }else{
            return table.indexofAttribute(attribute);
        }
    }

    private int numofindexs(int numofAttributesA, int numofAttributesB){
        int ans = numofAttributesA + numofAttributesB - 2;

        if (indexFirstTable != 0){
            ans--;
        }

        if (indexSecondTable != 0){
            ans--;
        }
        return ans;
    }

    private String buildIndexArrAndAttributeLine(int[] indexs, int indexFirstTable, int indexSecondTable){
        int cnt = 0;
        String attributeLine = "id\t";

        for (int i = 1; i < table.getNumofAttributes(); i++) {
            if (i == indexFirstTable){
                continue;
            }
            indexs[cnt++] = i;
            attributeLine = attributeLine + table.getTableName() + "." + table.getAttributesName().get(i) + "\t";
        }

        for (int i = 1; i < secondTable.getNumofAttributes(); i++){
            if (i == indexSecondTable){
                continue;
            }
            indexs[cnt++] = table.getNumofAttributes() + i;
            attributeLine = attributeLine + secondTable.getTableName() + "." + secondTable.getAttributesName().get(i) + "\t";
        }
        return attributeLine;
    }

    @Override
    public String execute() throws interpException {
        List<String> rawJoin = BoolOperation.join(table.getAllItems(), secondTable.getAllItems(), indexFirstTable, indexSecondTable);

        Table temp = new Table(getDBName(), "temp" + (int) (Math.random() * 1000));

        for (int i = 0; i < table.getNumofAttributes() + secondTable.getNumofAttributes(); i++){
            temp.addNewColumn("null");
        }
        temp.updateClass(rawJoin);

        int numofColumns = numofindexs(table.getNumofAttributes(), secondTable.getNumofAttributes());
        int[] indexs = new int[numofColumns];
        String attributeLine = buildIndexArrAndAttributeLine(indexs, indexFirstTable, indexSecondTable);

        List<String> trimmedRes = new ArrayList<>();
        for (int j = 0; j < rawJoin.size(); j++){
            List<String> currentItem = new ArrayList<>();
            for (int i = 0; i < indexs.length; i++){
                currentItem.add(temp.getColumns().get(indexs[i]).getColumnBody().get(j));
            }
            trimmedRes.add(FileDealer.transform2csvLine(currentItem));
        }

        String ans = "[OK]\n" + attributeLine + "\n";
        for (int i = 0; i < trimmedRes.size(); i++){
            ans = ans + (i + 1) + "\t" + trimmedRes.get(i) + "\n";
        }
        return ans;
    }
}
