import java.awt.*;

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

    public void draw(Graphics g, int x, int y, int size) {
        g.setColor(new Color(255, 255, 0, 100));//(int)((20-getAge())/20.0*255)));
        g.fillRect(x, y, size, size);
    }
}
