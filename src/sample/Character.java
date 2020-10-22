package sample;

import javafx.util.Pair;

import java.util.List;

public class Character {
    String username;
    double HP;
    List<Item> inventory;   // TODO: 10/22/2020 connect to DB
    // TODO: 10/23/2020 create image of character
    Pair<Integer,Integer> position;
}
