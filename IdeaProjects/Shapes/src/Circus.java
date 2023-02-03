public class Circus extends Shape{

    private int r;

    public Circus(int r){
        this.r = r;
    }
    @Override
    public double getArea() {
        return 0.5 * 3.1415926 * r * r;
    }

    @Override
    public double getC() {
        return 2 * 3.1415926 * r;
    }
}
