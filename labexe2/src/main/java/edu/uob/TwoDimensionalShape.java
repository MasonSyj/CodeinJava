package edu.uob;

class TwoDimensionalShape extends Shapes{

  protected Colour col;

  public double calculateArea(){
    return 0;
  }

  public int calculatePerimeterLength(){
    return 0;
  }

  @Override
  public String toString(){
    return "This is a " + col + " ";
  }

  protected void setCol(Colour c){
    col = c;
  }

  public Colour getCol(){
    System.out.println(col);
    return col;
  }
}
