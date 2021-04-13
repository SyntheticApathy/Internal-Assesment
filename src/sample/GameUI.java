package sample;

/**
 * This Class handles all ui elements, such as displaying firing lines, turrets, obstacles, enemies and so on.
 */

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


import sample.database.Database;
import sample.database.GameState;
import sample.database.GameToGameState;
import sample.database.SQLiteDatabase;
import sample.logicalgameplay.Enemy;
import sample.logicalgameplay.FiringLine;
import sample.logicalgameplay.GameLogic;
import sample.logicalmap.*;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;


import static sample.logicalgameplay.GameLogic.*;


public class GameUI {
    /* this is the width and height of the game window */
    public final static int width = 600;
    public final static int height = 400;

    static int flagForTurretShooting = 0;
    public final static int[] amountOfTurretsWhichCanBePlaced = {getRoundNumber()};
    public static int enemiesKilled;

    public static void init(int numberOfTrees, int numberOfBoulders) {
        Stage stage = new Stage();
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setResizable(false);



        /* Adding Base */
        Rectangle character = new Rectangle(width / 2, height / 2, 5, 5);
        character.setFill(Color.BLUE);
        root.getChildren().add(character);


        /* Adding Trees / Boulder / Enemies onto GUI from Logical Map */
        int numberOfEnemies = getRoundNumber() * 10;
        LogicalMap lm = new LogicalMapCreator().createLogicalMap(numberOfTrees, numberOfBoulders, numberOfEnemies, width / 5, height / 5);

        /* Getting Positions */
        Position[][] positions = lm.getPositions();

        /* Setting amount of turrets which can be placed each round */
        amountOfTurretsWhichCanBePlaced[0] = getRoundNumber();

        /* Adding additional turrets by user on Click*/
        root.setOnMouseClicked(e -> {

            /* translating coordinates into positions */
            int xPosition = GameLogic.coordinatesToPosition(e.getSceneX());
            int yPosition = GameLogic.coordinatesToPosition(e.getSceneY());
            GameLogic.turretLogic(xPosition, yPosition, positions, lm, root);

        });


        /* Displaying Positions and Enemies */
        displayPositionsAndEnemies(root, positions);

        /* Commencing GamePlay */
        runTurn(lm, stage, numberOfTrees, numberOfBoulders);
    }


