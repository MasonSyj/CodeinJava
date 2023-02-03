package edu.uob;

public class Shapes {

  // TODO use this class as then entry point; play around with your shapes, etc
  public static void main(String[] args) {
    Triangle testTriangle = new Triangle(5, 7, 9);
    int longestSide = testTriangle.getLongestSide();
    System.out.println("The longest side of the triangle is " + longestSide);


    var c1 = new Circle(5);
    System.out.println(c1.calculateArea() + " " + c1.calculatePerimeterLength());
    c1.setCol(Colour.RED);
    System.out.println(c1.toString());

    c1.getCol();

    var c2 = new Rectangle(4, 6);
    System.out.println(c2.calculateArea() + " " + c2.calculatePerimeterLength());
    c2.setCol(Colour.YELLOW);
    System.out.println(c2.toString());
    
    c2.getCol();

    var c3 = new Triangle(3, 4, 5);
    System.out.println(c3.getLongestSide() + " " + c3.calculatePerimeterLength() + " " + c3.calculateArea());
    c3.setCol(Colour.WHITE);
    System.out.println(c3.toString());
    c3.getCol();
  }
}
