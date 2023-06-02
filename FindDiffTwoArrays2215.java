class Solution {
    public List<List<Integer>> findDifference(int[] nums1, int[] nums2) {
                List<Integer> answer1 = Arrays.stream(nums1).boxed().toList();
        List<Integer> answer2 = Arrays.stream(nums2).boxed().toList();
        
        answer1 = new ArrayList<>(answer1.stream().distinct().toList());
        answer2 = new ArrayList<>(answer2.stream().distinct().toList());


        List<Integer> answer1Clone = new ArrayList<Integer>(answer1);

        for (int num: answer1Clone){
            if (answer2.contains(num)){
                answer1.remove(Integer.valueOf(num));
                answer2.remove(Integer.valueOf(num));
            }
        }

        List<List<Integer>> answer = new ArrayList<>();
        answer.add(answer1);
        answer.add(answer2);

        return answer;
    }
}
