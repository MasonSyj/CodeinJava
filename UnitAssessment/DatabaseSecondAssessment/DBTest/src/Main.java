import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        FileDealer test = new FileDealer("src", "people.tab");
        Table table = test.file2Table();
        table.printTable();

        String first = "name\tmark\tpass\t";
        String sam = "sam\t90\ttrue";
        String tom = "tom\t49\ttalse";

        List<String> markTable = new ArrayList<>();
        markTable.add(first);
        markTable.add(sam);
        markTable.add(tom);

        markTable.remove(0);

        System.out.println(markTable);

        List<String> filtered = markTable.stream()
                .filter(mark -> Integer.parseInt(mark.split("\t")[1]) < 50)
                .collect(Collectors.toList());

        System.out.println("after stream");

        System.out.println(filtered);
//        for(String str: filtered){
//            System.out.println(str);
//        }

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
