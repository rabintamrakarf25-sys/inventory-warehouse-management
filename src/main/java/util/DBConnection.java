package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String CONFIG_FILE = "db.properties";

    private static String url;
    private static String username;
    private static String password;

    static {
        loadProperties();
    }

    private static void loadProperties() {

        Properties properties = new Properties();

        try (FileInputStream input =
                     new FileInputStream(CONFIG_FILE)) {

            properties.load(input);

            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not load db.properties.",
                    e
            );
        }
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }
}