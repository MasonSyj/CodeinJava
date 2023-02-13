import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Swi extends JFrame implements ActionListener {
    JTextField input;
    JTextField output;
    public Swi(String str){
        super(str);
        super.setLayout(new FlowLayout());
        input = new JTextField(10);
        output = new JTextField(10);
        output.setEditable(false);
        add(input);
        add(output);

        input.addActionListener(this);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        setBounds(100, 100, 150, 150);
        super.setVisible(true);
        super.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == input){
            int n = 0, m = 0;
            n = Integer.parseInt(input.getText());
            m = n * n;
            output.setText(n + "'s pow of 2 is: " + m);
        }
    }
}
