import java.util.Comparator;

public abstract class Robot implements Comparable<Robot>{
    String name;
    int price;
    int weight;
    int height;
    int power;

    public Robot(String name, int price, int weight, int power) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.power = power;
    }

    public abstract void greet(Robot that);

    public abstract void talk(TranslationRobot robot);

    public abstract void talk(CarrierRobot robot);

    public String toString() {
        return "name: " + this.name + " price: " + this.price + " weight: " + this.weight + " power: " + this.power;
    }

    public int compareTo(Robot b) {
        return this.price - b.price;
    }

}
