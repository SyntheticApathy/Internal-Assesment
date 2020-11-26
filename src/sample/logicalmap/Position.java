package sample.logicalmap;

public class Position {
    private Obstacle obstacle;

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        if(! hasObstacle()) {
            this.obstacle = obstacle;
        } else {
            //do nothing
        }
    }

    public boolean hasObstacle() {
        return obstacle != null;
    }

    @Override
    public String toString() {
        if( hasObstacle()) {
            return obstacle.toString();
        } else {
            return "O";
        }

    }


}
