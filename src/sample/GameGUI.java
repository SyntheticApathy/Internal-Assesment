package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
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
import sample.logicalgameplay.FiringLine;
import sample.logicalgameplay.Game;
import sample.logicalgameplay.TurretShooting;
import sample.logicalmap.*;

import java.util.Set;


public class GameGUI {
    final static int width = 600;
    final static int height = 400;
    static int numberOfEnemies;
    final static int[] waveNumber = {1};
    static LogicalMap lm = new LogicalMap();

    public static void init(int numberOfTrees, int numberOfBoulders) {
        Stage stage = new Stage();
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Game lol");




        /* Adding Character */
        // TODO: 11/24/2020 add character jpg
        Rectangle character = new Rectangle(width / 2, height / 2, 5, 5);
        character.setFill(Color.BLUE);
        root.getChildren().add(character);


        /* Adding Trees / Boulder / Enemies onto GUI from Logical Map */
        numberOfEnemies = 25 + (2 ^ (waveNumber[0] - 1));
         lm = new LogicalMapCreator().createLogicalMap(numberOfTrees, numberOfBoulders, numberOfEnemies, width / 5, height / 5);

        Position[][] positions = lm.getPositions();
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                displayPosition(i, j, positions[i][j], root);
                displayEnemy(i, j, positions[i][j], root);
            }
        }


        scene.setOnKeyPressed(e -> {
            Timeline tl = new Timeline(new KeyFrame(Duration.millis(50), event -> {
                Game.moveEnemiesByOne(lm);
                shootEnemy(lm, root);
                for (int i = 0; i < positions.length; i++) {
                    for (int j = 0; j < positions[i].length; j++) {
                        displayPosition(i, j, positions[i][j], root);
                        displayEnemy(i, j, positions[i][j], root);
                    }
                }
            }));
            tl.setCycleCount(Animation.INDEFINITE);
            if (e.getCode() == KeyCode.SPACE) {
                tl.play();
            }

            // this is just testing and debugging, delete this when pushing to main
            if (e.getCode() == KeyCode.ENTER) {
//                System.out.println(lm.getEnemies().toString());
                Game.moveEnemiesByOne(lm);
                shootEnemy(lm, root);
                for (int i = 0; i < positions.length; i++) {
                    for (int j = 0; j < positions[i].length; j++) {
                        displayPosition(i, j, positions[i][j], root);
                        displayEnemy(i, j, positions[i][j], root);
                    }
                }

            }
        });


        stage.show();

    }

    public static void sideWindow() {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        root.setHgap(20);
        Scene scene = new Scene(root, 125, 50);
        stage.setAlwaysOnTop(true);
        stage.setResizable(true);
        stage.setTitle("Side window");


        Text text = new Text(String.valueOf(waveNumber[0]));

        Button button = new Button();
        button.setOnAction(event -> {
            //next turn

            // TODO: 12/25/2020 increment amount of enemies, spawn new enemies and delete old enemies. 
            
            text.setText(String.valueOf(waveNumber[0]++));
        });
        button.setText("Next Wave");


        root.add(text, 0, 0);
        root.add(button, 0, 1);

        stage.setScene(scene);
        stage.show();


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
        } else if (position.hasEnemyShadow() || position.hasDeadEnemy()) {
            Rectangle removeShadowRectangle = new Rectangle(x, y, 5, 5);
            removeShadowRectangle.setFill(Color.WHITESMOKE);
            root.getChildren().add(removeShadowRectangle);
            position.removeEnemyShadow();
            if (position.hasEnemy()) {
                if (position.getEnemy().isDead()) {
                    System.out.println("THE ENEMY IS DEAD");
                    Rectangle test = new Rectangle(x, y, 5, 5);
                    test.setFill(Color.WHITESMOKE);
                    root.getChildren().add(test);
                    position.removeEnemy();
                }
            }
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
        Rectangle boulder = new Rectangle(x, y, 5, 5);  // TODO: 11/26/2020  this needs to be the boulder (pic)
        boulder.setFill(Color.DARKGRAY);
        root.getChildren().add(boulder);
    }

    private static void displayTurret(int i, int j, Turret obstacle, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle turret = new Rectangle(x, y, 5, 5);  // TODO: 11/26/2020  this needs to be the turret (pic)
        turret.setFill(Color.BLACK);
        root.getChildren().add(turret);
    }

    private static void displayTree(int i, int j, Tree obstacle, AnchorPane root) {
        int x = translatePositionCoordinateIntoGUI(i);
        int y = translatePositionCoordinateIntoGUI(j);
        Rectangle tree = new Rectangle(x, y, 5, 5);  // TODO: 11/26/2020 this needs to be a tree (pic)
        tree.setFill(Color.BURLYWOOD);
        root.getChildren().add(tree);
    }

    private static int translatePositionCoordinateIntoGUI(int i) {
        return i * 5;
    }
}
