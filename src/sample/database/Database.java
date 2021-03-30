package sample.database;

import java.util.List;

public interface Database {
    void save(String userName, String saveName, GameState gameState);
    GameState load(String userName, String saveName);
    List<String> list(String userName);
    void delete(String userName, String saveName);
}
