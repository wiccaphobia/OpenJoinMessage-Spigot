package me.hxli.openjoinmessage.database;

import java.sql.*;

public class DataBaseUtils {

    private final PreparedStatement createMessage;
    private final PreparedStatement readMessage;
    private final PreparedStatement updateMessage;
    private final PreparedStatement deleteMessage;

    private Connection connection;

    public DataBaseUtils() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            this.connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/ojm?useSSL=false",
                    System.getenv("OJM_DB_USER"),
                    System.getenv("OJM_DB_PW")
            );

            createMessage = connection.prepareStatement("INSERT INTO custommessages(Type, Value) VALUES(?, ?)");
            readMessage = connection.prepareStatement("SELECT * FROM custommessages WHERE Type=?");
            updateMessage = connection.prepareStatement("UPDATE custommessages SET Value=? WHERE Type=?");
            deleteMessage = connection.prepareStatement("DELETE FROM custommessages WHERE Type=?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createValueMessage(String type, String value) {
        try {
            createMessage.setString(1, type);
            createMessage.setString(2, value.replace("&", "§"));
            createMessage.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String readValueMessage(String type) {
        String value = null;

        try {
            readMessage.setString(1, type);
            ResultSet rs = readMessage.executeQuery();

            while(rs.next()) {
                value = rs.getString("Value");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return value;
    }

    public void updateValueMessage(String type, String value) {
        try {
            updateMessage.setString(1, value.replace("&", "§"));
            updateMessage.setString(2, type);
            updateMessage.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteValueMessage(String type) {
        try {
            deleteMessage.setString(1, type);
            deleteMessage.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
