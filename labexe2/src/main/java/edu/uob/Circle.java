package edu.uob;

class Circle extends TwoDimensionalShape {
  int radius;

  public Circle(int r) {
    radius = r;
  }

  @Override
  public double calculateArea() {
    return (int) Math.round(Math.PI * radius * radius);
  }
  @Override
  public long calculatePerimeterLength() {
    return (int) Math.round(Math.PI * radius * 2.0);
  }

  @Override
  public String toString() {
    return super.toDefualtString() + "Circle with radius " + radius;
  }

}
