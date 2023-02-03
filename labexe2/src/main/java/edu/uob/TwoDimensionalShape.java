package edu.uob;

abstract class TwoDimensionalShape extends Shapes{

  protected Colour col;

  abstract public double calculateArea();

  abstract public long calculatePerimeterLength();


  public String toDefualtString(){
    return "This is a " + this.col + " ";
  }

  protected void setCol(Colour c){
    this.col = c;
  }

  public Colour getCol(){
    return this.col;
  }
}
