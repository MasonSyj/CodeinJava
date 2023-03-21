package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader
                (new FileReader("databases" + File.separator + "vrwbjdwuri" + File.separator + "marks"));

        String line = bufferedReader.readLine();
        while (line != null){
            System.out.println(line);
            line = bufferedReader.readLine();
        }

        Table t = new Table(".", "PhoneBrand");
        t.addNewColumn("Name");
        t.addNewColumn("Brand");
        t.addNewColumn("Price");

        List<String> value1 = new ArrayList<String>();
        value1.add("Pixel 7");
        value1.add("Google");
        value1.add("6000");
        t.addItem(value1, ItemType.NEW);

        List<String> value2 = new ArrayList<String>();
        value2.add("Iphone 13");
        value2.add("Apple");
        value2.add("8000");

        t.addItem(value2, ItemType.NEW);

        List<String> value3 = new ArrayList<String>();
        value3.add("Xiaomi 10");
        value3.add("Xiaomi");
        value3.add("7000");
        t.addItem(value3, ItemType.NEW);

        t.write2File();

        List<String> res = t.predicate("Price > 7000");
        for (String item: res){
            System.out.println(item);
        }

    }
}


//    public static void main(String[] args) {
//        double x = 43.5;
//        double y = 33.9;
//
//        String strx = "43.5";
//        String stry = "33.9";
//        if (Double.valueOf(strx) > Double.valueOf(stry)){
//            System.out.println("strx > stry");
//        }
//
//        strx = "6";
//        stry = "9.5";
//        stry = "9.5";
//
//        if (Double.valueOf(strx) > Double.valueOf(stry)){
//            System.out.println("strx > stry");
//        }else{
//            System.out.println("3: stry > strx");
//        }
//
//    }