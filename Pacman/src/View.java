
/*
public class View {
    public static void display(Field field){
        for (int j = 0; j < field.row; j++){
            for (int i = 0; i < field.col; i++){
                field.board[j][i].draw();
            }
            System.out.println();
        }
        System.out.println("numOfFoods:" + field.numFoods);
        System.out.println("Points:" + field.points);

        System.out.println("-----------------Separate Line------------------");
    }
}
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class View extends JPanel {
    private static final long serialVersionUID = -2417015700213488315L;
    private static final int GRID_SIZE = 25;

    private Field theField;

    public View(Field theField){
        this.theField = theField;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.GRAY);
        // 分别对行和列遍历，画出网格线
        for (int row = 0; row < theField.row; row++) {
            g.drawLine(0, row * GRID_SIZE, theField.col * GRID_SIZE, row * GRID_SIZE);
        }
        for (int col = 0; col < theField.col; col++) {
            g.drawLine(col * GRID_SIZE, 0, col * GRID_SIZE, theField.row * GRID_SIZE);
        }
        // 遍历所有Cell，让它们将自己展示出来
        for (int row = 0; row < theField.row; row++) {
            for (int col = 0; col < theField.row; col++) {
                Cell cell = theField.board[row][col];
                if (cell != null) {
                    cell.draw(g, col * GRID_SIZE, row * GRID_SIZE, GRID_SIZE);
                }
            }
        }
    }
    /**
     * 设置窗口在屏幕的默认位置
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(theField.col * GRID_SIZE + 1, theField.row * GRID_SIZE + 1);
    }
}


