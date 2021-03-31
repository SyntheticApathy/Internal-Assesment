package sample.logicalgameplay;
/**
 * This class handles all logical gameplay, such as deleting enemies and logic
 */

import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import sample.GameUI;
import sample.logicalmap.LogicalMap;
import sample.logicalmap.Position;
import sample.logicalmap.Turret;

import java.util.Set;

public class GameLogic {
    private static int roundNumber = 1;

    public GameLogic(int roundNumber) {


    }

    public static int getRoundNumber() {
        return roundNumber;
    }

    public static void setRoundNumber(int roundNumber) {
        GameLogic.roundNumber = roundNumber;
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

    public static void turretLogic(int xPosition, int yPosition, Position[][] positions, LogicalMap lm, AnchorPane root) {
        for (Position[] position : positions) {
            for (int j = 0; j < position.length; j++) {
                if (!positions[xPosition][yPosition].hasObstacle() && GameUI.amountOfTurretsWhichCanBePlaced[0] > 0) {
                    lm.addTurret(xPosition, yPosition);
                    GameUI.displayTurret(xPosition, yPosition, new Turret(), root);
                    GameUI.amountOfTurretsWhichCanBePlaced[0]--;
                    GameUI.turretPlaced();
                }
            }
        }
    }


    public static void deleteDeadEnemies(LogicalMap lm) {
        Set<Enemy> enemies = lm.getEnemies();
        enemies.removeIf(Enemy::isDead);
    }

    public static void shootEnemy(LogicalMap lm, AnchorPane root) {
        Set<FiringLine> firingLines = new TurretShooting(lm).fireTurrets();
        for (FiringLine firingLine : firingLines) {
            GameUI.displayFiringLine(firingLine, root);
        }
    }

    public static boolean isGameLost(LogicalMap lm) {
        Set<Enemy> enemies = lm.getEnemies();
        if (!enemies.isEmpty()) {
            for (Enemy enemy : enemies) {
                if (enemy.getCurrentPosition() == enemy.getEnemyPath().size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean enemiesOnMap(LogicalMap lm) {
        Set<Enemy> enemies = lm.getEnemies();
        return !enemies.isEmpty();
    }

    public static int translatePositionCoordinateIntoGUI(int i) {
        return i * 5;
    }

}
