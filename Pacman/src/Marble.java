import java.awt.*;

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

    public void draw(Graphics g, int x, int y, int size) {

        g.setColor(new Color(0, 128, 0, 100));//(int)((20-getAge())/20.0*255)));
        g.fillRect(x, y, size, size);
    }

}
