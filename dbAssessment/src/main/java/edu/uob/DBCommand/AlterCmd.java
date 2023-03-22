package edu.uob.DBCommand;

import edu.uob.*;
import edu.uob.Enums.AlterationType;
import edu.uob.Exceptions.interpException;

public class AlterCmd extends Command {
    AlterationType alterationType;
    String attributeName;
    public AlterCmd(String DBName, String tableName, AlterationType type, String attributeName) {
        super(DBName, tableName);
        this.alterationType = type;
        this.attributeName = attributeName;
    }

    @Override
    public String execute() throws interpException {
        if (alterationType == AlterationType.ADD){
            return addAttribute();
        }else{
            return dropAttribute();
        }
    }

    public String addAttribute() throws interpException {
        if (SQLKeywords.SQLKeyWords.contains(attributeName.toUpperCase())){
            throw new interpException("[ERROR], attribute Name cannot be SQL Keywords.");
        }

        // this two line appeared many times, may add to the Parent Class: Command
        FileDealer fd = new FileDealer(getDBName(), getTableName());
        Table table = fd.file2Table();
        if (table.getAttributesName().contains(attributeName)){
            throw new interpException("[ERROR] attribute already exists");
        }else{
            table.addNewColumn(attributeName);
            table.write2File();
            return "[OK] add attribute succesfully";
        }
    }

    public String dropAttribute() throws interpException {
        if (attributeName.equals("id")){
            throw new interpException("[ERROR], you can not drop auto id column");
        }
        FileDealer fd = new FileDealer(getDBName(), getTableName());
        Table table = fd.file2Table();
        if (table.dropColumn(attributeName)){
            return "[OK], drop attribute " + attributeName + " successfully";
        }else{
            throw new interpException("[ERROR], failed to drop attribute " + attributeName);
        }
    }


}
