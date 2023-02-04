package edu.uob;

class Rectangle extends TwoDimensionalShape {
  int width;
  int height;

  public Rectangle(int w, int h) {
    width = w;
    height = h;
  }

  @Override
  public double calculateArea() {
    return width * height;
  }

  @Override
  public long calculatePerimeterLength() {
    return 2 * (width + height);
  }

  public String toDefualtString() {
    return super.toDefualtString() + "Rectangle of dimensions " + width + " x " + height;
  }


}
