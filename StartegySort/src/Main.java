import java.util.Arrays;

public class Main {
        public static void main(String[] args) {
            Sorter sorter = new Sorter();

            int[] array = {5, 2, 8, 1, 9};

            sorter.setSortingStrategy(new BubbleSortStrategy());
            sorter.sortArray(array);
            System.out.println("Bubble Sort: " + Arrays.toString(array));

            sorter.setSortingStrategy(new QuickSortStrategy());
            sorter.sortArray(array);
            System.out.println("Quick Sort: " + Arrays.toString(array));

            sorter.setSortingStrategy(new MergeSortStrategy());
            sorter.sortArray(array);
            System.out.println("Merge Sort: " + Arrays.toString(array));
        }
    }
