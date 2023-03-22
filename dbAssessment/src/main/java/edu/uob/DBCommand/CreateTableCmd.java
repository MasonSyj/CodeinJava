package edu.uob.DBCommand;

import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
import edu.uob.SQLKeywords;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CreateTableCmd extends Command {


    private List<String[]> attributeList;
    File fileContainTable;

    public CreateTableCmd(String DBName, String tableName, List<String[]> attributeList){
        super(DBName, tableName);
        this.attributeList = attributeList;
        fileContainTable = new File("databases" + File.separator + getDBName() + File.separator + getTableName());
    }

    @Override
    public String execute() throws interpException {
        if (SQLKeywords.SQLKeyWords.contains(getTableName().toUpperCase())){
            throw new interpException("[ERROR], TableName Name cannot be SQL Keywords.");
        }

        try {
            if (fileContainTable.createNewFile()) {
                String attributeLine = setUpAttributeLine();
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileContainTable));
                writer.write(attributeLine);
                writer.flush();
                return "[OK]";
            } else {
                return "[ERROR]. Table already exists";
            }
        } catch (IOException e) {
            fileContainTable.delete();
            throw new interpException("[ERROR], Failed to create Table");

        }
    }

    public String setUpAttributeLine() throws interpException {
        String ans = "id\t";
        for (int i = 0; i < attributeList.size(); i++){
            for (int j = i + 1; j < attributeList.size(); j++){
                if (attributeList.get(i)[1].equals(attributeList.get(j)[1])){
                    fileContainTable.delete();
                    throw new interpException("[ERROR] one table cannot have two same attribute name");
                }
            }
        }

        for (String[] attributeName: attributeList){
            // attributeName consists of 0.tablename 1.attribute
            if (SQLKeywords.SQLKeyWords.contains(attributeName[1].toUpperCase())){
                fileContainTable.delete();
                throw new interpException("[ERROR] table attribute cannot be SQL Keywords");
            }
            ans = ans + attributeName[1]+ "\t";
        }
        return ans.substring(0, ans.length() - 1);
    }

}

