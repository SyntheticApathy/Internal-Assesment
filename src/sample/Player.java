package sample;

import javafx.scene.paint.Color;

import static sample.Main.HEIGHT;
import static sample.Main.WIDTH;

public class Player extends Entity {
    Color color = new Color(0.95,0.14,0.15,1);

    public Player(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
    }

    public Player() {
        super();
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
