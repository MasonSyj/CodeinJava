import javax.swing.*;
import java.awt.*;


public class Grid {
    public Grid(){
        JFrame window = new JFrame("Grid");
        Container mainPanel = window.getContentPane();
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(20, 20));
        Label label[][] = new Label[20][20];
        for (int j = 0; j < 20; j++){
            for (int i = 0; i < 20; i++){
                label[j][i] = new Label();
                if ((i + j) % 2 == 0){
                    label[j][i].setBackground(Color.black);
                }else{
                    label[j][i].setBackground(Color.white);
                }
                board.add(label[j][i]);
            }
        }
        mainPanel.add(board, BorderLayout.CENTER);

        JPanel comment = new JPanel();
        comment.add(new Button("Comment"));


        JButton b1 = new JButton("UP");
        JButton b2 = new JButton("RIGHT");
        JButton b3 = new JButton("DOWN");
        JButton b4 = new JButton("LEFT");


        mainPanel.add(b1, BorderLayout.NORTH);
        mainPanel.add(b2, BorderLayout.EAST);
        mainPanel.add(b3, BorderLayout.SOUTH);
        mainPanel.add(b4, BorderLayout.WEST);

        window.setBounds(100, 100, 500, 400);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] main){
        Grid grid = new Grid();
    }
}
