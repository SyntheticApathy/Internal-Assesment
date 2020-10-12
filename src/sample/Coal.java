package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static sample.Main.HEIGHT;
import static sample.Main.WIDTH;

public class Coal extends Resource {
    Color color;

    static {
        new Color(0, 0, 0, 1);
    }

    public Coal(Color color) throws Exception {
        super(calculateDepth(), setWidth(), setHeight());
        this.color = color;

    }

    private static double calculateDepth() {
        return Math.random() * 1000 + 50;

    }

    private static List<Integer> setWidth() throws Exception {
        return createCoordinates(WIDTH);

    }

    private static List<Integer> setHeight() throws Exception {
        return createCoordinates(HEIGHT);

    }

    public static List<Integer> createCoordinates(int x) {
        List<Integer> coordinates = new ArrayList<>();
        int end = (int) (Math.random() * 20);
        if (end == 0) {
            end = (int) (Math.random() * 20);
        }
        int endOfCoal = (int) (Math.random() * (x - end));
        System.out.println("you got here");
        System.out.println(x+ " " + coordinates);
        for (int i = 0; i < end; i++) {
            coordinates.add(endOfCoal);
            endOfCoal--;
        }

        return coordinates;
    }
}
