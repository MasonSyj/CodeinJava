package edu.uob;

import java.util.Timer;

public class Triangle extends TwoDimensionalShape{
  private TriangleVariant type;
  private int a;
  private int b;
  private int c;

  public TriangleVariant typecheck() {
    if (a == b && b == c) {
      return TriangleVariant.EQUILATERAL;
    }else if (a == b || b == c || a == c ){
      return TriangleVariant.ISOSCELES;
    }else if (isPythagoras()){
       return TriangleVariant.RIGHT;
    }else if (a + b == c || a + c == b || b + c == a){
       return  TriangleVariant.FLAT;
    }else if (a + b < c || a + c < b || b + c < a){
       return TriangleVariant.IMPOSSIBLE;
    }else if (a == 0 || b == 0 || c ==0){
       return  TriangleVariant.ILLEGAL;
    }else{
       return TriangleVariant.SCALENE;
    }
  }

  public Triangle(int a, int b, int c){
    this.a = a;
    this.b = b;
    this.c = c;
    this.type = typecheck();
  }

  public int getLongestSide(){
    int max = a > b? a: b;
    return max > c? max: c;
  }

  @Override
  public String toString(){
/*
      String res = "This is a Triangle with sides of length X, Y, Z";
      res.replace("X", String.valueOf(a));
      res.replace("Y", String.valueOf(b));
      res.replace("Z", String.valueOf(c));
*/
    return super.toString() + "Triangle with sides of length " + a + ", " + b + ", " + c;
  }



  @Override
  public double calculateArea() {
    return 0.25 * Math.sqrt((a + b + c) * (a + b - c) * (a + c - b) * (b + c -a));
  }

  @Override
  public int calculatePerimeterLength() {
    return a + b + c;
  }



  public boolean isPythagoras(){
    int max = a > b? a: b;
    max = max> c?max:c;
    int short1, short2;
    if (c == max){
      short1 = a;
      short2 = b;
    }else if (b == max){
      short1 = a;
      short2 = c;
    }else {
      short1 = b;
      short2 = c;
    }

    if (short1 * short1 + short2 * short2 == max * max){
      return true;
    }else{
      return false;
    }
  }
}
