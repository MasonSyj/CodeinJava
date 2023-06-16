public class CarrierRobot extends Robot{

    public CarrierRobot(String name) {
        super(name);
    }

    @Override
    public void greet(Robot that) {
        that.talk(this);
    }

    @Override
    public void talk(TranslationRobot robot) {
        System.out.println(this.name + " as a carrier robot is talking to another translation robot called " + robot.name);
    }

    @Override
    public void talk(CarrierRobot robot) {
        System.out.println(this.name + " as a carrier robot is talking to a carrier robot called " + robot.name);
    }
}
