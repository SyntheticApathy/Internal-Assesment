package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        homeScreen(primaryStage);


    }

    private void homeScreen(Stage stage) {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(100);
        root.setHgap(20);
        stage.setTitle("Game-io");

        BackgroundFill bf = new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(bf));

        Text welcomeText = new Text("WELCOME!");
        welcomeText.setFill(Color.BLACK);
        welcomeText.setStyle("-fx-font: 30 arial");


        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> {
            createNewGameMenu();
            stage.close();
        });

        Button loadGameButton = new Button("Load Game");
        loadGameButton.setOnAction(e -> {
            // TODO: 11/26/2020 connect with SQL database and load game
        });

        root.add(welcomeText, 2, 0);
        root.add(newGameButton, 1, 1);
        root.add(loadGameButton, 3, 1);

        stage.setScene(new Scene(root, 1200, 800, Color.RED));
        stage.show();
    }

    static void createNewGameMenu() {


        Stage stage = new Stage();
        stage.setTitle("Create new Game");

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        root.setHgap(20);

        BackgroundFill bf = new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(bf));

        Text numberOfTreesText = new Text("Amount of Trees To Be On Map : ");
        TextField numberOfTreesTextfield = new TextField("Enter Number Here");


        Text numberOfBouldersText = new Text("Amount of Boulders To Be On Map : ");
        TextField numberOfBouldersTextfield = new TextField("Enter Number Here");
        Button randomBouldersButton = new Button("Random");

        Button startButton = new Button("Create new World");
        startButton.setOnAction(event -> {

            /* check if Number of Trees && Number of Boulders is valid */

            if (numberOfTreesTextfield.getText().isEmpty() || stringIsInt(numberOfTreesTextfield.getText()) || numberOfBouldersTextfield.getText().isEmpty() || stringIsInt(numberOfBouldersTextfield.getText())) {
                /*  Error Message  */
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something went wrong");
                alert.setContentText("Try again, if this error persists, copy the below error and contact our customer service");

                Exception ex = new IllegalArgumentException("Illegal Argument Exception"); /* This is shown to the client */

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

                GridPane gridPane = new GridPane();
                gridPane.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(label, 0, 0);
                gridPane.add(textArea, 0, 1);

                alert.getDialogPane().setExpandableContent(gridPane);
                alert.showAndWait();

                numberOfBouldersTextfield.setText("");
                numberOfTreesTextfield.setText("");
            } else {
                if (Long.parseLong(numberOfTreesTextfield.getText()) + Long.parseLong(numberOfBouldersTextfield.getText()) >= ((GameUI.height / 5) * (GameUI.width / 5)) / 4.5) {
                    Alert tooBigOfANumberAlert = new Alert(Alert.AlertType.ERROR);
                    tooBigOfANumberAlert.setTitle("Number Error");
                    tooBigOfANumberAlert.setHeaderText("The number(s) you entered was too large");
                    tooBigOfANumberAlert.setContentText("We recommend entering a smaller number(s)");
                    tooBigOfANumberAlert.showAndWait();

                    numberOfBouldersTextfield.setText("");
                    numberOfTreesTextfield.setText("");
                } else {
                    GameUI.init(Integer.parseInt(numberOfTreesTextfield.getText()), Integer.parseInt(numberOfBouldersTextfield.getText()));
                    GameUI.menuUI();
                    stage.close();
                }
            }

        });

        randomBouldersButton.setOnAction(event -> {
            int[] temp = randomNumbers();
            numberOfTreesTextfield.setText((String.valueOf(temp[0])));
            numberOfBouldersTextfield.setText(String.valueOf(temp[1]));
        });


        root.add(numberOfTreesText, 1, 1);
        root.add(numberOfTreesTextfield, 2, 1);

        root.add(numberOfBouldersText, 1, 2);
        root.add(numberOfBouldersTextfield, 2, 2);
        root.add(randomBouldersButton, 3, 3);

        root.add(startButton, 5, 5);
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    private static int[] randomNumbers() {

        int max = (int) (((GameUI.height / 5) * (GameUI.width / 5)) / 4.5);
        int x = ThreadLocalRandom.current().nextInt(0, max);
        int y = ThreadLocalRandom.current().nextInt(0, max);

        return x + y <= max ? new int[]{x, y} : randomNumbers();

    }


    private static boolean stringIsInt(String str) {
        String regex = "^\\d+$";
        return !str.matches(regex);
    }


    public static void main(String[] uwu) {
        launch(uwu);
    }


}
