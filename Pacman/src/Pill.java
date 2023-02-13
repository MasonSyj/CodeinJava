public class Pill extends Food implements Draw{

    public Pill() {
        super(150);
    }

    public Pill(int points){
        super(points);
    }

    @Override
    public void toDefaultString() {
        super.toDefaultString();
        System.out.println("a pill. ");
    }

    @Override
    public void draw() {
        System.out.print("P");
    }
}
