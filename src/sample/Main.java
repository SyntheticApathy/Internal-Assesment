package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        homeScreen(primaryStage);


    }

    private void homeScreen(Stage stage) {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        root.setHgap(20);

        Button button1 = new Button("Create new Map");
        button1.setOnAction(event -> {
            System.out.println("test");
            createNewGameMenu();
            stage.close();
        });
        root.add(button1, 1, 1);


        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    private void createNewGameMenu() {
        Stage stage = new Stage();

        /* TODO: 10/22/2020  create sliders and general game creation gui */

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        root.setHgap(20);

        Text text = new Text("Placeholder");
        root.add(text, 0, 0);

        Button button = new Button("Placerholder");
        button.setOnAction(event -> {
            System.out.println("test1");
            GameGUI.init(); /* TODO: 10/22/2020 into constructor put everything that comes from the sliders */
            stage.close();
        });

        root.add(button, 1, 1);

        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
