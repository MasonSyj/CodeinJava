package edu.uob;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FuncTest {
    private List<String> attributesName;
    public static void main(String[] args) throws IOException {
        File filetest = new File("aFile");
        filetest.createNewFile();

        Table t = new Table(".", "PhoneBrand");
        t.addNewColumn(new Column("Name"));
        t.addNewColumn(new Column("Brand"));
        t.addNewColumn(new Column("Price"));

        List<String> value = new ArrayList<String>();

        value.add("Pixel 7");
        value.add("Google");
        value.add("6000");
        t.addValue(value);


//        for (int i = 0; i < value.size(); i++){
//            value.remove(i);
//        }
        while (value.size() != 0){
            value.remove(0);
        }

//        System.out.println(value.size());

        value.add("Iphone 13");
        value.add("Apple");
        value.add("8000");

        t.addValue(value);

        while (value.size() != 0){
            value.remove(0);
        }

//        System.out.println(value.size());

        value.add("Xiaomi 10");
        value.add("Xiaomi");
        value.add("7000");
        t.addValue(value);

        t.updateFile();

        FuncTest test = new FuncTest();
        test.attributesName = t.getAttributesName();

        List<String> res = t.predicate("Price < 8000");

        System.out.println("---------Separate Line-------------");
        for (String str: res){
            System.out.println(str);
        }

        System.out.println("---------Separate Line-------------");

        res = t.predicate("Brand like Apple");
        for (String str: res){
            System.out.println(str);
        }


    }

//    public void predicateOne(Table t){
//        t.getColumns().stream().filter().collect(Collectors.toList());
//    }
    /*
    Condition examples:
    age > 30;
    (age > 30);
    mark > 60;
    (mark > 60);


    age > 30 AND mark > 60
    age > 30 OR mark > 60
     */
}
