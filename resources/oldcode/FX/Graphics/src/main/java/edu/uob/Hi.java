import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
public class Hi extends Application {

//    Button button;
    public void start(Stage stage) {

        Label hello = new Label("Hello JavaFX world!\nFirst Try.");
        Group root = new Group(hello);
//        root.getChildren().add(hello);
        Scene scene = new Scene(root);

//        Scene scene = new Scene(layout, 300, 250);
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();



    }
}
