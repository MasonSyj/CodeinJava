package edu.uob;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Test implements ActionListener {
    JTextField inputBox;
    JTextField outputBox;



    public static void main(String[] main){
        Test test = new Test();

        test.inputBox = new JTextField(10);
        test.outputBox = new JTextField(10);
        JFrame window = new JFrame("Test");
        window.setBounds(100, 100, 400, 300);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.add(test.inputBox);
        panel.add(test.outputBox);
        window.add(panel);
        window.setVisible(true);
        String command = test.inputBox.getText();
        test.inputBox.addActionListener(e -> test.actionPerformed(e));
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

}
