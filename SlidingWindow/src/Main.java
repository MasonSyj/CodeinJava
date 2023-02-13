import java.math.BigDecimal;

public class Main {

    public static int maxSubArray(int[] arr, int len){
        int max = 0;
        int cur = 0;
        for (int i = 0; i < len - 1; i++){
            cur += arr[i];
        }

        for (int i = len - 1; i < arr.length; i++){
            cur += arr[i];
            max = Math.max(max, cur);
            cur -= arr[i - (len - 1)];
        }
        return max;
    }
    public static int maxRange(int[] arr){
        int max = 0;
        int curMax = 0;

        for (int i = 0; i < arr.length; i++){
            curMax = Math.max(arr[i], arr[i] + curMax);
            max = Math.max(curMax, max);
        }

        return max;
    }

    public static int smallestSum(int[] arr, int sum){
        int left, right;
        left = 0;
        right = 1;
        int cur = arr[left];
        int ans = 1000;

        for (left = 0; left < arr.length; left++){
            while (cur < sum && right < arr.length){
                cur += arr[right++];
            }
            if (cur >= sum){
//                System.out.printf("left: %d, right: %d, cur: %d\n", left, right, cur);
                ans = Math.min(right - left, ans);
            }

            cur -= arr[left];
        }

        return ans;
    }

    //for now two kinds of Fruits
    public static int maxFruits(int[] arr){
        if(arr.length <= 2){
            return arr.length;
        }

        int ans = 0;
        int cur = 0;
        int firstFruit = -1;
        int secondFruit = -1;
        int index = 0;

        for (int i = 0; i < arr.length; i++){
            if (firstFruit == -1){
                firstFruit = arr[i];
            }else if (secondFruit == -1 && arr[i] != firstFruit){
                secondFruit = arr[i];
                index = i + 1;
                break;
            }
        }


        if (secondFruit == -1) {
            return arr.length;
        }

        ans = cur = index;

        for (int i = index; i < arr.length; i++){
            if (arr[i] == firstFruit || arr[i] == secondFruit){
                cur++;
            }else{
                secondFruit = arr[i];
                firstFruit = arr[i - 1];
                int j = i - 1;
                while (arr[j] == firstFruit){
                    j--;
                }
                cur = i - j;
            }

            ans = Math.max(cur, ans);
        }

        return ans;
    }


    public static void main(String[] args) {
        int arr[] = {-2,1,-3,4,-1,2,1,-5,4};

        for (int num: arr){
            System.out.print(num + " ");
        }

        System.out.println("\nHello world!");

        System.out.println("maxRange: " + maxRange(arr));

        System.out.println("maxSubArray: " + maxSubArray(arr, 3));


        int arr2[] = {4, 2, 2, 7, 8, 1, 2, 8, 10};

        for (int num: arr2){
            System.out.print(num + " ");
        }

        System.out.println("\nsmallestSum: " + smallestSum(arr2, 11));

        int arr3[] = {3,3,3,1,2,1,1,2,3,3,4};

        for(int num: arr3){
            System.out.print(num + " ");
        }

        System.out.println("\ntotalFruit: " + maxFruits(arr3));
    }
}