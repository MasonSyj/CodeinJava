public class Marble extends Food implements Draw{

    public Marble() {
        super(100);
    }


    public Marble(int points) {
        super(points);
    }

    @Override
    public void toDefaultString() {
        super.toDefaultString();
        System.out.println("a marble. ");
    }

    @Override
    public void draw() {
        System.out.print("M");
    }
}
