package sample;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.logicalgameplay.Game;
import sample.logicalmap.*;


public class GameGUI {
    final static int width = 600;
    final static int height = 400;

    public static void init(int numberOfTrees, int numberOfBoulders) {
        Stage stage = new Stage();
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);




        /* Adding Character */
        // TODO: 11/24/2020 add character
        Rectangle character = new Rectangle(width / 2, height / 2, 5, 5);
        character.setFill(Color.BLUE);
        root.getChildren().add(character);


        /* Adding Trees / Boulder / Enemies onto GUI from Logical Map */
        int numberOfEnemies = 100; // TODO: 12/6/2020 change this so that it depends on what round it is in, this is just a quick switch
        LogicalMap lm = new LogicalMapCreator().createLogicalMap(numberOfTrees, numberOfBoulders, numberOfEnemies, width / 5, height / 5);

        Position[][] positions = lm.getPositions();
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                displayPosition(i, j, positions[i][j], root);
                displayEnemy(i, j, positions[i][j], root);
            }
        }

//
//        /* General Movement Of Character */
//        scene.setOnKeyPressed(e -> {
//
//            /* Movement Up */
//            if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
//                if (character.getY() - 5 <= 0) {
//                    character.setY(height - character.getHeight());  /* spawn character on other side of map,
//                                                              needs to have enough space so
//                                                              the character doesn't clip out*/
//                }
//
//                character.setY(character.getY() - 5);
//            }
//            /* Movement to the Right */
//            if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) {
//                if (character.getX() + 5 >= width) {
//                    character.setX(5);   /* spawn character on other side of map,
//                                       needs to have enough space so
//                                       the character doesn't clip out*/
//                }
//                character.setX(character.getX() + 5);
//            }
//
//            /*Movement to the  Left */
//
//            if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) {
//                if (character.getX() - 5 <= 0) {
//                    character.setX(width - character.getWidth());/* spawn character on other side of map,
//                                                          needs to have enough space so
//                                                          the character doesn't clip out*/
//                }
//                character.setX(character.getX() - 5);
//            }
//            /*Movement Down */
//            if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) {
//                if (character.getY() + 5 >= height) {
//                    character.setY(5);                   /* spawn character on other side of map,
//                                                       needs to have enough space so
//                                                       the character doesn't clip out*/
//                }
//                character.setY(character.getY() + 5);
//            }
//        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                Game.moveEnemiesByOne(lm);
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
