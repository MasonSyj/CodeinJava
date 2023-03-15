package edu.uob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinCmd extends Command{
    private String secondTableName;

    //Key: Attribute, Value: TableitBelongto
    List<List<String>> ref;


    public JoinCmd(String DBname, String firstTableName, String secondTableName, List<List<String>> ref){
        super(DBname, firstTableName);
        this.secondTableName = secondTableName;
        this.ref = ref;
    }

    @Override
    public String execute() {
        return null;
    }
}
