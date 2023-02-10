package edu.uob;

import javax.swing.*;
import java.awt.*;

public class CheckRadio {
	public static void main(String[] args){
		JCheckBox c1 = new JCheckBox();
		JCheckBox c2 = new JCheckBox("正确");
		JCheckBox c3 = new JCheckBox("错误", false);

		JRadioButton r1 = new JRadioButton();
		JRadioButton r2 = new JRadioButton("Hello", true);
		JRadioButton r3 = new JRadioButton("2023", false);

//		Boolean b1 = c1.getState();

		JFrame window = new JFrame();
		window.setBounds(100, 100, 500, 400);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setTitle("Check Box and Radiob Box");
		window.add(c1);
		window.add(c2);
		window.add(c3);

		window.add(r1);
		window.add(r2);
		window.add(r3);

		window.setVisible(true);
	}
}
