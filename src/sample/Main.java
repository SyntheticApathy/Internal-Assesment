package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

        Button button1 = new Button("New Game");
        button1.setOnAction(event -> {
            createNewGameMenu();
            stage.close();
        });
        root.add(button1, 1, 1);
        Button loadGameButton = new Button("Load Game");
        loadGameButton.setOnAction(e -> {
            // TODO: 11/26/2020 connect with SQL database
        });

        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    private void createNewGameMenu() {

        Stage stage = new Stage();

        /* TODO: 10/22/2020  create sliderPlaceholders and general game creation gui */

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        root.setHgap(20);

        Text numberOfTreesText = new Text("Amount of Trees To Be On Map : ");
        TextField numberOfTreesTextfield = new TextField("Enter Number Here");


        Button button = new Button("Create new World");
        button.setOnAction(event -> {

            /* checking if Number of Trees is valid */
            if (numberOfTreesTextfield.getText().isEmpty() || !stringIsInt(numberOfTreesTextfield.getText())) {
                throw new IllegalArgumentException();
            }
            int numberOfTrees = Integer.parseInt(numberOfTreesTextfield.getText());
            ;

            stage.close();
            GameGUI.init(numberOfTrees);

        });


        root.add(numberOfTreesText, 1, 1);
        root.add(numberOfTreesTextfield, 2, 1);


        root.add(button, 5, 5);
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    private boolean stringIsInt(String str) {
        String regex = "^\\d+$";
        return str.matches(regex);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
