package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTableCmd extends Command{

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
        try {
            File fileContainTable = new File("databases" + File.separator + getDBName() + File.separator + getTableName());
            //this seems to nested here, change, +
            if (fileContainTable.createNewFile()) {
                String attributeLine = "id\t";
                // attributeName consists of 0.tablename 1.attribute
                for (String[] attributeName: attributeList){
                    attributeLine = attributeLine + attributeName[1]+ "\t";
                }
                attributeLine = attributeLine.substring(0, attributeLine.length() - 1);
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileContainTable));
                writer.write(attributeLine);
                writer.flush();
                return "[OK]";
            } else {
                System.out.println("Table already exists.");
                return "[ERROR]. DB already exists";
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "[ERROR], Failed to create Table";
        }
    }

}

