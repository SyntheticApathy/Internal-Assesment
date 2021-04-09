package sample;

import sample.logicalmap.LogicalMap;
import sample.logicalmap.LogicalMapCreator;

public class TextMain {
    public static void main(String[] args) {
        int width = 10;
        int height = 5;
        int numberOfTrees = 11;
        int numberOfBoulders = 2;
        int numberOfEnemies = 3;
        LogicalMap lm = new LogicalMapCreator().createLogicalMap(numberOfTrees, numberOfBoulders,numberOfEnemies, width, height);

    }
}
