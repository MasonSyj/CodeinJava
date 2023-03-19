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

    public CreateTableCmd(String DBName, String tableName, List<String[]> attributeList){
        super(DBName, tableName);
        this.attributeList = attributeList;
        execute();
    }

    @Override
    public String execute() {
        try {
            File fileContainTable = new File("databases" + File.separator + getDBName() + File.separator + getTableName());
            //this seems to nested here, change, +
            if (fileContainTable.createNewFile()) {
                if (attributeList != null){
                    List<String> attributes = new ArrayList<String>();
                    for (String[] list: attributeList){
                        String attribute = list[1]+ "\t";
                        attributes.add(attribute);
                    }
                    String attributeLine = attributes.toString();
                    attributeLine = attributeLine.replace("[", "");
                    attributeLine = attributeLine.replace(",", "");
                    attributeLine = attributeLine.replace("]", "");
                    attributeLine = attributeLine.replace(" ", "");
                    attributeLine = attributeLine.substring(0, attributeLine.length() - 1);
                    System.out.println(attributeLine);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileContainTable));
                    writer.write(attributeLine);
                    writer.newLine();
                    writer.flush();
                }
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

