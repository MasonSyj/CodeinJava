package edu.uob.DBCommand;

import edu.uob.BoolOperation;
import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
import edu.uob.FileDealer;
import edu.uob.Table;

import java.util.*;

public class JoinCmd extends Command {
    private String secondTableName;
    private String firstAttributeName;
    private String secondAttributeName;


    public JoinCmd(String DBname, String firstTableName, String secondTableName, String firstAttributeName, String secondAttributeName){
        super(DBname, firstTableName);
        this.secondTableName = secondTableName;
        this.firstAttributeName = firstAttributeName;
        this.secondAttributeName = secondAttributeName;
    }

    // swap if firstAttributeName belongs to the secondTable
    public void swapAttribute(){
        String temp = firstAttributeName;
        firstAttributeName = secondAttributeName;
        secondAttributeName = temp;
    }

    public void swapControl(Table firstTable){
        int indexFirstTable = -1;
        if (firstAttributeName.contains(".")){
            String tableNameFirstName = firstAttributeName.split("\\.")[0];
            if (!tableNameFirstName.equals(getTableName())){
                swapAttribute();
            }
        }else{
            if (!firstTable.getAttributesName().contains(firstAttributeName)){
                swapAttribute();
            }
        }
    }

    private int getTableIndex(Table table, String Attributes) {
        if (Attributes.split("\\.").length != 1){
            return table.getAttributesName().indexOf(Attributes.split("\\.")[1]);
        }else{
            return table.getAttributesName().indexOf(Attributes);
        }
    }

    private int numofindexs(int numofAttributesA, int numofAttributesB, int indexFirstTable, int indexofAttributeB){
        int ans = numofAttributesA + numofAttributesB - 2;

        if (indexFirstTable != 0){
            ans--;
        }

        if (indexofAttributeB != 0){
            ans--;
        }
        return ans;
    }

    private String buildIndexArrAndAttributeLine(int[] indexs, Table firstTable, Table secondTable, int indexFirstTable, int indexSecondTable){
        int cnt = 0;
        String attributeLine = "id\t";

        for (int i = 1; i < firstTable.getNumofAttributes(); i++) {
            if (i == indexFirstTable){
                continue;
            }
            indexs[cnt++] = i;
            attributeLine = attributeLine + firstTable.getTableName() + "." + firstTable.getAttributesName().get(i) + "\t";
        }

        for (int i = 1; i < secondTable.getNumofAttributes(); i++){
            if (i == indexSecondTable){
                continue;
            }
            indexs[cnt++] = firstTable.getNumofAttributes() + i;
            attributeLine = attributeLine + secondTable.getTableName() + "." + secondTable.getAttributesName().get(i) + "\t";
        }
        return attributeLine;
    }

    @Override
    public String execute() throws interpException {
        FileDealer fd1 = new FileDealer(getDBName(), getTableName());
        FileDealer fd2 = new FileDealer(getDBName(), secondTableName);

        Table firstTable = fd1.file2Table();
        Table secondTable = fd2.file2Table();

        List<String> itemsFirstTable = firstTable.getAllItems();
        List<String> itemsSecondTable = secondTable.getAllItems();
        swapControl(firstTable);

        int indexFirstTable = getTableIndex(firstTable, firstAttributeName);
        int indexSecondTable = getTableIndex(secondTable, secondAttributeName);

        if (indexSecondTable == -1 || indexFirstTable == -1){
            throw new interpException("[ERROR] Join command can't find common thing to join");
        }

        List<String> rawJoin = BoolOperation.join(itemsFirstTable, itemsSecondTable, indexFirstTable, indexSecondTable);

        Table temp = new Table(getDBName(), "temp" + (int) (Math.random() * 1000));

        int totalLength = firstTable.getNumofAttributes() + secondTable.getNumofAttributes();
        for (int i = 0; i < totalLength; i++){
            temp.addNewColumn("null");
        }
        temp.updateClass(rawJoin);

        int numofColumns = numofindexs(firstTable.getNumofAttributes(), secondTable.getNumofAttributes(), indexFirstTable, indexSecondTable);

        int[] indexs = new int[numofColumns];
        String attributeLine = buildIndexArrAndAttributeLine(indexs, firstTable, secondTable, indexFirstTable, indexSecondTable);

        List<String> trimmedRes = new ArrayList<>();
        for (int j = 0; j < rawJoin.size(); j++){
            List<String> currentItem = new ArrayList<>();
            for (int i = 0; i < indexs.length; i++){
                currentItem.add(temp.getColumns().get(indexs[i]).getColumnBody().get(j));
            }
            trimmedRes.add(FileDealer.transform2csvLine(currentItem));
        }

        int cnt = 1;
        String ans = "[OK]\n" + attributeLine + "\n";
        for (String item: trimmedRes){
            ans = ans + cnt++ + "\t" + item + "\n";
        }

        return ans;

    }


}
