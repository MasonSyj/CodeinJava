import java.awt.*;

public class Pacman extends Cell implements Draw{
    int eatFigures;

    public Pacman(){
        super(true);
        this.eatFigures = 0;
    }

    public void eat(){
        this.eatFigures++;
    }

    @Override
    public void toDefaultString() {
        super.toDefaultString();
        System.out.println("Pacman ");
    }

    @Override
    public void draw(Graphics g, int x, int y, int size) {
        int alpha = (int) 200;
        g.setColor(new Color(0, 0, 0, alpha));//(int)((20-getAge())/20.0*255)));
        g.fillRect(x, y, size, size);
    }

}
