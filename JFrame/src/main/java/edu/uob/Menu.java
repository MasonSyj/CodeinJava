package edu.uob;

import javax.swing.*;
import java.awt.*;

public class Menu {


    public static void main(String[] args){
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("abc");
        JMenuItem itema = new JMenuItem("File");
        JMenuItem itemb = new JMenuItem("Edit");

//        JMenuItem itema = new JMenuItem("File", Icon icon);

        menu.add(itema);
        int cnt = menu.getItemCount();
        System.out.println("cnt: " + cnt);

        menu.add(itemb);
        cnt = menu.getItemCount();
        System.out.println("cnt: " + cnt);

        JMenuItem reta = menu.getItem(0);



    }
}
