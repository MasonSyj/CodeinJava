package edu.uob;

import java.awt.*;
import javax.swing.*;

public class ColorFont {
	public static void main(String[] args){
		Color c1 = new Color(66, 66, 66);
		Color c2 = new Color(245, 123, 97);

//		Font f1 = new Font(...);

		JLabel l1 = new JLabel("Hello");
		l1.setBackground(Color.PINK);
		l1.setForeground(Color.BLUE);

		c1 = l1.getBackground();
		c2 = l1.getForeground();

		String[] fontlib = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		//or
		String[] alternative = Toolkit.getDefaultToolkit().getFontList();

		l1.setSize(200, 200);
		Dimension d1 = l1.getSize();

		l1.setLocation(50, 70);
		Point p1 = l1.getLocation();

		l1.setBounds(40, 40, 40, 40);
		Rectangle r1 = l1.getBounds();

		l1.setEnabled(true);
		l1.setVisible(true);




	}
}
