package edu.uob;

import java.util.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Test implements ActionListener, MouseListener{
    JTextField inputBox;
    JTextField outputBox;

    public static void main(String[] main){



        Test test = new Test();

        test.inputBox = new JTextField(10);
        test.outputBox = new JTextField(10);
        JFrame window = new JFrame("Test");
        window.setBounds(100, 100, 250, 300);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
//        panel.add(test.inputBox);
//        panel.add(test.outputBox);
        window.add(panel);
        window.setVisible(true);
        String command = test.inputBox.getText();
        test.inputBox.addActionListener(e -> test.actionPerformed(e));
        window.addMouseListener(test);
    }


    public void actionPerformed(ActionEvent event) {
        String command = inputBox.getText();
      //  inputBox.setText("");
        System.out.println(command);
        outputBox.setText(command);
        int currentRow = Character.toLowerCase(command.charAt(0)) - 'a';
        int currentCol = command.charAt(1) - '1';
        System.out.println(currentRow + " "+ currentCol);
    }
    public void mousePressed(MouseEvent event) {
        System.out.println(event.getX() + " " + event.getY());
        if (event.getX() < 35) {
            if (event.isPopupTrigger()) System.out.println(1);
            else if (event.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) System.out.println(2);
            else System.out.println("getX < 35");
        }
        if (event.getY() < 35) {
            if (event.isPopupTrigger()) System.out.println(1);
            else if (event.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) System.out.println(2);
            else System.out.println("getY < ");
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {





g2
c3
a5
a3
e1
a6
d2
b4
g1
c2
f3
f6
f0
g6
d3
b1
d4
c5
b5
g3
g0
a1
e0
g4
d0
f5
d5
d1
f4
e4
e6
a0
b3
d6
a2
a4
c1
e3
g5
b2
f1
e5
b6
c4
c0
f2
c6
b0
e2














    }
}
