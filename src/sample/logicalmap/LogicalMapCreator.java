package sample.logicalmap;

import javafx.util.Pair;
import sample.logicalgameplay.Enemy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class LogicalMapCreator {
    public LogicalMap createLogicalMap(int numberOfTrees, int numberOfBoulders, int numberOfEnemies, int width, int height) {
        LogicalMap logicalMap = new LogicalMap(width, height);

        Set<Pair<Integer, Integer>> turretCoordinates = new HashSet<Pair<Integer, Integer>>() {{
            double coefficient = 0.45;
            add(new Pair<>((int) Math.round(coefficient * width) - 1, (int) Math.round(coefficient * height) - 1));
            add(new Pair<>((int) Math.round(coefficient * width) - 1, (int) Math.round((1 - coefficient) * height)));
            add(new Pair<>((int) Math.round((1 - coefficient) * width), (int) Math.round(coefficient * height) - 1));
            add(new Pair<>((int) Math.round((1 - coefficient) * width), (int) Math.round((1 - coefficient) * height)));

        }};
        putTurretsOnLogicalMap(turretCoordinates, logicalMap);

        Set<Pair<Integer, Integer>> illegalLocations = new HashSet<>();
        illegalLocations.addAll(turretCoordinates);
        illegalLocations.add(logicalMap.getMiddle());

        putTreesOnLogicalMap(selectRandomCoordinates(numberOfTrees, width, height), logicalMap, illegalLocations);
        putBouldersOnLogicalMap(selectRandomCoordinates(numberOfBoulders, width, height), logicalMap, illegalLocations);
        putEnemiesOnLogicalMap(generateEnemyStartingCoordinates(numberOfEnemies, width, height), logicalMap, illegalLocations);
        return logicalMap;
    }




    public void putTreesOnLogicalMap(Set<Pair<Integer, Integer>> coordinates, LogicalMap logicalMap, Set<Pair<Integer, Integer>> illegalLocations) {
        Position[][] positions = logicalMap.getPositions();
        for (Pair<Integer, Integer> coordinate : coordinates) {
            if (!illegalLocations.contains(coordinate)) {
                positions[coordinate.getKey()][coordinate.getValue()].setObstacle(new Tree());
            }
        }
    }

    public void putBouldersOnLogicalMap(Set<Pair<Integer, Integer>> coordinates, LogicalMap logicalMap, Set<Pair<Integer, Integer>> illegalLocations) {
        Position[][] positions = logicalMap.getPositions();
        for (Pair<Integer, Integer> coordinate : coordinates) {
            if (!illegalLocations.contains(coordinate)) {
                positions[coordinate.getKey()][coordinate.getValue()].setObstacle(new Boulder());
            }
        }
    }

    public void putTurretsOnLogicalMap(Set<Pair<Integer, Integer>> coordinates, LogicalMap logicalMap) {
        for (Pair<Integer, Integer> coordinate : coordinates) {
            logicalMap.addTurret(coordinate.getKey(), coordinate.getValue());
        }
    }


    public void putEnemiesOnLogicalMap(List<Pair<Integer, Integer>> coordinates, LogicalMap logicalMap, Set<Pair<Integer, Integer>> illegalLocations) {
        Position[][] positions = logicalMap.getPositions();
        for (Pair<Integer, Integer> coordinate : coordinates) {
            if (!illegalLocations.contains(coordinate)) {
                positions[coordinate.getKey()][coordinate.getValue()].setEnemy(new Enemy(logicalMap, coordinate));
            }
        }
    }

    public List<Pair<Integer, Integer>> generateEnemyStartingCoordinates(int number, int width, int height) {
        if (number < 0) throw new IllegalArgumentException("Invalid number:" + number);
        if (width < 1) throw new IllegalArgumentException("Invalid width:" + width);
        if (height < 1) throw new IllegalArgumentException("Invalid height" + height);
        if (number > width + height) throw new IllegalArgumentException("number is too big for size");


        List<Pair<Integer, Integer>> coordinates = new ArrayList<>();
        /* spawn enemy on left of map */
        while (coordinates.size() < number / 4) {
            int x = ThreadLocalRandom.current().nextInt(0, (width / 20));
            int y = ThreadLocalRandom.current().nextInt(0, height);

            coordinates.add(new Pair<>(x, y));

        }
        /* spawn enemy on right  of map */
        while (coordinates.size() >= (number / 4) && coordinates.size() < (number / 2)) {
            int x = ThreadLocalRandom.current().nextInt(width - (width / 20), width);
            int y = ThreadLocalRandom.current().nextInt(0, height);

            coordinates.add(new Pair<>(x, y));
        }
        /* spawn enemy on the top of map */
        while (coordinates.size() >= (number / 2) && coordinates.size() < ((number / 4) * 3)) {
            int x = ThreadLocalRandom.current().nextInt(0, width);
            int y = ThreadLocalRandom.current().nextInt(0, (height / 20));

            coordinates.add(new Pair<>(x, y));
        }
        /*  spawn enemy on the bottom of the map */
        while (coordinates.size() >= ((number / 4) * 3) && coordinates.size() < number) {
            int x = ThreadLocalRandom.current().nextInt(0, width);
            int y = ThreadLocalRandom.current().nextInt(height - (height / 20), height);

            coordinates.add(new Pair<>(x, y));
        }

        return coordinates;
    }

    public Set<Pair<Integer, Integer>> selectRandomCoordinates(int number, int width, int height) {
        /*randomly generate number points with x between 0 and width-1 and y between 0 and height-1 */
        /*the points should not repeat */
        if (number < 0) throw new IllegalArgumentException("Invalid number:" + number);
        if (width < 1) throw new IllegalArgumentException("Invalid width:" + width);
        if (height < 1) throw new IllegalArgumentException("Invalid height" + height);
        if (number > width * height) throw new IllegalArgumentException("number is too big for size");

        Set<Pair<Integer, Integer>> coordinates = new HashSet<>();
        while (coordinates.size() < number) {
            int x = (int) (Math.random() * (width - 1));
            assert x >= 0;
            assert x < width;

            int y = (int) (Math.random() * (height - 1));
            assert y >= 0;
            assert y < height;

            coordinates.add(new Pair<>(x, y));
        }
        assert coordinates.size() == number;
        return coordinates;
    }
}
