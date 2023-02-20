package edu.uob;

import javax.swing.*;

public class JCombo {

	public static void main(String[] args){
		JFrame window = new JFrame();

		window.setTitle("Combo");
		window.setBounds(100, 100, 500, 400);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JComboBox cb = new JComboBox();
		JCheckBox c1 = new JCheckBox("Age");
		JCheckBox c2 = new JCheckBox("Height");
		JCheckBox c3 = new JCheckBox("Sex");

		cb.addItem(c1.getText());
		cb.addItem(c2.getText());
		cb.addItem(c3.getText());

		cb.insertItemAt("School", 0);
		cb.remove(0);
		cb.removeItem("School");
		cb.removeAllItems();

		String str = cb.getSelectedItem().toString();
		int index0 = cb.getSelectedIndex();


		window.add(cb);




		window.setVisible(true);

	}

}
