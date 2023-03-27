import java.io.File;
import java.util.*;

public class Main {

    static int id;
    public static void main(String[] args) {
        List<Integer> queue = new LinkedList<>();
        for (int i = 0; i < 5; i++){
            queue.add(i + 1);
        }

        int[] queries = new int[4];
        queries[0] = 2;
        queries[1] = 1;
        queries[2] = 2;
        queries[3] = 1;

        for (int i = 0; i < queries.length; i++){
//            queue.indexOf()
            int val = queue.remove(queries[i]);
            queue.add(0, val);
        }
        int num = 9669;
        
//        File dir = new File("databases" + File.separator + "happy");
//
//        dir.mkdirs();
//        dir.delete();
//
////        dir.mkdir();
//
//
//        HashSet<Integer> set = new HashSet<>();
//        set.add(4);
//        set.add(14);
//        set.add(34);
//        set.add(44);
//        set.add(46);
//        set.add(78);
//
//        int x = Collections.min(set);
//        System.out.println(x);
//        set.stream().filter()
    }
}
