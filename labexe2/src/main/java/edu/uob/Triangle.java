package edu.uob;

import java.util.Timer;

public class Triangle extends TwoDimensionalShape{
  private TriangleVariant type;
  private long a;
  private long b;
  private long c;

  public TriangleVariant getVariant(){
    return this.type;
  }

  public TriangleVariant typecheck() {
    if (a <= 0 || b <= 0 || c <= 0 ) {
      return TriangleVariant.ILLEGAL;
    }else if (a == b && b == c){
      return  TriangleVariant.EQUILATERAL;
    }else if (a + b == c || a + c == b || b + c == a){
      return  TriangleVariant.FLAT;
    }else if (a == b || b == c || a == c ){
      return TriangleVariant.ISOSCELES;
    }else if (a * a + b * b == c * c || a * a + c * c == b * b  || b * b + c * c == a * a){
       return TriangleVariant.RIGHT;
    }else if (a + b < c || a + c < b || b + c < a){
       return TriangleVariant.IMPOSSIBLE;
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

  public long getLongestSide(){
    long max = Math.max(a, b);
    return Math.max(max, c);
  }

  public String toDefualtString(){
    return super.toDefualtString() + "Triangle with sides of length " + a + ", " + b + ", " + c;
  }



  @Override
  public double calculateArea() {
    return 0.25 * Math.sqrt((a + b + c) * (a + b - c) * (a + c - b) * (b + c -a));
  }

  @Override
  public long calculatePerimeterLength() {
    return a + b + c;
  }

}
