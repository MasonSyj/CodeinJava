package edu.uob;

import javax.swing.*;
import java.awt.*;

public class JFramesMethondExample {
    public static void main(String[] args){
        JFrame window1 = new JFrame("window 1");
        JFrame window2 = new JFrame("window 2");
        window1.setBounds(60, 100, 188, 108);
        window2.setBounds(260, 100, 188, 108);

        window1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Container con = window1.getContentPane();
        con.setBackground(Color.MAGENTA);

        window1.setVisible(true);
        window2.setVisible(true);



    }
}
