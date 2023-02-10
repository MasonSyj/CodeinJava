package edu.uob;

import javax.swing.*;

public class Jlabel {
	public static void main(String[] args){
		JLabel l1 = new JLabel();
		JLabel l2 = new JLabel("Bristol", SwingConstants.CENTER);
		JLabel l3 = new JLabel("Shanghai", SwingConstants.LEFT);

		JButton b1 = new JButton("Click");

		JFrame window = new JFrame();
		window.setBounds(100, 100, 500, 400);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setResizable(true);

//		window.add(l1);
		window.add(l3);
		window.add(l2);
		window.add(b1);

		String str1 = l1.getText();
		l1.setText("Change");
		float align1 = l1.getAlignmentX();
		float align2 = l1.getAlignmentY();
		l1.setVerticalAlignment(SwingConstants.BOTTOM);
		l1.setHorizontalAlignment(SwingConstants.LEFT);


		window.setVisible(true);
	}
}
