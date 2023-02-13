import java.awt.*;

public class Cell implements Draw{
    boolean isexist;

    public Cell(){
        this.isexist = false;
    }

    public Cell(boolean isexist){
        this.isexist = isexist;
    }


    public void draw(Graphics g, int x, int y, int size) {
        g.setColor(new Color(0, 0, 0, 0));//(int)((20-getAge())/20.0*255)));
        g.fillRect(x, y, size, size);
    }


    public void toDefaultString(){
        if (this.isexist == true){
            System.out.print("exist");
        }else{
            System.out.println("dead");
        }

    }
}
