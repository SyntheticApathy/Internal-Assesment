package sample.logicalmap;

import sample.logicalgameplay.Enemy;

public class Position {
    private Obstacle obstacle;
    private Enemy enemy;
    private boolean enemyShadow;

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        if (!hasObstacle()) {
            this.obstacle = obstacle;
        } else {
            //do nothing
        }
    }

    public void setEnemy(Enemy enemy) {
        if (!hasEnemy()) {
            this.enemy = enemy;
        } else {
            //do nothing
        }
    }

    public void removeEnemy() {
        enemyShadow = true;
        enemy = null;
    }

    public void removeEnemyShadow() {
        enemyShadow = false;
    }

    public boolean hasEnemyShadow() {
        return enemyShadow;
    }

    public boolean hasEnemy() {
        return enemy != null;
    }

    public boolean hasCharacter() {
        //TODO
        return false;
    }


    public boolean hasObstacle() {
        return obstacle != null;
    }


    @Override
    public String toString() {
        if (hasObstacle()) {
            return obstacle.toString();
        } else {
            return "O";
        }

    }

    public Enemy getEnemy() {
        return enemy;
    }

    public boolean hasDeadEnemy() {
        if (hasEnemy()){
            return enemy.isDead();
        }
        return false;
    }
}
