package edu.uob;

import javax.swing.*;
import java.awt.*;

public class WindowMenuExample {


    public static void main(String[] args){

        Icon arrow = new ImageIcon("src/main/java/edu/uob/pngs/arrow.png");
        Icon call = new ImageIcon("src/main/java/edu/uob/pngs/call.png");
        Icon cross = new ImageIcon("src/main/java/edu/uob/pngs/cross.png");
        Icon download = new ImageIcon("src/main/java/edu/uob/pngs/download.png");
        Icon tick = new ImageIcon("src/main/java/edu/uob/pngs/tick.png");

        JMenuBar bar = new JMenuBar();
        JMenu filem = new JMenu("File");
        JMenu editm = new JMenu("Edit");
        bar.add(filem);
        bar.add(editm);
        JMenuItem save = new JMenuItem("save", arrow);
        JMenuItem open = new JMenuItem("open", call);
        JMenuItem copy = new JMenuItem("recent", cross);
        JMenuItem paste = new JMenuItem("paste", download);
        JMenuItem delete = new JMenuItem("delete", tick);
        filem.add(save);
        filem.add(open);
        open.add(copy);
        editm.add(paste);
        paste.add(delete);


        JFrame window = new JFrame();
        window.setJMenuBar(bar);

        window.setBounds(100, 100, 1000, 600);


        Container content = window.getContentPane();
        content.setBackground(Color.YELLOW);

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        window.setResizable(true);

        window.setVisible(true);

    }


}
