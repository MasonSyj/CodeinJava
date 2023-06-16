import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        Robot c3po = new TranslationRobot("c3p0", 3000, 10, 50);
        Robot c4po = new TranslationRobot("c4p0", 4000, 12, 45);

        Robot r2d2 = new CarrierRobot("r2d2", 2000, 20, 100);
        Robot r2d3 = new CarrierRobot("r2d3", 5000, 18, 110);

        c3po.greet(r2d2);
        c3po.greet(c4po);

        r2d2.greet(c3po);
        r2d2.greet(r2d3);

        List<Robot> list = new ArrayList<>();
        list.add(c3po);
        list.add(c4po);
        list.add(r2d2);
        list.add(r2d3);

        Collections.sort(list);

        for (Robot robot: list) {
            System.out.println(robot.toString());
        }
        System.out.println("--------------------------------");

        Collections.sort(list, new WeightComparator());

        for (Robot robot: list) {
            System.out.println(robot.toString());
        }
        System.out.println("--------------------------------");


        Collections.sort(list, new PowerComparator());

        for (Robot robot: list) {
            System.out.println(robot.toString());
        }
        System.out.println("--------------------------------");
    }
}
