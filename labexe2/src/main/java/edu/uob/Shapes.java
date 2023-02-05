package edu.uob;

public class Shapes {

  // TODO use this class as then entry point; play around with your shapes, etc
  public static void main(String[] args) {
    Triangle testTriangle = new Triangle(5, 7, 9);
    long longestSide = testTriangle.getLongestSide();
    System.out.println("The longest side of the triangle is " + longestSide);


    TwoDimensionalShape example;
    example = new Circle(6);
    System.out.println(example.calculateArea() + " " + example.calculatePerimeterLength());
    example.setCol(Colour.RED);
    System.out.println(example.toDefualtString());

    example.getCol();

    example = new Rectangle(4, 6);
    System.out.println(example.calculateArea() + " " + example.calculatePerimeterLength());
    example.setCol(Colour.YELLOW);
    System.out.println(example.toDefualtString());

    example.getCol();

    example = new Triangle(3, 4, 5);
    System.out.println(((Triangle) example).getLongestSide() + " " + example.calculatePerimeterLength() + " " + example.calculateArea());
    example.setCol(Colour.WHITE);
    System.out.println(example.toDefualtString());
    example.getCol();
  }
}
