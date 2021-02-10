package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


public class GameGUI {
    final static int width = 600;
    final static int height = 400;
    static int flag = 0;

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
        Game.setRoundNumber(10);
        int numberOfEnemies = Game.getRoundNumber() * 10;
        LogicalMap lm = new LogicalMapCreator().createLogicalMap(numberOfTrees, numberOfBoulders, numberOfEnemies, width / 5, height / 5);

        Position[][] positions = lm.getPositions();
        iterate(root, positions);


        scene.setOnKeyPressed(e -> {

            Timeline tl = new Timeline(new KeyFrame(Duration.millis(50), event -> {
                if (!isGameLost(lm)) {
                    Game.moveEnemiesByOne(lm);
                    iterate(root, positions);
                    if (flag % 5 == 0) {
                        shootEnemy(lm, root);
                    }
                    flag++;
                }
            }));
            tl.setCycleCount(Animation.INDEFINITE);
            if (e.getCode() == KeyCode.SPACE) {
                tl.play();
            }
            if (isGameLost(lm)) {
                gameLostUI();
                stage.close();
            }
        });


        stage.show();

    }

    private static void gameLostUI() {

        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setResizable(false);

        Text text = new Text("test lmao");

        root.add(text, 0, 0);

        stage.show();
    }

    private static boolean isGameLost(LogicalMap lm) {
        Set<Enemy> enemies = lm.getEnemies();
        for (Enemy enemy : enemies) {
            if (enemy.getCurrentPosition() == enemy.getEnemyPath().size() - 1) {
                return true;
            }
        }

        return false;
    }

    private static void iterate(AnchorPane root, Position[][] positions) {
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
