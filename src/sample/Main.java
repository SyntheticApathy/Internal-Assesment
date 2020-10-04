package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        primaryStage.show();

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(25), e -> {
            print(primaryStage);
        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

    }

    private void print(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        primaryStage.setScene(new Scene(root,550,550));

        Rectangle rectangle = new Rectangle(200,50,5,5);
        rectangle.setFill(Color.BLACK);
        root.getChildren().addAll(rectangle);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
