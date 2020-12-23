package app.utils;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class MySqlConnection {
    public static Connection getConnection() {
        Connection conn;
        try {
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream("database.properties");
            properties.load(inputStream);
            String url = "jdbc:mysql://" + properties.getProperty("host") + ":" + properties.getProperty("port") + "/" + properties.getProperty("db");
            String user = properties.getProperty("user");
            String pass = properties.getProperty("password");
            conn = DriverManager.getConnection(url,user,pass);
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}