public class Main {


    public static void main(String[] args) {
        Robot c3po = new TranslationRobot("c3p0");
        Robot c4po = new TranslationRobot("c3p0");

        Robot r2d2 = new CarrierRobot("r2d2");
        Robot r2d3 = new CarrierRobot("r2d2");

        c3po.greet(r2d2);
        c3po.greet(c4po);

        r2d2.greet(c3po);
        r2d2.greet(r2d3);
    }
}
