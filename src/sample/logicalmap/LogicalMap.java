package sample.logicalmap;

import javafx.util.Pair;
import sample.logicalgameplay.Enemy;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class LogicalMap {
    private int width;
    private int height;
    private Set<Enemy> enemies = new LinkedHashSet<>();


    private Position[][] positions;

    public LogicalMap(int width, int height) {
        this.width = width;
        this.height = height;
        positions = new Position[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                positions[i][j] = new Position();
            }
        }
    }

    public LogicalMap() {

    }



    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position[][] getPositions() {
        return positions;
    }

    public Pair<Integer, Integer> getMiddle() {
        return new Pair<>(width / 2, height / 2);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public Set<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                sb.append(positions[i][j].toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}

