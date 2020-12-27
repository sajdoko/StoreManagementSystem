package model;

import controller.UserSessionController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "store_manager.sqlite";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:src\\app\\db\\" + DB_NAME;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "id";
    public static final String COLUMN_USERS_FULLNAME = "fullname";
    public static final String COLUMN_USERS_USERNAME = "username";
    public static final String COLUMN_USERS_EMAIL = "email";
    public static final String COLUMN_USERS_PASSWORD = "password";
    public static final String COLUMN_USERS_ADMIN = "admin";
    public static final String COLUMN_USERS_STATUS = "status";
    public static final int INDEX_USERS_ID = 1;
    public static final int INDEX_USERS_FULLNAME = 2;
    public static final int INDEX_USERS_USERNAME = 3;
    public static final int INDEX_USERS_EMAIL = 4;
    public static final int INDEX_USERS_PASSWORD = 5;
    public static final int INDEX_USERS_ADMIN = 6;
    public static final int INDEX_USERS_STATUS = 7;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCTS_ID = "id";
    public static final String COLUMN_PRODUCTS_NAME = "name";
    public static final String COLUMN_PRODUCTS_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCTS_PRICE = "price";
    public static final String COLUMN_PRODUCTS_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCTS_CATEGORY = "category";
    public static final int INDEX_PRODUCTS_ID = 1;
    public static final int INDEX_PRODUCTS_NAME = 2;
    public static final int INDEX_PRODUCTS_DESCRIPTION = 3;
    public static final int INDEX_PRODUCTS_PRICE = 4;
    public static final int INDEX_PRODUCTS_QUANTITY = 5;
    public static final int INDEX_PRODUCTS_CATEGORY = 6;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    private Connection conn;


    private static final Datasource instance = new Datasource();

    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public int logIn(String email, String password) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_EMAIL + " = ? AND " + COLUMN_USERS_PASSWORD + " = ? "
        );
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);

        ResultSet results = preparedStatement.executeQuery();

        if (results.next()) {

            new UserSessionController(
                    results.getInt(INDEX_USERS_ID),
                    results.getString(INDEX_USERS_FULLNAME),
                    results.getString(INDEX_USERS_USERNAME),
                    results.getString(INDEX_USERS_EMAIL),
                    results.getInt(INDEX_USERS_ADMIN),
                    results.getString(INDEX_USERS_STATUS)
            );

            return results.getInt(INDEX_USERS_ID);
        } else {
            return 0;
        }

    }

    public List<Product> getAllProducts(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_PRODUCTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_PRODUCTS_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Product> products = new ArrayList<>();
            while (results.next()) {
                Product product = new Product();
                product.setId(results.getInt(INDEX_PRODUCTS_ID));
                product.setName(results.getString(INDEX_PRODUCTS_NAME));
                products.add(product);
            }

            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

}















