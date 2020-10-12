package sample;


import java.util.ArrayList;
import java.util.List;

import static sample.Main.*;

public class OverWorld {

    List<Resource> xresources = new ArrayList<>();
    List<Resource> yresources = new ArrayList<>();
    public static Player player = new Player();


    public OverWorld() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        OverWorld.player = player;
    }

    public List<Pair<List<Resource>, List<Resource>>> coordinates() {
        List<sample.Pair<List<Resource>, List<Resource>>> x = new ArrayList<>();
        sample.Pair<List<Resource>, List<Resource>> pair = new sample.Pair<>();

        xresources.add((Resource) Coal.createCoordinates(WIDTH));
        yresources.add((Resource) Coal.createCoordinates(HEIGHT));

        pair.addXReources(xresources);
        pair.addYResources(yresources);

        x.add(pair);

        return x;


    }
}


