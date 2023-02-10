package edu.uob;

import javax.swing.*;
import java.awt.*;

public class JText {
	public static void main(String[] args){

		JFrame window = new JFrame();
		window.setBounds(100, 100, 800, 600);
		Container con = window.getContentPane();
		con.setBackground(Color.blue);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setResizable(true);

		window.setTitle("Still Trying");

		JTextField f1 = new JTextField();
		JTextField f2 = new JTextField(5);
		JTextField f3 = new JTextField("Hello World!");
		JTextField f4 = new JTextField("Hello World!", 20);


		JTextArea a1 = new JTextArea();
		JTextArea a2 = new JTextArea(20, 30);
		JTextArea a3 = new JTextArea("Typing at Bristol 2023");
		JTextArea a4 = new JTextArea("What a wonderful words", 10, 20);

		a1.append("a1's text");
//		a2.insert("a2 is inserted into certain place", 20);
		a3.replaceRange("hello", 3, 9);

		window.add(a1);
		window.add(a2);
		window.add(a3);
		window.add(a4);

		window.add(f1);
		window.add(f2);
		window.add(f3);
		window.add(f4);



		String str1 = f1.getText();
		String str2 = a1.getText();
		String str3 = f2.getSelectedText();
		String str4 = a2.getSelectedText();

		window.setVisible(true);


	}
}
