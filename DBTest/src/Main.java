import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        FileDealer test = new FileDealer("src", "people.tab");
        Table table = test.file2Table();
        table.printTable();
    }

//    public static void main(String[] args) {
//        File file = new File("src" + File.separator + "people.tab");
//        BufferedReader reader;
//
//        try {
//            reader = new BufferedReader(new FileReader(file));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        String line = null;
//        String attributeLine = null;
//
//        List<String> attributesList = new ArrayList<String>();
//
//        try {
//            attributeLine = reader.readLine();
//            attributesList = csvLineParse(attributeLine);
//
//            line = reader.readLine();
//
//            while (line != null && line.length() > 0){
//                List valueList = new ArrayList();
//                valueList = valuesParse(line);
//
//                thispeople.setRandomage();
//                arr[cnt++] = thispeople;
//                System.out.println(thispeople.toDefaultString());
//                line = reader.readLine();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        for (String str: attributesList){
//            System.out.print(str + "\t");
//        }

//
//        try{
//            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//            writer.write(firstline);
//            for (int i = 0; i < 3; i++){
//                writer.write(arr[i].toDefaultString());
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try{
//            reader = new BufferedReader(new FileReader(file));
//            line = reader.readLine();
//            while (line != null){
//                System.out.println(line);
//                line = reader.readLine();
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//}


    private static List<String> csvLineParse(String line) {
        List<String> ans = new ArrayList<String>();
        ans = Arrays.stream(line.split("\t")).toList();
        return ans;
    }


}
