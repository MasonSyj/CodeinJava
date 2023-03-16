package edu.uob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTableCmd extends Command{

    private List<List<String>> ref;

    public CreateTableCmd(String DBName, String tableName, List<List<String>> ref){
        super(DBName, tableName);
        this.ref = ref;
        execute();
    }

    @Override
    public String execute() {
        try {
            File fileContainTable = new File(getDBName() + File.separator + getTableName());
            if (fileContainTable.createNewFile()) {
                if (ref != null){
                    List<String> attributes = new ArrayList<String>();
                    for (List<String> list: ref){
                        String attribute = list.get(1) + "\t";
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

