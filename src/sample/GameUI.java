package sample;

/**
 * This Class handles all ui elements, such as displaying firing lines, turrets, obstacles, enemies and so on.
 */

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.logicalgameplay.Enemy;
import sample.logicalgameplay.FiringLine;
import sample.logicalgameplay.GameLogic;
import sample.logicalmap.*;

import static sample.logicalgameplay.GameLogic.*;


public class GameUI {
    final static int width = 600;
    final static int height = 400;
    static int flag = 0;
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

        /* Adding additional turrets */
        root.setOnMouseClicked(e -> {

            /* translating coordinates into positions */
            int xPosition = (int) e.getSceneX() / 5;
            int yPosition = (int) e.getSceneY() / 5;

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
                        Duration.millis(50),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                GameLogic.deleteDeadEnemies(lm);  // delete enemies killed by turrets from lm

                                // if game is still going on
                                if (!GameLogic.isGameLost(lm) && GameLogic.enemiesOnMap(lm)) {
                                    GameLogic.moveEnemiesByOne(lm);
                                    displayPositionsAndEnemies(root, lm.getPositions()); // redraw the map each time the enemies move

                                    // gameplay mechanics
                                    if (flag % 5 == 0) {
                                        GameLogic.shootEnemy(lm, root);
                                    }
                                    flag++;
                                }

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
                            }
                        }));
        Timeline tl = timelines[0];
        tl.setCycleCount(Animation.INDEFINITE);


        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                tl.play();
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                menuUI();
            }
        });

        stage.show();
    }

    /* Menu for saving/quitting and for info */
    public static void menuUI() {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 600);
        stage.setScene(scene);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            // TODO: 31/03/2021 save game
        });
        Button endGame = new Button("Quit");

        endGame.setOnAction(event -> System.exit(0));

        Text enemiesKilledText = new Text();
        Text roundNumber = new Text();
        Text turretsToBePlaced = new Text();


        root.add(saveButton, 0, 0);
        root.add(endGame, 1, 0);

        root.add(enemiesKilledText, 0, 1);
        root.add(roundNumber, 0, 2);
        root.add(turretsToBePlaced, 0, 3);

        stage.show();

        /* creating a new thread each time the menu is opened, so each menu is always up to date */
        MenuUIUpdaterRunnable updater = new MenuUIUpdaterRunnable(enemiesKilledText, turretsToBePlaced, roundNumber);
        Thread t = new Thread(updater);
        t.start();
        stage.setOnCloseRequest(event -> updater.setShouldRun(false));
    }

    public static void turretPlaced() {
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
        stage.setScene(scene);
        stage.setResizable(false);

        /* calculating the amount of enemies which survived */
        int amountOfEnemiesWhichSurvived = 0;
        for (Enemy enemy : lm.getEnemies()) {
            if (!enemy.isDead()) {
                amountOfEnemiesWhichSurvived++;
            }
        }

        Text text = new Text("you lost.");
        Text enemiesLeftText = new Text("There were still " + amountOfEnemiesWhichSurvived + " enemies left.");
        Text enemiesKilledText = new Text("You defeated " + enemiesKilled + " enemies");

        root.add(text, 0, 0);
        root.add(enemiesLeftText, 0, 1);
        root.add(enemiesKilledText, 0, 2);
        stage.show();
    }


    private static void displayPositionsAndEnemies(AnchorPane root, Position[][] positions) {
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
            test.setFill(Color.WHITESMOKE);
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
