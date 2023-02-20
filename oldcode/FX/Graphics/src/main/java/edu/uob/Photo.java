//package edu.uob;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.image.*;

public class Photo extends Application{
    public void start(Stage stage){
        Image photo = new Image("image1.png");
        ImageView view = new ImageView(photo);
        Group root = new Group(view);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
