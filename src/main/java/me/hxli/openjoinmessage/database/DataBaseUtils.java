package me.hxli.openjoinmessage.database;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.logging.Logger;

public class DataBaseUtils{

    private final String addTableCustomMessages = ("CREATE TABLE `ojm`.`custommessages` (`Type` VARCHAR(242) NOT NULL COLLATE 'utf8mb4_bin', `Value` VARCHAR(242) NULL DEFAULT NULL COLLATE 'utf8mb4_bin', PRIMARY KEY (`Type`(242))) ENGINE = InnoDB;");
    private final String createDBScript = ("CREATE DATABASE `ojm`;"+addTableCustomMessages);

    private final PreparedStatement createMessage;
    private final PreparedStatement readMessage;
    private final PreparedStatement updateMessage;
    private final PreparedStatement deleteMessage;

    private final String defaultusr = "defaultusr";
    private final String defaultpw = "defaultpw";
    private boolean usingDefaultConfig;
    private String dbURL;
    private String username;
    private String password;

    private Logger logger;

    private Connection connection;

    public DataBaseUtils(Logger logger) throws SQLException, ClassNotFoundException {

        this.logger = logger;
        int i;
        boolean dbExists = false;
        this.usingDefaultConfig = false;
        Statement statement = null;

        this.usingDefaultConfig = readJSONFile();

        if (this.usingDefaultConfig) {
            logger.info("You're using the default config, ojmdb_config.json needs to be modified!");
        } else {
            try {
                Class.forName("org.mariadb.jdbc.Driver");

                this.connection = DriverManager.getConnection(
                        this.dbURL,
                        this.username,
                        this.password
                );

                statement = connection.createStatement();

                String[] createDBScriptArray = createDBScript.split(";");
                for (i = 0; i < createDBScriptArray.length; i++) {
                    statement.executeUpdate(createDBScriptArray[i]);
                }

                this.connection.close();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1007) {
                    // database already exists
                    dbExists = true;
                } else if (e.getErrorCode() == 1045) {
                    // access denied
                    logger.info("Access denied (maybe incorrect login information?");
                } else {
                    // other exceptions (server down, no perms, etc.)
                    logger.info("Exception occurred while trying to run the database generation script");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException();
            }

            if (!dbExists)
                logger.info("Database created");
        }

        Class.forName("org.mariadb.jdbc.Driver");

        this.connection = DriverManager.getConnection(
                dbURL + "ojm?useSSL=false",
                username,
                password
        );

        try {
            PreparedStatement tableCheckStatement = connection.prepareStatement("SHOW TABLES LIKE 'custommessages'");
            ResultSet resultSet = tableCheckStatement.executeQuery();
            if (!resultSet.next()) {
                // Table doesn't exist, create it
                logger.info("Table \"custommessages\" not found. creating it...");
                connection.prepareStatement(addTableCustomMessages).executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createMessage = connection.prepareStatement("INSERT INTO custommessages(Type, Value) VALUES(?, ?)");
        readMessage = connection.prepareStatement("SELECT * FROM custommessages WHERE Type=?");
        updateMessage = connection.prepareStatement("UPDATE custommessages SET Value=? WHERE Type=?");
        deleteMessage = connection.prepareStatement("DELETE FROM custommessages WHERE Type=?");
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

    public boolean readJSONFile() {
        String filename = "plugins/ojmdb_config.json";
        boolean flag = false;

        try {
            String configContent;
            if (Files.exists(Paths.get(filename))) {
                configContent = new String(Files.readAllBytes(Paths.get(filename)));
            } else {
                JSONObject config = new JSONObject();
                config.put("url", "jdbc:mariadb://localhost:3306/");
                config.put("username", this.defaultusr);
                config.put("password", this.defaultpw);

                Files.write(Paths.get(filename), config.toString().getBytes(), StandardOpenOption.CREATE);
                configContent = config.toString();
            }

            JSONObject jsonObject = new JSONObject(configContent);

            String url = jsonObject.getString("url");
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            this.dbURL = url;
            this.username = username;
            this.password = password;

            if (this.username.equals(this.defaultusr) || this.password.equals(this.defaultpw))
                flag = true;
        } catch (IOException | JSONException e) {
            System.out.println("Error handling JSON file: " + e.getMessage());
        }

        return flag;
    }

    public boolean getUsingDefaultConfig() {
        return this.usingDefaultConfig;
    }
}