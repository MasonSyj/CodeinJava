package edu.uob;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

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

    }
}
