package sample.logicalgameplay;

import javafx.util.Pair;
import sample.logicalmap.LogicalMap;
import sample.logicalmap.Position;

import java.util.Set;

public class Game {
    private static int roundNumber = 1;
    private LogicalMap logicalMap;
    private Set<Pair<Integer, Integer>> turretCoordinates;
    private Set<Enemy> enemies;

    public Game(Set<Enemy> enemies, LogicalMap logicalMap, Set<Pair<Integer, Integer>> turretCoordinates, int roundNumber) {


    }

    public static void moveEnemiesByOne(LogicalMap logicalMap) {
        Set<Enemy> enemies = logicalMap.getEnemies();
        for (Enemy enemy : enemies) {
            Position oldPosition = getEnemiesPosition(enemy, logicalMap);
            enemy.moveByOne();
            Position newPosition = getEnemiesPosition(enemy, logicalMap);
            oldPosition.removeEnemy();
            newPosition.setEnemy(enemy);
        }

    }

    private static Position getEnemiesPosition(Enemy enemy, LogicalMap logicalMap) {
        int i = enemy.getCurrentPosition();
        Pair<Integer, Integer> positionPair = enemy.getEnemyPath().get(i);
        return logicalMap.getPositions()[positionPair.getKey()][positionPair.getValue()];
    }

    public static int getRoundNumber() {
        return roundNumber;
    }

    public static void setRoundNumber(int roundNumber) {
        Game.roundNumber = roundNumber;
    }

}
