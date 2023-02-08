package edu.uob;

import java.util.Random;

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

    if(example instanceof MultiVariantShape) {
        System.out.println("This shape has multiple variants");
    } else {
      System.out.println("This shape has only one variant");
    }

    example.getCol();

    example = new Rectangle(4, 6);
    System.out.println(example.calculateArea() + " " + example.calculatePerimeterLength());
    example.setCol(Colour.YELLOW);
    System.out.println(example.toDefualtString());

    if(example instanceof MultiVariantShape) {
      System.out.println("This shape has multiple variants");
    } else {
      System.out.println("This shape has only one variant");
    }

    example.getCol();

    example = new Triangle(3, 4, 5);
    System.out.println(((Triangle) example).getLongestSide() + " " + example.calculatePerimeterLength() + " " + example.calculateArea());
    example.setCol(Colour.WHITE);
    System.out.println(example.toDefualtString());
    example.getCol();

    if(example instanceof MultiVariantShape) {
      System.out.println("This shape has multiple variants");
    } else {
      System.out.println("This shape has only one variant");
    }


    TwoDimensionalShape[] arr = new TwoDimensionalShape[100];
    Random rand = new Random();
    for (int i = 0; i < arr.length; i++){
      int choice = rand.nextInt(3);
      if (choice < 1){
        arr[i] = new Triangle(3, 4, 5);
      }else if (choice < 2){
        arr[i] = new Circle(6);
      }else {
        arr[i] = new Rectangle(7, 8);
      }
    }

    int tricnt = 0;
    for (int i = 0; i < arr.length; i++){
      if (arr[i] instanceof Triangle){
          tricnt++;
      }
    }

    System.out.println("100 shapes hold: " + tricnt + " Triangles");

    example = new Triangle(3, 4, 5);
    example = (Triangle)example;
    System.out.println(((Triangle) example).getVariant());


    example = new Rectangle(4, 6);
    example = (Triangle)example;
    System.out.println(((Triangle) example).getVariant());


  }




}
