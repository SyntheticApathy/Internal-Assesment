package sample.logicalmap;

import sample.logicalgameplay.Enemy;

public class Position {
    private Obstacle obstacle;
    private Enemy enemy;

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
        if (!hasEnemy()){
            this.enemy = enemy;
        } else {
            //do nothing
        }
    }

    public void removeEnemy() {
        enemy = null;
    }

    public boolean hasEnemy() {
        return enemy != null;
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
}
