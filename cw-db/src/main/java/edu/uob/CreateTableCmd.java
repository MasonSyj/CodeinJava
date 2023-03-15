package edu.uob;

import java.io.File;
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
                    FileDealer fd = new FileDealer(getDBName(), getTableName());
                    Table table = fd.file2Table();
                    List<String> attributes = new ArrayList<String>();
                    for (List<String> x: ref){
                        String attribute = x.get(1);
                        attributes.add(attribute);
                    }
                    table.addColumns(attributes);
                }
                return "OK";
            } else {
                System.out.println("Table already exists.");
                return "Failed. DB already exists";
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "Failed to create Table";
        }
    }
}

