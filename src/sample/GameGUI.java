package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class GameGUI {


    public static void init() {
        Stage stage = new Stage();

        AnchorPane root = new AnchorPane();

        Button button = new Button("Placeholder");
        button.setOnAction(event -> {
            System.out.println("test2");

        });

        root.getChildren().add(button);

        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }
}
