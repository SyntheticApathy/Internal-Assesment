package sample.logicalmap;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LogicalMapCreatorTest {

    @Test
    void putTreesOnLogicalMap() {

    }

    @Test
    void selectRandomCoordinatesWithBadInput() {
        LogicalMapCreator logicalMapCreator = new LogicalMapCreator();
        assertThrows(IllegalArgumentException.class, () -> {
            logicalMapCreator.selectRandomCoordinates(-1, 1, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            logicalMapCreator.selectRandomCoordinates(0, 0, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            logicalMapCreator.selectRandomCoordinates(0, 1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            logicalMapCreator.selectRandomCoordinates(2, 1, 1);
        });
    }

    @Test
    void selectRandomCoordinates() {
        LogicalMapCreator logicalMapCreator = new LogicalMapCreator();
        int number = 5;
        int width = 2;
        int height = 10;
        Set<Pair<Integer, Integer>> pairs = logicalMapCreator.selectRandomCoordinates(number, width, height);
        assertEquals(number, pairs.size());
        for (Pair<Integer, Integer> pair : pairs) {
            int x = pair.getKey();
            int y = pair.getValue();
            assertTrue(x < width);
            assertTrue(x >= 0);
            assertTrue(y < height);
            assertTrue(y >= 0);
        }
    }
}