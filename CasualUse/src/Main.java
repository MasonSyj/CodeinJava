import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        File dir = new File("databases" + File.separator + "happy");

        dir.mkdirs();
        dir.delete();

//        dir.mkdir();


        HashSet<Integer> set = new HashSet<>();
        set.add(4);
        set.add(14);
        set.add(34);
        set.add(44);
        set.add(46);
        set.add(78);

        int x = Collections.min(set);
        System.out.println(x);
//        set.stream().filter()
    }
}
