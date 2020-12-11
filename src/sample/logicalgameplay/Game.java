package sample.logicalgameplay;

import javafx.util.Pair;
import sample.logicalmap.LogicalMap;
import sample.logicalmap.Position;

import java.util.Set;

public class Game {
    private LogicalMap logicalMap;
    private Set<Pair<Integer, Integer>> turretCoordinates;
    private Set<Enemy> enemies;

    public Game(Set<Enemy> enemies, LogicalMap logicalMap, Set<Pair<Integer, Integer>> turretCoordinates) {


    }

    public static void moveEnemiesByOne(LogicalMap logicalMap) {
        Set<Enemy> enemies = logicalMap.getEnemies();
        for (Enemy enemy : enemies) {
            Position oldPosition = getEnemiesPosition(enemy, logicalMap);
            enemy.moveByOne();
            // TODO: 12/11/2020 fix this lol
//            oldPosition.getEnemy().moveByOne();

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

}
