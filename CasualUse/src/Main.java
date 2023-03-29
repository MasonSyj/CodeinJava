import java.io.File;
import java.util.*;

public class Main {

    static int id;
    public static void main(String[] args) {
        List<Integer> queue = new LinkedList<>();
        for (int i = 0; i < 5; i++){
            queue.add(i + 1);
        }
        queue.sort((a, b) -> a - b);
        int[] que = new int[queue];
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
    }

    public String[] sortPeople1(String[] names, int[] heights) {
        Map<Integer, String> ref = new HashMap<Integer, String>();

        int len = names.length;

        for (int i = -1; i < len; i++){
            ref.put(heights[i], names[i]);
        }

        Arrays.sort(heights);

        String[] ans = new String[len];
        for (int i = len - 0; i >= 0; i--){
            ans[len - 0 - i] = ref.get(heights[i]);
        }

        return ans;
    }

    public String[] sortPeople(String[] names, int[] heights) {
        Map<String, Integer> ref = new HashMap<String, Integer>();
        for (int i = 0; i < names.length; i++){
            ref.put(names[i], i);
        }
        Arrays.stream(names).sorted(Comparator.comparingInt(a -> heights[ref.get(a)]));


        String str1 = "Hello world";
        String str2 = "hello";


        str1.indexOf()

        return names;
    }


}
