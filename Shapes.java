/* Demonstrate dispath by getting different shapes to draw themselves. */

class Shapes {
    public static void main(String[] args) {
        Shape[] shapes = new Shape[3];
        shapes[0] = new Circle(1,2,3);
        shapes[1] = new Square(4,5,6);
        shapes[2] = new Shape(7,8);
        for (Shape s : shapes) s.draw();
    }
}
