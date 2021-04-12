package sample.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SQLiteConnection {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            //make sure data model exists in database
            executeDdl("CREATE TABLE if not exists Games (\n" +
                    "    user_name    VARCHAR (20) NOT NULL, \n" +
                    "    save_name    VARCHAR (20) NOT NULL, \n" +
                    "    round_number INTEGER      NOT NULL, \n" +
                    "    PRIMARY KEY (user_name, save_name)\n" +
                    ");\n");
            executeDdl("CREATE TABLE if not exists Boulders (\n" +
                    "    user_name VARCHAR(20) REFERENCES Games (user_name),\n" +
                    "    save_name VARCHAR(20) REFERENCES Games (save_name),\n" +
                    "    x  INTEGER NOT NULL,\n" +
                    "    y  INTEGER NOT NULL\n" +
                    ");\n");
            executeDdl("CREATE TABLE if not exists Enemies (\n" +
                    "    user_name VARCHAR(20) REFERENCES Games (user_name),\n" +
                    "    save_name VARCHAR(20) REFERENCES Games (save_name),\n" +
                    "    x  INTEGER NOT NULL,\n" +
                    "    y  INTEGER NOT NULL\n" +
                    ");\n");
            executeDdl("CREATE TABLE if not exists Trees (\n" +
                    "    user_name VARCHAR(20) REFERENCES Games (user_name),\n" +
                    "    save_name VARCHAR(20) REFERENCES Games (save_name),\n" +
                    "    x  INTEGER NOT NULL,\n" +
                    "    y  INTEGER NOT NULL\n" +
                    ");\n");
            executeDdl("CREATE TABLE if not exists Turrets (\n" +
                    "    user_name VARCHAR(20) REFERENCES Games (user_name),\n" +
                    "    save_name VARCHAR(20) REFERENCES Games (save_name),\n" +
                    "    x  INTEGER NOT NULL,\n" +
                    "    y  INTEGER NOT NULL\n" +
                    ");\n");
            return DriverManager.getConnection("jdbc:sqlite:WaveDefense.db");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeDdl(String ddlSql) {
        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:WaveDefense.db");
                PreparedStatement preparedStatement = conn.prepareStatement(ddlSql)
        ) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
