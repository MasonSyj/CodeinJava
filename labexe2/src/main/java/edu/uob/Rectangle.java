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
  public int calculatePerimeterLength() {
    return 2 * (width + height);
  }

  public String toString() {
    return super.toString() + "Rectangle of dimensions " + width + " x " + height;
  }

  public void setCol(Colour c){
    col = c;
  }

  public Colour getCol(){
    System.out.println(col);
    return col;
  }
}
