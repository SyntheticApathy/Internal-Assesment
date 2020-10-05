package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static sample.Main.HEIGHT;
import static sample.Main.WIDTH;

public class OverWorld {
    Player player = new Player(WIDTH / 2, HEIGHT / 2);
    List<Resource> xresources = new ArrayList<>();
    List<Resource> yresources = new ArrayList<>();
    List<Pair<List<Resource>, List<Resource>>> resources = new ArrayList<>();


    public OverWorld() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void coordinates() {

            xresources.add((Resource) Coal.createCoordinates(WIDTH));

            yresources.add((Resource) Coal.createCoordinates(HEIGHT));



    }
}


