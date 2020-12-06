package sample.logicalgameplay;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import sample.logicalmap.LogicalMap;
import sample.logicalmap.LogicalMapCreator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    private LogicalMap testMapEmpty5x5 = new LogicalMapCreator().createLogicalMap(0, 0,0, 5,5);

    @Test
    void testSimple3x3Path() {
        Pair<Integer, Integer> initialPosition = new Pair<>(0, 0);
        Enemy enemy = new Enemy(testMapEmpty5x5, initialPosition);
        List<Pair<Integer, Integer>> enemyPath = enemy.getEnemyPath();
        assertEquals(5, enemyPath.size());
        assertEquals(initialPosition, enemyPath.get(0));

        Pair<Integer, Integer> targetPosition = new Pair<>(2,2);
        assertEquals(targetPosition, enemyPath.get(4));
    }

    @Test
    void chooseUnsettledNodeWithLowestDistance() {
    }
}