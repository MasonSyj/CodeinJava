import com.sun.source.tree.Tree;

import java.io.File;
import java.util.*;

public class Main {

    public void levelOrder(TreeNode root, Deque<TreeNode> deque, List<Integer> list){
        if (root == null){
            return;
        }

        list.add(root.val) ;
        deque.add(root.left);
        deque.add(root.right);
        levelOrder(deque.poll(), deque, list);

    }
    public TreeNode reverseOddLevels(TreeNode root) {
        Deque<TreeNode> deque = new ArrayDeque<TreeNode>();
        List<Integer> list = new ArrayList<Integer>();
        levelOrder(root, deque, list);
        int[] arr = list.stream().mapToInt(Integer::intValue).toArray();
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i] + " ");
        }

        return root;
    }
    public static void main(String[] args) {
        int a = 3;
        int b = 4;
        if (a > b){
            System.out.println("a > b");
        } else{
            if (a == 3){
                System.out.println("a == b");
            } else {
                System.out.println("a < b");
            }
        }
<<<<<<< HEAD
=======
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
>>>>>>> 744ad17f5c58949b9b2bbd0e0d326e22f7ecfd78
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
