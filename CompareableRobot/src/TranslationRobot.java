public class TranslationRobot extends Robot{


    public TranslationRobot(String name, int price, int weight, int power) {
        super(name, price, weight, power);
    }

    @Override
    public void greet(Robot that) {
        that.talk(this);
    }

    @Override
    public void talk(TranslationRobot robot) {
        System.out.println(this.getName() + " as a translation is talking to another translation robot called " + robot.getName());
    }

    @Override
    public void talk(CarrierRobot robot) {
        System.out.println(this.getName()+ " as a translation is talking to a carrier robot called " + robot.getName());
    }

}
