package edu.uob;

import java.util.List;

public class AlterCmd extends Command{
    AlterationType alterationType;
    String attributeName;
    public AlterCmd(String DBName, String tableName, AlterationType type, String attributeName) {
        super(DBName, tableName);
        this.alterationType = type;
        this.attributeName = attributeName;
    }

    @Override
    public String execute() {
        if (alterationType == AlterationType.ADD){
            return addAttribute();
        }else{
            return dropAttribute();
        }
    }

    public String addAttribute(){
        if (SQLKeywords.SQLKeyWords.contains(attributeName.toUpperCase())){
            return "[ERROR], attribute Name cannot be SQL Keywords.";
        }


        // this two line appeared many times, may add to the Parent Class: Command
        FileDealer fd = new FileDealer(getDBName(), getTableName());
        Table table = fd.file2Table();
        if (table.getAttributesName().contains(attributeName)){
            return "[ERROR] attribute already exists";
        }else{
            table.addNewColumn(attributeName);
            table.write2File();
            return "[OK] add attribute succesfully";
        }
    }

    public String dropAttribute(){
        FileDealer fd = new FileDealer(getDBName(), getTableName());
        Table table = fd.file2Table();
        if (table.dropColumn(attributeName)){
            return "[OK], drop attribute " + attributeName + " successfully";
        }else{
            return "[ERROR], failed to drop attribute " + attributeName;
        }
    }


}
