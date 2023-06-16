public class TranslationRobot extends Robot{


    public TranslationRobot(String name) {
        super(name);
    }

    @Override
    public void greet(Robot that) {
        that.talk(this);
    }

    @Override
    public void talk(TranslationRobot robot) {
        System.out.println(this.name + " as a translation is talking to another translation robot called " + robot.name);
    }

    @Override
    public void talk(CarrierRobot robot) {
        System.out.println(this.name + " as a translation is talking to a carrier robot called " + robot.name);
    }
}
