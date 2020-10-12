package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static sample.Main.*;

public class OverWorld {

    List<Resource> xresources = new ArrayList<>();
    List<Resource> yresources = new ArrayList<>();
    public static Player player =new Player();


    public OverWorld() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        OverWorld.player = player;
    }

    public List<Pair<List<Resource>, List<Resource>>> coordinates() {
            xresources.add((Resource) Coal.createCoordinates(WIDTH));
            yresources.add((Resource) Coal.createCoordinates(HEIGHT));




    }
}


