import java.awt.*;
import javax.swing.*;

public class Main extends JFrame{
    JTextField text;
    JButton button;
    JCheckBox cb1, cb2, cb3;
    JRadioButton rb1, rb2;
    ButtonGroup group;
    JComboBox combobox;
    JTextArea area;

    public Main(){
        init();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void init(){
        setLayout(new FlowLayout());

        add(new JLabel("Text field:"));
        text = new JTextField(10);
        add(text);

        add(new JLabel("Button"));
        button = new JButton("Yes");
        add(button);

        add(new JLabel("Choices:"));
        cb1 = new JCheckBox("Music");
        cb2 = new JCheckBox("Travel");
        cb3 = new JCheckBox("Basketball");
        add(cb1);
        add(cb2);
        add(cb3);

        add(new JLabel("Single Choice:"));
        group = new ButtonGroup();
        rb1 = new JRadioButton("Male");
        rb2 = new JRadioButton("Female");
        group.add(rb1);
        group.add(rb2);
        add(rb1);
        add(rb2);

        add(new JLabel("List:"));
        combobox = new JComboBox();
        combobox.addItem("music");
        combobox.addItem("martial arts");
        combobox.addItem("chess");
        add(combobox);

        add(new JLabel("Text Area:"));
        area = new JTextArea(6, 12);
        add(new JScrollPane(area));
    }

    public static void main(String[] args){
        Main window1 = new Main();
        window1.setBounds(100, 100, 310, 260);
        window1.setTitle("Regular compenent");
    }
}
