public abstract class Shape {

    public abstract double getArea();

    public abstract double getC();
    public static void main(String[] args) {
        Circus c1 = new Circus(5);
        Triangle t1 = new Triangle(3, 4, 5);
        Square s1 = new Square(6);
        System.out.println("c1's s and c:" + c1.getArea() + ", " + c1.getC());
        System.out.println("t1's s and c:" + t1.getArea() + ", " + t1.getC());
        System.out.println("s1's s and c:" + s1.getArea() + ", " + s1.getC());
    }
}