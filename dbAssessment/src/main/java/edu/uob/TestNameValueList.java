package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestNameValueList {
    public static void main(String[] args) {
        Table t = new Table(".", "PhoneBrand");
        t.addNewColumn(new Column("Name"));
        t.addNewColumn(new Column("Brand"));
        t.addNewColumn(new Column("Price"));

        List<String> value = new ArrayList<String>();

        value.add("Pixel 7");
        value.add("Google");
        value.add("7000");
        t.addValue(value);

//        for (int i = 0; i < value.size(); i++){
//            value.remove(i);
//        }
        while (value.size() != 0){
            value.remove(0);
        }

        value.add("Iphone 13");
        value.add("Apple");
        value.add("8000");

        t.addValue(value);

        while (value.size() != 0){
            value.remove(0);
        }

        value.add("Xiaomi 10");
        value.add("Xiaomi");
        value.add("6000");
        t.addValue(value);

        t.addValue(t.csvLineParse("Mate40\tHuawei\t5000"));

        t.updateFile();

        String str = "Xiaomi 10\tXaiomi\t6000";
        String str2 = "Mate40\tHuawei\t5000";
        List<String> res = new ArrayList<>();
        List<String> list = Arrays.asList(str, str2);
        for (String string: list){
            String[] toarray = string.split("\t");
            for (int i = 0; i < toarray.length; i++){
                if (i == 2){ //replace 2 by attributeindex
                    toarray[i] = "8000"; //replace 8000 by the value
                    break;
                }
            }
            String modified = String.join("\t", toarray);
            res.add(modified);
        }
        for (String string: res){
            System.out.println(string);
        }
    }
}
