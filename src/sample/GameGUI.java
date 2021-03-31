package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import sample.logicalgameplay.Game;
import sample.logicalgameplay.TurretShooting;
import sample.logicalmap.*;

import java.util.Set;

import static sample.logicalgameplay.Game.getRoundNumber;
import static sample.logicalgameplay.Game.setRoundNumber;


public class GameGUI {
    final static int width = 600;
    final static int height = 400;
    static int flag = 0;
    final static int[] amountOfTurretsWhichCanBePlaced = {getRoundNumber()};
    private static LogicalMap currentLogicalMap;
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
        currentLogicalMap = lm;

        /* Getting Positions */
        Position[][] positions = lm.getPositions();

        amountOfTurretsWhichCanBePlaced[0] = getRoundNumber();

        /* Adding additional turrets */
        root.setOnMouseClicked(e -> {

            /* translating coordinates into positions */
            int xPosition = (int) e.getSceneX() / 5;
            int yPosition = (int) e.getSceneY() / 5;

            turretLogic(xPosition, yPosition, positions, lm, root);

        });


        /* Displaying Positions and Enemies */
        displayPositionsAndEnemies(root, positions);

        /* Commencing GamePlay */
        runTurn(lm, stage, numberOfTrees, numberOfBoulders);
    }

    private static void turretLogic(int xPosition, int yPosition, Position[][] positions, LogicalMap lm, AnchorPane root) {
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                if (!positions[xPosition][yPosition].hasObstacle() && GameGUI.amountOfTurretsWhichCanBePlaced[0] > 0) {
                    lm.addTurret(xPosition, yPosition);
                    displayTurret(xPosition, yPosition, new Turret(), root);
                    GameGUI.amountOfTurretsWhichCanBePlaced[0]--;
                    GameGUI.turretPlaced();
                }
            }
        }
    }

    public static void runTurn(LogicalMap lm, Stage stage, int numberOfTrees, int numberOfBoulders) {
        Scene scene = stage.getScene();
        AnchorPane root = (AnchorPane) scene.getRoot();

        Timeline[] timelines = new Timeline[1]; //object creation hack
        timelines[0] = new Timeline(
                new KeyFrame(
                        Duration.millis(50),
                        event -> {

                            deleteDeadEnemies(lm);  // delete enemies killed by turrets from lm

                            // if game is still going on
                            if (!isGameLost(lm) && enemiesOnMap(lm)) {
                                Game.moveEnemiesByOne(lm);
                                displayPositionsAndEnemies(root, lm.getPositions()); // redraw the map each time the enemies move

                                // gameplay mechanics
                                if (flag % 5 == 0) {
                                    shootEnemy(lm, root);
                                }
                                flag++;
                            }

                            // if game is lost
                            if (isGameLost(lm)) {
                                stage.close();
                                timelines[0].stop();
                                gameLostUI(lm);

                            }
                            // if game has been won
                            if (!enemiesOnMap(lm)) {
                                timelines[0].stop();
                                scene.setOnKeyPressed(e -> {
                                    if (e.getCode() == KeyCode.ENTER) {
                                        stage.close();
                                        setRoundNumber(getRoundNumber() + 1);
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

    private static void menuUI(LogicalMap lm) {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 600);
        stage.setScene(scene);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            //save game
        });
        Button endGame = new Button("Quit");
        endGame.setOnAction(event -> {
            System.exit(0);
        });

        Text enemiesKilledText = new Text();
        Text roundNumber = new Text();
        Text turretsToBePlaced = new Text();


        root.add(saveButton, 0, 0);
        root.add(endGame, 1, 0);

        root.add(enemiesKilledText, 0, 1);
        root.add(roundNumber, 0, 2);
        root.add(turretsToBePlaced, 0, 3);

        stage.show();


        MenuUIUpdaterRunnable updater = new MenuUIUpdaterRunnable(enemiesKilledText, turretsToBePlaced, roundNumber);
        Thread t = new Thread(updater);
        t.start();
        stage.setOnCloseRequest(event -> {
            updater.setShouldRun(false);
        });
    }

    public static void turretPlaced() {

    }

    public static void enemyKilled(Enemy e) {
        if (!e.isDead()) {
            enemiesKilled++;
        }
    }


    private static void deleteDeadEnemies(LogicalMap lm) {
        Set<Enemy> enemies = lm.getEnemies();
        enemies.removeIf(Enemy::isDead);
    }

    private static boolean enemiesOnMap(LogicalMap lm) {
        Set<Enemy> enemies = lm.getEnemies();
        return !enemies.isEmpty();
    }

    private static void gameLostUI(LogicalMap lm) {

        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setResizable(false);

        Text text = new Text("you lost.");
        Text enemiesLeftText = new Text("There were still " + lm.getEnemies().size() + " enemies left.");
        Text enemiesKilledText = new Text("You defeated " + enemiesKilled + " enemies");

        root.add(text, 0, 0);
        root.add(enemiesLeftText, 0, 1);
        root.add(enemiesKilledText, 0, 2);
        stage.show();
    }

    private static boolean isGameLost(LogicalMap lm) {
        Set<Enemy> enemies = lm.getEnemies();
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.getCurrentPosition() == enemy.getEnemyPath().size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void displayPositionsAndEnemies(AnchorPane root, Position[][] positions) {
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                displayPosition(i, j, positions[i][j], root);
                displayEnemy(i, j, positions[i][j], root);
            }
        }
    }


    private static void shootEnemy(LogicalMap lm, AnchorPane root) {
        Set<FiringLine> firingLines = new TurretShooting(lm).fireTurrets();
        for (FiringLine firingLine : firingLines) {
            displayFiringLine(firingLine, root);
        }
    }

    private static void displayFiringLine(FiringLine firingLine, AnchorPane root) {
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
            displayTree(i, j, (Tree) obstacle, root);
        } else if (obstacle instanceof Turret) {
            displayTurret(i, j, (Turret) obstacle, root);
        } else if (obstacle instanceof Boulder) {
            displayBoulder(i, j, (Boulder) obstacle, root);
        }
    }

    private static void displayBoulder(int i, int j, Boulder obstacle, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle boulder = new Rectangle(x, y, 5, 5);
        boulder.setFill(Color.DARKGRAY);
        root.getChildren().add(boulder);
    }

    private static void displayTurret(int i, int j, Turret obstacle, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle turret = new Rectangle(x, y, 5, 5);
        turret.setFill(Color.BLACK);
        root.getChildren().add(turret);
    }

    private static void displayTree(int i, int j, Tree obstacle, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle tree = new Rectangle(x, y, 5, 5);
        tree.setFill(Color.BURLYWOOD);
        root.getChildren().add(tree);
    }

    private static int translatePositionCoordinateIntoGUI(int i) {
        return i * 5;
    }


}
