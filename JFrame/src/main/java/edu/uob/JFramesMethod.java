package edu.uob;

import javax.swing.*;

public class JFramesMethod {
    public static void main(String[] args){
        JFrame window = new JFrame("Hello world");
        window.setBounds(100, 100, 800, 600);
//      window.setSize(1024, 768);
//      window.setLocation(300, 300);
        window.setVisible(true);
        window.setResizable(true);
//        window.dispose();
//        window.setExtendedState(0);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        */
    }

}