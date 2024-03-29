package sample.database;

/**
 * This method loads the info from the database and turns it into the game
 * Database -> Game
 */

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;
import sample.GameUI;
import sample.logicalgameplay.GameLogic;
import sample.logicalmap.Boulder;
import sample.logicalmap.LogicalMap;
import sample.logicalmap.LogicalMapCreator;
import sample.logicalmap.Tree;

import java.util.Arrays;
import java.util.Set;

import static sample.GameUI.*;

public class GameStateToGame {
    private Set<Pair<Integer, Integer>> treeCoordinates;
    private Set<Pair<Integer, Integer>> boulderCoordinates;
    private Set<Pair<Integer, Integer>> turretCoordinates;
    private Set<Pair<Integer, Integer>> enemyCoordinates;
    private int roundNumber;

    public GameStateToGame(
            int roundNumber,
            Set<Pair<Integer, Integer>> treeCoordinates,
            Set<Pair<Integer, Integer>> boulderCoordinates,
            Set<Pair<Integer, Integer>> turretCoordinates,
            Set<Pair<Integer, Integer>> enemyCoordinates) {
        this.roundNumber = roundNumber;
        this.treeCoordinates = treeCoordinates;
        this.boulderCoordinates = boulderCoordinates;
        this.turretCoordinates = turretCoordinates;
        this.enemyCoordinates = enemyCoordinates;
    }

    public void createGame() {
        LogicalMap lm = new LogicalMapCreator().createLogicalMapFromDatabase(boulderCoordinates, treeCoordinates, enemyCoordinates, turretCoordinates);
        int numberOfTrees = 0;
        int numberOfBoulders = 0;
        for (int i = 0; i < lm.getPositions().length; i++) {
            for (int j = 0; j < lm.getPositions()[i].length; j++) {
                if (lm.getPositions()[i][j].hasObstacle()) {
                    if (lm.getPositions()[i][j].getObstacle() instanceof Tree) {
                        numberOfTrees++;
                    } else if (lm.getPositions()[i][j].getObstacle() instanceof Boulder) {
                        numberOfBoulders++;
                    } else {
                        // do nothing
                    }
                }
            }
        }


        /* creating stage and stuff so that runTurn() can be called */

        Stage stage = new Stage();
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);

        /* Adding Base */
        Rectangle character = new Rectangle(width / 2, height / 2, 5, 5);
        character.setFill(Color.BLUE);
        root.getChildren().add(character);


        displayPositionsAndEnemies(root, lm.getPositions());

        GameLogic.setRoundNumber(roundNumber);

        GameUI.runTurn(lm, stage, numberOfTrees, numberOfBoulders);

    }
}
