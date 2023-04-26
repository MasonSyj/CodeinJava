import java.util.*;

public class Solution {
        public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
            Map<Integer, Integer> ref = new HashMap<Integer, Integer>();

            for (int[] couple: items1){
                ref.put(couple[0], couple[1]);
            }

            for (int[] couple: items2){
                if (!ref.containsKey(couple[0])){
                    ref.put(couple[0], couple[1]);
                } else {
                    ref.put(couple[0], ref.get(couple[0]) + couple[1]);
                }
            }

            List<Integer> values = ref.keySet().stream().sorted((a, b) -> a - b).toList();
            List<List<Integer>> answer = new ArrayList<>();

            for (Integer value: values){
                answer.add(Arrays.asList(value, ref.get(value)));
            }
            return answer;
        }
}
