import javax.swing.*;
import java.awt.*;

public class Card {
    JFrame window;

    public Card(String title){
        window = new JFrame();
        window.setTitle(title);
        CardLayout card = new CardLayout();
        Container c = window.getContentPane();
        window.setLayout(card);
        c.add("1", new JButton("1"));
        c.add("2", new JButton("2"));
        c.add("3", new JButton("3"));
        c.add("4", new JButton("4"));
        c.add("5", new JButton("5"));

        window.setBounds(100, 100, 800, 600);

        c.setBackground(Color.MAGENTA);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public static void main(String[] main){
        //Card c1 = new Card("Hello World");
        JFrame frame = new JFrame("CardLayout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        JPanel buttonPanel = new JPanel();
        JButton nextButton = new JButton("Next");
        buttonPanel.add(nextButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        final JPanel cardPanel = new JPanel();
        final CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        for (int i = 1; i <= 5; i++) {
            JButton card = new JButton("Card " + i);
            card.setPreferredSize(new Dimension(200, 200));
            String cardName = "card" + 1;
            cardPanel.add(card, cardName);
        }
        contentPane.add(cardPanel, BorderLayout.CENTER);
        nextButton.addActionListener(e -> cardLayout.next(cardPanel));

       frame.pack();
        frame.setVisible(true);
    }
}
