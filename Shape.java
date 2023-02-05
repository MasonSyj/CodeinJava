/* This is the base class for various shapes.  A Shape object is effectively
a point - it just has a position. */

class Shape {
    protected int x, y;

    Shape(int x0, int y0) {
        x = x0;
        y = y0;
    }

    void draw() {
        System.out.println("Shape at " + x + ", " + y);
    }
}
