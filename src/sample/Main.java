package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

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
            // TODO: 11/26/2020 connect with SQL database and load game
        });

        stage.setScene(new Scene(root, 1200, 800, Color.RED));
        stage.show();
    }

    private void createNewGameMenu() {
        final boolean[] flag = {true};


        Stage stage = new Stage();

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
                /*  Error Message  */
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something went wrong");
                alert.setContentText("Try again, if this error persists, copy the below error and contact our customer service");

                Exception ex = new IllegalArgumentException("Illegal Argument Exception");

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String exceptiontext = sw.toString();

                Label label = new Label("Error:");

                TextArea textArea = new TextArea(exceptiontext);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                alert.getDialogPane().setExpandableContent(expContent);
                alert.showAndWait();

                createNewGameMenu();
                stage.close();
            } else {
                if (Long.parseLong(numberOfTreesTextfield.getText()) >= ((GameGUI.height / 5) * (GameGUI.width / 5)) / 4.5) {
                    flag[0] = !flag[0];
                    Alert tooBigOfANumberAlert = new Alert(Alert.AlertType.ERROR);
                    tooBigOfANumberAlert.setTitle("Number Error");
                    tooBigOfANumberAlert.setHeaderText("The number you entered was too large");
                    tooBigOfANumberAlert.setContentText("We reccomend entering a smaller number :)");

                    tooBigOfANumberAlert.showAndWait();
                    createNewGameMenu();
                    stage.close();
                }
                if (flag[0]) {
                    GameGUI.init(Integer.parseInt(numberOfTreesTextfield.getText()));
                    stage.close();
                }
            }

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


    public static void main(String[] uwu) {
        launch(uwu);
    }
}