    public static void runTurn(LogicalMap lm, Stage stage, int numberOfTrees, int numberOfBoulders) {
        Scene scene = stage.getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();

        Timeline[] timelines = new Timeline[1]; //object creation hack
        timelines[0] = new Timeline(
                new KeyFrame(
                        Duration.millis(50), event -> {

                    GameLogic.deleteDeadEnemies(lm);  // delete enemies killed by turrets from lm

                    // if game is still going on
                    if (!GameLogic.isGameLost(lm) && GameLogic.enemiesOnMap(lm)) {
                        GameLogic.moveEnemiesByOne(lm);
                        displayPositionsAndEnemies(root, lm.getPositions()); // redraw the map each time the enemies move

                        /* gameplay mechanics start*/
                        if (flagForTurretShooting % 5 == 0) {
                            GameLogic.shootEnemy(lm, root);
                        }
                        flagForTurretShooting++;
                    }
                    /*gameplay mechanics end */

                    // if game is lost
                    if (GameLogic.isGameLost(lm)) {
                        stage.close();
                        timelines[0].stop();
                        gameLostUI(lm);

                    }
                    // if game has been won
                    if (!GameLogic.enemiesOnMap(lm)) {
                        timelines[0].stop();
                        scene.setOnKeyPressed(e -> {
                            if (e.getCode() == KeyCode.ENTER) {
                                stage.close();
                                GameLogic.setRoundNumber(getRoundNumber() + 1);
                                init(numberOfTrees, numberOfBoulders);
                            }
                        });
                    }
                }));
        Timeline tl = timelines[0];
        tl.setCycleCount(Animation.INDEFINITE);


        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                tl.play();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                menuUI(lm);
            }
        });

        stage.show();
    }

    /* Menu for saving/quitting and for info */
    public static void menuUI(LogicalMap lm) {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Menu");

        BackgroundFill bf = new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(bf));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            saveUI(lm);

        });
        Button endGame = new Button("Quit");
        endGame.setOnAction(event -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
            exitAlert.setTitle("Exit Confirmation");
            exitAlert.setContentText("Make sure to save before quitting, if the game is not saved all data will be lost");


            Optional<ButtonType> result = exitAlert.showAndWait();
            if (!result.isPresent()) {
                // alert is exited, do nothing
            } else if (result.get() == ButtonType.OK) {
                System.exit(0);
            } else if (result.get() == ButtonType.CANCEL) {
                exitAlert.close();
            }

        });

        Text enemiesKilledText = new Text();
        Text roundNumber = new Text();
        Text turretsToBePlaced = new Text();


        Text infoText = new Text("Press the space bar in order to start the round. \nAfter all the enemies are defeated, \npress enter to " +
                "go to the next round\n\n\n");

        Text spaceText = new Text("\n\n\n");

        root.add(infoText, 0, 1);


        root.add(enemiesKilledText, 0, 3);
        root.add(roundNumber, 0, 4);
        root.add(turretsToBePlaced, 0, 5);

        root.add(spaceText, 0, 6);

        root.add(saveButton, 0, 7);
        root.add(endGame, 1, 7);

        stage.show();

        /* creating a new thread each time the menu is opened, so each menu is always up to date */
        MenuUIUpdaterRunnable updater = new MenuUIUpdaterRunnable(enemiesKilledText, turretsToBePlaced, roundNumber);
        Thread t = new Thread(updater);
        t.start();

        stage.setOnCloseRequest(event -> {
            updater.setShouldRun(false);
        });

    }

    private static void saveUI(LogicalMap lm) {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root, width, height);
        stage.setTitle("End Screen");
        stage.setScene(scene);
        stage.setResizable(false);

        BackgroundFill bf = new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(bf));


        Text enterUserNameText = new Text("Enter the User Name you want to save this game to: ");
        Text enterSaveNameText = new Text("Enter the Save Name you want to save this game by: ");


        TextField enterUserNameTextField = new TextField();
        TextField enterSaveNameTextField = new TextField();


        Button okButton = new Button("Ok");

        okButton.setOnAction(event -> {
            String userName = null;
            String saveName = null;
            if (enterUserNameTextField.getText().isEmpty() || enterSaveNameTextField.getText().isEmpty()) {
                // alert
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


            } else if (!enterSaveNameTextField.getText().isEmpty() && !enterUserNameTextField.getText().isEmpty()) {
                userName = enterUserNameTextField.getText();
                saveName = enterSaveNameTextField.getText();
            }
            if (userName != null && saveName != null){
            GameState gameState = GameToGameState.transform(lm, getRoundNumber());
            Database db = new SQLiteDatabase();
            db.save(userName, saveName, gameState);
            stage.close();
            }

        });


        root.add(enterUserNameText, 0, 0);
        root.add(enterUserNameTextField, 1, 0);
        root.add(enterSaveNameText, 0, 1);
        root.add(enterSaveNameTextField, 1, 1);
        root.add(okButton, 2, 2);

        stage.show();

    }

    public static void turretPlaced() {
        //for future development
    }

    public static void enemyKilled(Enemy e) {
        if (!e.isDead()) {
            enemiesKilled++;
        }
    }

    /* Screen that appears after the game is lost */
    private static void gameLostUI(LogicalMap lm) {

        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root, width, height);
        stage.setTitle("End Screen");
        stage.setScene(scene);
        stage.setResizable(false);

        BackgroundFill bf = new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(bf));

        /* calculating the amount of enemies which survived */
        int amountOfEnemiesWhichSurvived = 0;
        for (Enemy enemy : lm.getEnemies()) {
            if (!enemy.isDead()) {
                amountOfEnemiesWhichSurvived++;
            }
        }

        Text youLostText = new Text("The Enemy has Reached the Base \n\n");
        youLostText.setFill(Color.BLACK);
        youLostText.setStyle("-fx-font: 30 arial");

        Text enemiesLeftText = new Text("There were still " + amountOfEnemiesWhichSurvived + " enemies left.\n");
        Text enemiesKilledText = new Text("You defeated " + enemiesKilled + " enemies\n\n");

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(event -> System.exit(0));

        Button retryButton = new Button("Try again");
        retryButton.setOnAction(event -> {
            Main.createNewGameMenu();

            stage.close();
        });

        root.add(youLostText, 1, 0);
        root.add(enemiesLeftText, 1, 1);
        root.add(enemiesKilledText, 1, 2);

        root.add(quitButton, 1, 4);
        root.add(retryButton, 1, 5);
        stage.show();
    }


    public static void displayPositionsAndEnemies(AnchorPane root, Position[][] positions) {
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                displayPosition(i, j, positions[i][j], root);
                displayEnemy(i, j, positions[i][j], root);
            }
        }
    }


    public static void displayFiringLine(FiringLine firingLine, AnchorPane root) {
        int logicalStartX = firingLine.getTurretCoordinate().getKey();
        int logicalStartY = firingLine.getTurretCoordinate().getValue();
        int logicalEndX = firingLine.getEnemyCoordinate().getKey();
        int logicalEndY = firingLine.getEnemyCoordinate().getValue();
        int startX = translatePositionCoordinateIntoGUI(logicalStartX);
        int startY = translatePositionCoordinateIntoGUI(logicalStartY);
        int endX = translatePositionCoordinateIntoGUI(logicalEndX);
        int endY = translatePositionCoordinateIntoGUI(logicalEndY);

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.GREEN);
        root.getChildren().add(line);
    }


    private static void displayEnemy(int i, int j, Position position, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        if (position.hasEnemy()) {
            Rectangle enemyRectangle = new Rectangle(x, y, 5, 5);
            enemyRectangle.setFill(Color.RED);
            root.getChildren().add(enemyRectangle);
        } else if (position.hasEnemyShadow()) {
            Rectangle test = new Rectangle(x, y, 5, 5);
            test.setFill(Color.PINK);
            root.getChildren().add(test);
            position.removeEnemyShadow();
        }
    }

    private static void displayPosition(int i, int j, Position position, AnchorPane root) {
        if (position.hasObstacle()) {
            Obstacle obstacle = position.getObstacle();
            displayObstacle(i, j, obstacle, root);
        }

    }

    private static void displayObstacle(int i, int j, Obstacle obstacle, AnchorPane root) {
        if (obstacle instanceof Tree) {
            displayTree(i, j, root);
        } else if (obstacle instanceof Turret) {
            displayTurret(i, j, (Turret) obstacle, root);
        } else if (obstacle instanceof Boulder) {
            displayBoulder(i, j, root);
        }
    }

    private static void displayBoulder(int i, int j, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle boulder = new Rectangle(x, y, 5, 5);
        boulder.setFill(Color.DARKGRAY);
        root.getChildren().add(boulder);
    }

    public static void displayTurret(int i, int j, Turret obstacle, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle turret = new Rectangle(x, y, 5, 5);
        turret.setFill(Color.BLACK);
        root.getChildren().add(turret);
    }

    private static void displayTree(int i, int j, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle tree = new Rectangle(x, y, 5, 5);
        tree.setFill(Color.BURLYWOOD);
        root.getChildren().add(tree);
    }


}
