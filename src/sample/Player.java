package sample;

import javafx.scene.paint.Color;



public class Player extends Entity {
    Color color = new Color(0,0.74,0.15,1);

    public Player(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
    }

    public Player() {
        super();
    }


    public Color getColor() {
        return color;
    }

}
