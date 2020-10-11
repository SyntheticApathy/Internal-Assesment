package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    static final int WIDTH = 550;
    static final int HEIGHT = 600;
    static OverWorld world = new OverWorld();
    static Player player = new Player(world.player.xCoordinate, world.player.yCoordinate);

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.show();

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(25), e -> {
            print(primaryStage, world);



        }));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();

    }

    private void print(Stage primaryStage, OverWorld world) {
        AnchorPane root = new AnchorPane();
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));


        Rectangle rectangle = new Rectangle(player.xCoordinate, player.yCoordinate, 5, 5);
        rectangle.setFill(player.color);


        root.getChildren().addAll(rectangle);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
