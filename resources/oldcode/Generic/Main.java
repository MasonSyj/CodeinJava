package ChapterOneProblemSet;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Collection<Integer> setInt = new Collection<Integer>();
        Collection<String> setString = new Collection<String>();

        setInt.insert(3);
        setInt.insert(4);
        setInt.insert(5);

        setString.insert("hello");
        setString.insert("world");

        System.out.printf("3?: %b, 4?: %b, 5?: %b, 6?: %b\n",
                setInt.isPresent(3), setInt.isPresent(4),setInt.isPresent(5),setInt.isPresent(6));
        System.out.printf("hello?: %b, world?: %b, bristol?: %b\n",
                setString.isPresent("hello"), setString.isPresent("world"), setString.isPresent("bristol"));

/* Doesn't work here
        Object minint = Collections.min(setInt);
        Object minmax = Collections.max(setInt);
*/
        setInt.remove(3);
        setInt.remove(4);
        System.out.printf("empty?: %b\n", setInt.isEmpty());
        setInt.remove(5);
        System.out.printf("empty?: %b\n", setInt.isEmpty());

        HashSet<Integer> hsi = new HashSet<Integer>();

        hsi.add(4);
        hsi.add(5);
        hsi.add(6);

        Object minint = Collections.min(hsi);
        Object maxint = Collections.max(hsi);
        System.out.println("minint:" + minint + " maxint:" + maxint);

        HashSet<String> hss = new HashSet<String>();
        hss.add("apple");
        hss.add("pear");
        hss.add("peach");

        Object minstr = Collections.min(hss);
        Object maxstr = Collections.max(hss);
        System.out.println("minstr:" + minstr + " maxstr:" + maxstr);





    }
}
