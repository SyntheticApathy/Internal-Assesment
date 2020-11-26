package sample;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.logicalmap.*;


public class GameGUI {
    final static int width = 1200;
    final static int height = 800;

    public static void init(int numberOfTrees) {
        Stage stage = new Stage();
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, width, height);

        /* Adding Character */
        // TODO: 11/24/2020 add character
        Rectangle character = new Rectangle(600, 400, 5, 5);
        character.setFill(Color.RED);
        root.getChildren().add(character);


        /* Adding Trees onto GUI from Logical Map */
        LogicalMap lm = new LogicalMapCreator().createLogicalMap(numberOfTrees, width / 5, height / 5);

        Position[][] positions = lm.getPositions();
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                displayPosition(i, j, positions[i][j], root);
            }
        }


        stage.setScene(scene);

        /* General Movement Of Character */ // do I want this? we can make the character a literal
                                            // building or smth and just leave it.
        scene.setOnKeyPressed(e -> {

            /* Movement Up */
            if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
                if (character.getY() - 5 <= 0) {
                    character.setY(height - character.getHeight());  /* spawn character on other side of map,
                                                              needs to have enough space so
                                                              the character doesn't clip out*/
                }

                character.setY(character.getY() - 5);
            }
            /* Movement to the Right */
            if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) {
                if (character.getX() + 5 >= width) {
                    character.setX(5);   /* spawn character on other side of map,
                                       needs to have enough space so
                                       the character doesn't clip out*/
                }
                character.setX(character.getX() + 5);
            }

            /*Movement to the  Left */

            if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) {
                if (character.getX() - 5 <= 0) {
                    character.setX(width - character.getWidth());/* spawn character on other side of map,
                                                          needs to have enough space so
                                                          the character doesn't clip out*/
                }
                character.setX(character.getX() - 5);
            }
            /*Movement Down */
            if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) {
                if (character.getY() + 5 >= height) {
                    character.setY(5);                   /* spawn character on other side of map,
                                                       needs to have enough space so
                                                       the character doesn't clip out*/
                }
                character.setY(character.getY() + 5);
            }
        });
        stage.show();
    }

    private static void displayPosition(int i, int j, Position position, AnchorPane root) {
        if (position.hasObstacle()) {
            Obstacle obstacle = position.getObstacle();
            displayObstacle(i, j, obstacle, root);
        } else {
            //do nothing
        }
    }

    private static void displayObstacle(int i, int j, Obstacle obstacle, AnchorPane root) {
        if (obstacle instanceof Tree) {
            displayTree(i, j, (Tree) obstacle, root);
        } else if (obstacle instanceof Turret) {
            displayTurret(i, j, (Turret) obstacle, root);
        }
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
