import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {



    public static void main(String[] args) {
        File file = new File("src" + File.separator + "people.tab");
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        People[] arr = new People[3];
        int cnt = 0;
        String line = null;
        String firstline = null;

        try {
            firstline = reader.readLine();
            System.out.println("firstline: " + firstline);
            line = reader.readLine();
            while (line != null && line.length() > 0){
                People thispeople = People.lineParser(line);
                thispeople.setRandomage();
                arr[cnt++] = thispeople;
                System.out.println(thispeople.toDefaultString());
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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


    }



}
