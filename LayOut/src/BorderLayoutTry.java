import javax.swing.*;
import java.awt.*;

public class BorderLayoutTry {
    private JFrame window;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;

    public BorderLayoutTry(String str1, String str2,String str3, String str4){
        b1 = new JButton(str1);
        b2 = new JButton(str2);
        b3 = new JButton(str3);
        b4 = new JButton(str4);

        init();
        window.add(b1, BorderLayout.NORTH);
        window.add(b2, BorderLayout.EAST);
        window.add(b3, BorderLayout.SOUTH);
        window.add(b4, BorderLayout.WEST);
        //window.add(b2, BorderLayout.SOUTH); can't add two same thing, it will cover

    }

    public void init(){
        window = new JFrame("Button Example");
        window.setLayout(new java.awt.BorderLayout());

        window.setBounds(100, 100, 500, 400);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] main){
        BorderLayoutTry b1 = new BorderLayoutTry("UP", "RIGHT", "DOWN", "LEFT");
    }


}
