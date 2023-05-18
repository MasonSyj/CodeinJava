import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.add("hello");
        list.add("world");
        list.add("from");
        list.add("bristol");

        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator);
            iterator.next();
        }

        iterator = list.iterator();
        while (iterator.hasNext()){
            String currentString = (String) iterator.next();
            System.out.println(currentString);
        }

        Iterator<String> iterator2 = list.iterator();
        while (iterator2.hasNext()){
            String currentString = iterator2.next();
            System.out.println(currentString);
        }
    }
}
