package sample.logicalmap;

import sample.logicalgameplay.Enemy;

public class Position {
    private Obstacle obstacle;

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
        // TODO: 12/6/2020 this i guess idk 
            
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

}
