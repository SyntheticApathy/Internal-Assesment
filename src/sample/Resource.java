package sample;

import java.util.List;

public class Resource {
    double depth;
    List<Integer> x, y;

    public Resource(double depth, List<Integer> x, List<Integer> y) {
        this.depth = depth;
        this.x = x;
        this.y = y;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public List<Integer> getX() {
        return x;
    }

    public void setX(List<Integer> x) {
        this.x = x;
    }

    public List<Integer> getY() {
        return y;
    }

    public void setY(List<Integer> y) {
        this.y = y;
    }
}
