package edu.uob.pngs;

import javax.swing.*;

public class Button {
	public static void main(String[] args){
		JButton b1 = new JButton();
		JButton b2 = new JButton("click here");

		String str1 = b2.getLabel();
		b1.setLabel("Ok");
		b2.setActionCommand("Start Exectuing");


	}
}
