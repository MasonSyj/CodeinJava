import java.util.Comparator;

public class WeightComparator implements Comparator<Robot> {

    @Override
    public int compare(Robot o1, Robot o2) {
        return o1.getWeight()- o2.getWeight();
    }
}
