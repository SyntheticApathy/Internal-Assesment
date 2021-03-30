package sample.database;

import javafx.util.Pair;

import java.util.Set;

public class GameState {
    private int roundNumber;
    private Set<Pair<Integer, Integer>> enemies;
    private Set<Pair<Integer, Integer>> trees;
    private Set<Pair<Integer, Integer>> boulders;
    private Set<Pair<Integer, Integer>> turrets;

    public GameState(int roundNumber,
                     Set<Pair<Integer, Integer>> enemies,
                     Set<Pair<Integer, Integer>> trees,
                     Set<Pair<Integer, Integer>> boulders,
                     Set<Pair<Integer, Integer>> turrets) {
        this.roundNumber = roundNumber;
        this.enemies = enemies;
        this.trees = trees;
        this.boulders = boulders;
        this.turrets = turrets;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Set<Pair<Integer, Integer>> getEnemies() {
        return enemies;
    }

    public Set<Pair<Integer, Integer>> getTrees() {
        return trees;
    }

    public Set<Pair<Integer, Integer>> getBoulders() {
        return boulders;
    }

    public Set<Pair<Integer, Integer>> getTurrets() {
        return turrets;
    }
}
