package sample.logicalgameplay;

import javafx.util.Pair;

public class FiringLine {
    Pair<Integer, Integer> turretCoordinate;
    Pair<Integer, Integer> enemyCoordinate;

    public FiringLine(Pair<Integer, Integer> turretCoordinate, Pair<Integer, Integer> enemyCoordinate) {
        this.turretCoordinate = turretCoordinate;
        this.enemyCoordinate = enemyCoordinate;
    }

    public Pair<Integer, Integer> getTurretCoordinate() {
        return turretCoordinate;
    }

    public Pair<Integer, Integer> getEnemyCoordinate() {
        return enemyCoordinate;
    }

}
