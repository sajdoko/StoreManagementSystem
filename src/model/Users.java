package model;

import app.utils.MySqlConnection;
import controller.UserSessionController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {

    private long id;
    private String fullname;
    private String username;
    private String email;
    private String password;
    private int admin;
    private String status;

    Connection connection = MySqlConnection.getConnection();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return fullname;
    }

    public void setName(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int logIn(String email, String password) throws SQLException {

        String sql = "SELECT * FROM users WHERE email = ? and password = ?";

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {

            UserSessionController setSession = new UserSessionController(resultSet.getInt("id"), resultSet.getString("fullname"), resultSet.getString("username"), resultSet.getString("email"), resultSet.getInt("admin"), resultSet.getString("status"));

            return resultSet.getInt("id");
        } else {
            return 0;
        }
    }
}
