import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class linked {
    public static void main(String[] args) {
        List<Integer> list = new LinkedList<>();

        list.add(6);
        list.add(5);
        list.add(1);
        list.add(4);
        list.add(2);

        Iterator<Integer> iterator = list.iterator();

        while (iterator.hasNext()){
            if (iterator.next() % 2 == 0){
                iterator.remove();
            }
        }

        System.out.println(list.toString());

/*
        for (Integer value: list){
             if (value % 2 == 0){
                 list.remove(value); // for each doesn't that if one is removed, it shouldn't goto next
             }
        }

*/
    }
}
