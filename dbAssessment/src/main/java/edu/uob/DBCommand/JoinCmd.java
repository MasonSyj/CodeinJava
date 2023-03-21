package edu.uob.DBCommand;

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

    @Override
    public String execute() {
        FileDealer fd1 = new FileDealer(getDBName(), getTableName());
        FileDealer fd2 = new FileDealer(getDBName(), secondTableName);

        Table firstTable = fd1.file2Table();
        Table secondTable = fd2.file2Table();

        List<String> itemsFirstTable = firstTable.getAllItems();
        List<String> itemsSecondTable = secondTable.getAllItems();

        int indexFirstTable = -1;
        // swap if firstAttributeName belongs to the secondTable
        if (!firstAttributeName.contains(".")){
            if (!firstTable.getAttributesName().contains(firstAttributeName)){
                String temp = firstAttributeName;
                firstAttributeName = secondAttributeName;
                secondAttributeName = temp;
            }
            indexFirstTable = firstTable.getAttributesName().indexOf(firstAttributeName);
        }else{
            String tableNameFirstName = firstAttributeName.split("\\.")[0];
            if (!tableNameFirstName.equals(getTableName())){
                String temp = firstAttributeName;
                firstAttributeName = secondAttributeName;
                secondAttributeName = temp;
            }
            String name = firstAttributeName.split("\\.")[1];
            indexFirstTable = firstTable.getAttributesName().indexOf(name);
        }


        int indexSecondTable = -1;
        if (secondAttributeName.split("\\.").length != 0){
            indexSecondTable = secondTable.getAttributesName().indexOf(secondAttributeName.split("\\.")[1]);
        }else{
            indexSecondTable = secondTable.getAttributesName().indexOf(secondAttributeName);
        }

        if (indexSecondTable == -1 || indexFirstTable == -1){
            throw new IllegalArgumentException("Join command can't find common thing to join");
        }

        List<String> Joinedres = new ArrayList<String>();
        for (String stra : itemsFirstTable){
            for (String strb: itemsSecondTable){
                if (Arrays.stream(stra.split("\t")).toList().get(indexFirstTable)
                        .equals(Arrays.stream(strb.split("\t")).toList().get(indexSecondTable))){
                    Joinedres.add(stra + "\t" + strb);
                    break;
                }
            }
        }

        List<String> Trimmedres = new ArrayList<String>();
        for (String item: Joinedres){
            String[] curItem = item.split("\t");
            for (int i = 0; i < curItem.length; i++){
                if (i == indexSecondTable){
                    curItem[i] = "";
                    break;
                }
            }
            Trimmedres.add(String.join("\t", curItem));
        }
        return "[OK] " + Trimmedres;
    }
}
