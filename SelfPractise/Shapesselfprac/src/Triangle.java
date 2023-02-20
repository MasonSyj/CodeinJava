public class Triangle extends Shape{

    private int a;
    private int b;
    private int c;

    public Triangle(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }
    @Override
    public double getArea() {
        return 0.25 * Math.sqrt((a + b + c) * (a + b - c) * (a + c - b) * (b + c -a));
    }

    @Override
    public double getC() {
        return a + b + c;
    }
}
