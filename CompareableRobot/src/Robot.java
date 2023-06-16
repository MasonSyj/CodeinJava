public abstract class Robot {
    String name;
    int price;
    int weight;
    int height;
    int power;

    public Robot(String name) {
        this.name = name;
        this.price = 1000;
        this.weight = 100;
        this.power = 100;
    }

    public abstract void greet(Robot that);

    public abstract void talk(TranslationRobot robot);

    public abstract void talk(CarrierRobot robot);
}
