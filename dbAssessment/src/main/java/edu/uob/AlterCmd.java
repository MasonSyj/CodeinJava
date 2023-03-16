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
        // this two line appeared many times, may add to the Parent Class: Command
        FileDealer fd = new FileDealer(getDBName(), getTableName());
        Table table = fd.file2Table();
        if (table.getAttributesName().contains(attributeName)){
            return "[ERROR] attribute already exists";
        }else{
            table.addNewColumn(new Column(attributeName));
            return "[OK] add attribute succesfully";
        }

    }

    public String dropAttribute(){
        FileDealer fd = new FileDealer(getDBName(), getTableName());
        Table table = fd.file2Table();
        if (table.dropExistedColumn(attributeName)){
            return "[OK], drop attribute " + attributeName + " successfully";
        }else{
            return "[ERROR], failed to drop attribute " + attributeName;
        }
    }


}
