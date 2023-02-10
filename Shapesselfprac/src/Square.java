public class Square extends Shape{

    private int a;

    public Square(int a){
        this.a = a;
    }
    @Override
    public double getArea() {
        return a * a;
    }

    @Override
    public double getC() {
        return 4 * a;
    }
}
