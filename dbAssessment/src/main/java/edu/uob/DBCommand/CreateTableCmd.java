package edu.uob.DBCommand;

import edu.uob.DBCommand.Command;
import edu.uob.SQLKeywords;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CreateTableCmd extends Command {

    //maybe List<String> will be just fine
    private List<String[]> attributeList;
    // needed? for interface
//    private String attributeNames;

    public CreateTableCmd(String DBName, String tableName, List<String[]> attributeList){
        super(DBName, tableName);
        this.attributeList = attributeList;
    }

    @Override
    public String execute() {
        File fileContainTable = new File("databases" + File.separator + getDBName() + File.separator + getTableName());
        if (SQLKeywords.SQLKeyWords.contains(getTableName().toUpperCase())){
            return "[ERROR], TableName Name cannot be SQL Keywords.";
        }

        try {
            if (fileContainTable.createNewFile()) {
                String attributeLine = setUpAttributeLine();
                if (attributeLine == null){
                    return "[ERROR], AttributeName cannot be keywords.";
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(fileContainTable));
                writer.write(attributeLine);
                writer.flush();
                return "[OK]";
            } else {
                return "[ERROR]. Table already exists";
            }
        } catch (IOException e) {
            if (fileContainTable.exists()){
                fileContainTable.delete();
            }
            return "[ERROR], Failed to create Table";
        }
    }

    public String setUpAttributeLine(){
        String ans = "id\t";
        for (String[] attributeName: attributeList){
            // attributeName consists of 0.tablename 1.attribute
            if (SQLKeywords.SQLKeyWords.contains(attributeName[1].toUpperCase())){
                return null;
            }
            ans = ans + attributeName[1]+ "\t";
        }
        return ans.substring(0, ans.length() - 1);
    }

}

