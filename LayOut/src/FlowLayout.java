import javax.swing.*;

public class FlowLayout {
    private JFrame window;
    private JButton b1;
    private JButton b2;

    public FlowLayout(String str1, String str2){
        b1 = new JButton(str1);
        b2 = new JButton(str2);
        init();
        window.add(b1);
        window.add(b2);
    }

    public void init(){
        window = new JFrame("Button Example");
        window.setLayout(new java.awt.FlowLayout());
        window.setBounds(100, 100, 500, 400);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] main){
        FlowLayout b1 = new FlowLayout("Press", "Dont's Press");
    }


}
