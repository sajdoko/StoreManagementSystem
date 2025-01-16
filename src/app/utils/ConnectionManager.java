package app.utils;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager implements AutoCloseable {

    final Connection conn;
    final String url;

    public static ConnectionManager connect(String url) throws SQLException {
        return new ConnectionManager(url);
    }

    ConnectionManager(String url) throws SQLException {
        this.url = url;
        conn = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established.");
    }

    public Connection getConnection() {
        return conn;
    }

    @Override
    public void close() throws SQLException {
        if(this.conn!=null && !this.conn.isClosed()) {
            this.conn.close();
            System.out.println("Connection has been closed");
        }else {
            System.out.println("Connection has already been closed");
        }
    }

    public void drop(){
        String path = url.substring("jdbc:sqlite:".length());
        File file = new File(path);
        if(file.exists()) {
            if (file.delete()){
                System.out.println("Database has been deleted.");
            }else {
                System.out.println("Database could not be deleted.");
            }
        }
    }
}
