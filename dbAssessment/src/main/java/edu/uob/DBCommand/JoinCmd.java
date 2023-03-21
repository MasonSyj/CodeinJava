package edu.uob.DBCommand;

import edu.uob.BoolOperation;
import edu.uob.DBCommand.Command;
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

    public int getFirstTableIndex(Table firstTable){
        int indexFirstTable = -1;
        if (!firstAttributeName.contains(".")){
            if (!firstTable.getAttributesName().contains(firstAttributeName)){
                swapAttribute();
            }
            indexFirstTable = firstTable.getAttributesName().indexOf(firstAttributeName);
        }else{
            String tableNameFirstName = firstAttributeName.split("\\.")[0];
            String name = firstAttributeName.split("\\.")[1];
            if (!tableNameFirstName.equals(getTableName())){
                swapAttribute();
            }
            indexFirstTable = firstTable.getAttributesName().indexOf(firstAttributeName.split("\\.")[1]);
        }
        return indexFirstTable;
    }

    private int getSecondTableIndex(Table secondTable) {
        if (secondAttributeName.split("\\.").length != 0){
            return secondTable.getAttributesName().indexOf(secondAttributeName.split("\\.")[1]);
        }else{
            return secondTable.getAttributesName().indexOf(secondAttributeName);
        }
    }

    @Override
    public String execute() {
        FileDealer fd1 = new FileDealer(getDBName(), getTableName());
        FileDealer fd2 = new FileDealer(getDBName(), secondTableName);

        Table firstTable = fd1.file2Table();
        Table secondTable = fd2.file2Table();

        List<String> itemsFirstTable = firstTable.getAllItems();
        List<String> itemsSecondTable = secondTable.getAllItems();

        int indexFirstTable = getFirstTableIndex(firstTable);
        int indexSecondTable = getSecondTableIndex(secondTable);

        if (indexSecondTable == -1 || indexFirstTable == -1){
            throw new IllegalArgumentException("Join command can't find common thing to join");
        }

        List<String> rawJoin = BoolOperation.join(itemsFirstTable, itemsSecondTable, indexFirstTable, indexSecondTable);
//        List<String> JoinedRes = new ArrayList<String>();
//        for (String stra : itemsFirstTable){
//            for (String strb: itemsSecondTable){
//                if (Arrays.stream(stra.split("\t")).toList().get(indexFirstTable)
//                        .equals(Arrays.stream(strb.split("\t")).toList().get(indexSecondTable))){
//                    JoinedRes.add(stra + "\t" + strb);
//                    break;
//                }
//            }
//        }

        List<String> TrimmedRes = new ArrayList<String>();
        for (String item: rawJoin){
            int cnt = 1;
            String[] curItems = item.split("\t");
            curItems[indexSecondTable + firstTable.getNumofAttributes()] = "";
            String curItem = cnt++ + "\t" + String.join("\t", curItems);
            TrimmedRes.add(curItem);
        }

        String ans = "";
        for (String str: TrimmedRes){
            ans = ans + str + "\n";
        }


        return "[OK]\n" + ans;
    }


}
