import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Robot{

    String name;
    int price;
    int level;

    public Robot(String name, int price, int level){
        this.name = name;
        this.price = price;
        this.level = level;
    }

    public static void main(String[] args) {
        Robot r1 = new Robot("apple", 5000, 8);
        Robot r2 = new Robot("huawei", 6000, 6);
        Robot r3 = new Robot("sanxi", 5500, 7);

        Robot[] list = new Robot[3];
        list[0] = r1;
        list[1] = r2;
        list[2] = r3;

        Arrays.sort(list, (a, b) -> a.level - b.level);

        for (Robot r: list){
            System.out.println(r.name);
        }

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> nameLengths = names.stream()
                .map(x -> x.length())
                .collect(Collectors.toList());

        System.out.println(nameLengths);


    }
}
