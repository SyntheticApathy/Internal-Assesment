package sample.database;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SQLiteDatabase implements Database {
    @Override
    public void save(String userName, String saveName, GameState gameState) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            insertGames(conn, userName, saveName, gameState.getRoundNumber());
            insertTrees(conn, userName, saveName, gameState.getTrees());
            insertBoulders(conn, userName, saveName, gameState.getBoulders());
            insertTurrets(conn, userName, saveName, gameState.getTurrets());
            insertEnemies(conn, userName, saveName, gameState.getEnemies());
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertEnemies(Connection conn, String userName, String saveName, Set<Pair<Integer, Integer>> enemies) {
        for (Pair<Integer, Integer> enemy : enemies) {
            insertEnemy(conn, userName, saveName, enemy.getKey(), enemy.getValue());
        }
    }

    private void insertEnemy(Connection conn, String userName, String saveName, int x, int y) {
        String sql = "INSERT INTO Enemies(user_name, save_name, x, y) VALUES(?,?,?,?)";
        insertCoordinates(conn, userName, saveName, x, y, sql);
    }

    private void insertTurrets(Connection conn, String userName, String saveName, Set<Pair<Integer, Integer>> turrets) {
        for (Pair<Integer, Integer> turret : turrets) {
            insertTurret(conn, userName, saveName, turret.getKey(), turret.getValue());
        }
    }

    private void insertTurret(Connection conn, String userName, String saveName, int x, int y) {
        String sql = "INSERT INTO Turrets(user_name, save_name, x, y) VALUES(?,?,?,?)";
        insertCoordinates(conn, userName, saveName, x, y, sql);
    }

    private void insertBoulders(Connection conn, String userName, String saveName, Set<Pair<Integer, Integer>> boulders) {
        for (Pair<Integer, Integer> boulder : boulders) {
            insertBoulder(conn, userName, saveName, boulder.getKey(), boulder.getValue());
        }
    }

    private void insertBoulder(Connection conn, String userName, String saveName, int x, int y) {
        String sql = "INSERT INTO Boulders(user_name, save_name, x, y) VALUES(?,?,?,?)";
        insertCoordinates(conn, userName, saveName, x, y, sql);
    }

    private void insertCoordinates(Connection conn, String userName, String saveName, int x, int y, String sql) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, saveName);
            preparedStatement.setInt(3, x);
            preparedStatement.setInt(4, y);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertTrees(Connection conn, String userName, String saveName, Set<Pair<Integer, Integer>> trees) {
        for (Pair<Integer, Integer> tree : trees) {
            insertTree(conn, userName, saveName, tree.getKey(), tree.getValue());
        }
    }

    private void insertTree(Connection conn, String userName, String saveName, int x, int y) {
        String sql = "INSERT INTO Trees(user_name, save_name, x, y) VALUES(?,?,?,?)";
        insertCoordinates(conn, userName, saveName, x, y, sql);
    }

    private void insertGames(Connection conn, String userName, String saveName, int roundNumber) {
        String sql = "INSERT INTO Games(user_name, save_name, round_number) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, saveName);
            preparedStatement.setInt(3, roundNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState load(String userName, String saveName) {
        int roundNumber = loadGame(userName, saveName);
        Set<Pair<Integer, Integer>> enemies = loadEnemies(userName, saveName);
        Set<Pair<Integer, Integer>> trees = loadTrees(userName, saveName);
        Set<Pair<Integer, Integer>> boulders = loadBoulders(userName, saveName);
        Set<Pair<Integer, Integer>> turrets = loadTurrets(userName, saveName);
        return new GameState(roundNumber, enemies, trees, boulders, turrets);;
    }

    @Override
    public List<String> list(String userName) {
        String sql = "SELECT save_name FROM Games WHERE user_name = ?";

        List<String> retVal = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String saveName = resultSet.getString(1);
                retVal.add(saveName);
            }

            return retVal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String userName, String saveName) {
        throw new UnsupportedOperationException("not implemented");
    }

    private Connection getConnection() {
        return SQLiteConnection.getConnection();
    }
}
