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
    }
}
