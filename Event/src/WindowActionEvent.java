import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowActionEvent extends JFrame {
    JTextField text;
    ActionListener listener;

    public WindowActionEvent(){
        setLayout(new FlowLayout());
        text = new JTextField(20);
        add(text);
        listener = new ReaderListen();
        text.addActionListener(listener);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Hello World!");
        setBounds(100, 100, 400, 200);
        setVisible(true);
    }

    public static void main(String[] args){
        WindowActionEvent wae = new WindowActionEvent();
    }
}
