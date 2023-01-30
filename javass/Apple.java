

import java.awt.*;
import javax.swing.*; 

public class Apple {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> 
			{
				var frame = new SimpleFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			});
			
	}
}

class SimpleFrame extends JFrame{
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 200;
	
	public SimpleFrame(){
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidh = screenSize.width;
		int screenHeight = screenSize.height;
		setSize(screenWidh / 2, screenHeight / 2);
	}
}
