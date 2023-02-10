import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class HashMapDemo {
    public static void main(String [] args){
        String csvFile = "capitals.csv";
        String line = "";
        HashMap<String, String> capitalCities = new HashMap<String, String>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                line = line.replace("\"", "");
                String[] countryDetails = line.split(",");
                capitalCities.put(countryDetails[0].toLowerCase(),countryDetails[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in).useDelimiter("\\n");
        for(int i = 0; i < 10; ++ i) {
            System.out.print("Enter a country ->");
            String s = sc.next();
            if (capitalCities.containsKey(s.toLowerCase())) {
                System.out.printf("The capital of %s is %s\n", s, capitalCities.get(s.toLowerCase()));
            } else {
                System.out.println("Not heard of " + s);
            }
        }
    }
}
