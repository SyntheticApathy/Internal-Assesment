package sample.logicalgameplay;

import javafx.util.Pair;
import sample.logicalmap.LogicalMap;
import sample.logicalmap.Position;
import sample.logicalmap.Turret;

import java.util.LinkedHashSet;
import java.util.Set;

public class TurretShooting {
    private static final double TURRET_RANGE =  10;
    private final LogicalMap lm;
    private final Set<Pair<Integer, Integer>> turretCoordinates = new LinkedHashSet<>();

    public TurretShooting(LogicalMap lm) {
        this.lm = lm;
        Position[][] positions = lm.getPositions();
        for (int i = 0; i < positions.length; i++) {
            Position[] row = positions[i];
            for (int j = 0; j < row.length; j++) {
                Position position = row[j];
                if (position.hasObstacle() && position.getObstacle() instanceof Turret) {
                    turretCoordinates.add(new Pair<>(i, j));
                }
            }
        }
    }

    public Set<FiringLine> fireTurrets() {
        Set<FiringLine> firingLines = new LinkedHashSet<>();
        Set<Enemy> enemies = getAliveEnemies();
        for (Pair<Integer, Integer> turretCoordinate : turretCoordinates) {
            ClosestEnemy closestEnemy = findClosestEnemy(turretCoordinate, enemies);
            if (closestEnemy != null && closestEnemy.getDistance() <= TURRET_RANGE) {
                Enemy enemy = closestEnemy.getEnemy();
                enemy.kill();
                firingLines.add(new FiringLine(turretCoordinate, enemy.getCurrentCoordinate()));
            } else {
                // DO NOT FIRE
            }
        }
        return firingLines;
    }

    private ClosestEnemy findClosestEnemy(
            Pair<Integer, Integer> sourceCoordinate,
            Set<Enemy> enemies) {
        double closestDistance = Double.MAX_VALUE;
        ClosestEnemy closest = null;
        for (Enemy enemy : enemies) {
            double distance = distance(sourceCoordinate, enemy);
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = new ClosestEnemy(distance, enemy);
            }
        }
        return closest;
    }

    private double distance(Pair<Integer, Integer> sourceCoordinate, Enemy enemy) {
        Pair<Integer, Integer> targetCoordinate = enemy.getCurrentCoordinate();
        int xa = sourceCoordinate.getKey();
        int xb = targetCoordinate.getKey();
        int ya = sourceCoordinate.getValue();
        int yb = targetCoordinate.getValue();
        return Math.sqrt((xb - xa) * (xb - xa) + (yb - ya) * (yb - ya) );
    }

    private Set<Enemy> getAliveEnemies() {
        Set<Enemy> enemies = new LinkedHashSet<>();
        for (Enemy enemy : lm.getEnemies()) {
            if (!enemy.isDead()) {
                enemies.add(enemy);
            }
        }
        return enemies;
    }

    private static class ClosestEnemy {
        private final Enemy enemy;
        private final double distance;

        public ClosestEnemy(double distance, Enemy enemy) {
            this.distance = distance;
            this.enemy = enemy;
        }

        public Enemy getEnemy() {
            return enemy;
        }

        public double getDistance() {
            return distance;
        }
    }
}
