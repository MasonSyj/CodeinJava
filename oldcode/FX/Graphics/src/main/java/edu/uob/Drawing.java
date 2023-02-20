/* Display a drawing of a seesaw. */

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;

public class Drawing extends Application {
    public void start(Stage stage) {
        Canvas canvas = new Canvas(400, 300);
        Group root = new Group(canvas);
        stage.setScene(new Scene(root));
        GraphicsContext g = canvas.getGraphicsContext2D();
        draw(g);
        stage.show();
    }

    private void draw(GraphicsContext g) {
        g.fillRect(50, 100, 300, 25);
        g.fillOval(175, 125, 50, 50);
    }
}