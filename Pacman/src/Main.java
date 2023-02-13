import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class Main{
    public static void main(String[] args) throws InterruptedException {
        Field field = new Field(10, 10);
        View view = new View(field);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Cells");
        frame.add(view, BorderLayout.CENTER);

        JButton up = new JButton("up");
        JButton right = new JButton("right");
        JButton down = new JButton("down");
        JButton left = new JButton("left");

        frame.add(up, BorderLayout.NORTH);
        frame.add(right, BorderLayout.EAST);
        frame.add(down, BorderLayout.SOUTH);
        frame.add(left, BorderLayout.WEST);

        up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.move(Direction.toUp);
                view.repaint();
            }
        });

        down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.move(Direction.toDown);
                view.repaint();
            }
        });

        left.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.move(Direction.toLeft);
                view.repaint();
            }
        });

        right.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                field.move(Direction.toRight);
                view.repaint();
            }
        });


        frame.setBounds(100, 100, 800, 600);
        frame.pack();
        frame.setVisible(true);



        /*
        for(int i = 0; i < 20; i++){
            Thread.sleep(500);
            field.move(Direction.toRight);
            view.repaint();
            Thread.sleep(500);
            field.move(Direction.toDown);
            view.repaint();
            Thread.sleep(500);
            field.move(Direction.toRight);
            view.repaint();
        }
        */



    }
}
