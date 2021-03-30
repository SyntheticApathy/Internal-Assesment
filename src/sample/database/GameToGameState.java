package sample.database;

import javafx.util.Pair;
import sample.logicalgameplay.Enemy;
import sample.logicalmap.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class GameToGameState {
    public static GameState transform(LogicalMap lm, int roundNumber) {
        Set<Pair<Integer, Integer>> enemyCoordinates = new LinkedHashSet<>();

        Set<Enemy> enemies = lm.getEnemies();
        for (Enemy enemy : enemies) {
            enemyCoordinates.add(enemy.getCurrentCoordinate());
        }

        Set<Pair<Integer, Integer>> treeCoordinates = new LinkedHashSet<>();
        Set<Pair<Integer, Integer>> boulderCoordinates = new LinkedHashSet<>();
        Set<Pair<Integer, Integer>> turretCoordinates = new LinkedHashSet<>();


        Position[][] positions = lm.getPositions();
        for (int x = 0; x < positions.length; x++) {
            for (int y = 0; y < positions[x].length; y++) {
                Position position = positions[x][y];
                if (position.hasObstacle()) {
                    Obstacle obstacle = position.getObstacle();
                    if (obstacle instanceof Tree) {
                        treeCoordinates.add(new Pair<>(x, y));
                    } else if (obstacle instanceof Boulder) {
                        boulderCoordinates.add(new Pair<>(x, y));
                    } else if (obstacle instanceof Turret) {
                        turretCoordinates.add(new Pair<>(x, y));
                    } else {
                        throw new IllegalStateException("Unexpected obstacle type: " + obstacle);
                    }
                }
            }
        }
        return new GameState(roundNumber, enemyCoordinates, treeCoordinates, boulderCoordinates, turretCoordinates);
    }
}
