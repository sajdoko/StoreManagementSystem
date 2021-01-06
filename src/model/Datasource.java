package model;

import controller.UserSessionController;
import javafx.beans.property.StringProperty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "store_manager.sqlite";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:src\\app\\db\\" + DB_NAME;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCTS_ID = "id";
    public static final String COLUMN_PRODUCTS_NAME = "name";
    public static final String COLUMN_PRODUCTS_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCTS_PRICE = "price";
    public static final String COLUMN_PRODUCTS_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCTS_CATEGORY_ID = "category_id";

    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORIES_ID = "id";
    public static final String COLUMN_CATEGORIES_NAME = "name";
    public static final String COLUMN_CATEGORIES_DESCRIPTION = "description";

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDERS_ID = "id";
    public static final String COLUMN_ORDERS_PRODUCT_ID = "product_id";
    public static final String COLUMN_ORDERS_USER_ID = "user_id";
    public static final String COLUMN_ORDERS_SHIPPING_ADDRESS = "shipping_address";
    public static final String COLUMN_ORDERS_ORDER_EMAIL = "order_email";
    public static final String COLUMN_ORDERS_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDERS_ORDER_STATUS = "order_status";

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "id";
    public static final String COLUMN_USERS_FULLNAME = "fullname";
    public static final String COLUMN_USERS_USERNAME = "username";
    public static final String COLUMN_USERS_EMAIL = "email";
    public static final String COLUMN_USERS_PASSWORD = "password";
    public static final String COLUMN_USERS_ADMIN = "admin";
    public static final String COLUMN_USERS_STATUS = "status";

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

    public User logIn(String email) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_EMAIL + " = ?");
        preparedStatement.setString(1, email);
        ResultSet results = preparedStatement.executeQuery();

        User user = new User();
        if (results.next()) {

            user.setId(results.getInt("id"));
            user.setFullname(results.getString("fullname"));
            user.setUsername(results.getString("username"));
            user.setEmail(results.getString("email"));
            user.setPassword(results.getString("password"));
            user.setSalt(results.getString("salt"));
            user.setAdmin(results.getInt("admin"));
            user.setStatus(results.getString("status"));

        }

        return user;
    }

    public List<Product> getAllProducts(int sortOrder) {

        StringBuilder queryProducts = queryProducts();

        if (sortOrder != ORDER_BY_NONE) {
            queryProducts.append(" ORDER BY ");
            queryProducts.append(COLUMN_PRODUCTS_NAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryProducts.append("DESC");
            } else {
                queryProducts.append("ASC");
            }
        }
//        System.out.println(queryProducts.toString());
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryProducts.toString())) {

            List<Product> products = new ArrayList<>();
            while (results.next()) {
                Product product = new Product();
                product.setId(results.getInt(1));
                product.setName(results.getString(2));
                product.setDescription(results.getString(3));
                product.setPrice(results.getDouble(4));
                product.setQuantity(results.getInt(5));
                product.setCategory(results.getString(6));
                product.setNr_sales(results.getInt(7));
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Product> getOneProduct(int product_id) {

        StringBuilder queryProducts = queryProducts();
        queryProducts.append(" WHERE " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + " = ?");
//        System.out.println(queryProducts.toString());
        try (PreparedStatement statement = conn.prepareStatement(String.valueOf(queryProducts))) {
            statement.setInt(1, product_id);
            ResultSet results = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (results.next()) {
                Product product = new Product();
                product.setId(results.getInt(1));
                product.setName(results.getString(2));
                product.setDescription(results.getString(3));
                product.setPrice(results.getDouble(4));
                product.setQuantity(results.getInt(5));
                product.setCategory(results.getString(6));
                product.setNr_sales(results.getInt(7));
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Product> searchProducts(String searchString, int sortOrder) {


        StringBuilder queryProducts = queryProducts();

        queryProducts.append(" WHERE (" + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + " LIKE ? OR " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_DESCRIPTION + " LIKE ?)");

        if (sortOrder != ORDER_BY_NONE) {
            queryProducts.append(" ORDER BY ");
            queryProducts.append(COLUMN_PRODUCTS_NAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryProducts.append("DESC");
            } else {
                queryProducts.append("ASC");
            }
        }

        try (PreparedStatement statement = conn.prepareStatement(queryProducts.toString())) {
            statement.setString(1, "%" + searchString + "%");
            statement.setString(2, "%" + searchString + "%");
            ResultSet results = statement.executeQuery();

            List<Product> products = new ArrayList<>();
            while (results.next()) {
                Product product = new Product();
                product.setId(results.getInt(1));
                product.setName(results.getString(2));
                product.setDescription(results.getString(3));
                product.setPrice(results.getDouble(4));
                product.setQuantity(results.getInt(5));
                product.setCategory(results.getString(6));
                product.setNr_sales(results.getInt(7));
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    private StringBuilder queryProducts() {
        return new StringBuilder("SELECT " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_DESCRIPTION + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_PRICE + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_QUANTITY + ", " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_NAME + ", " +
                " (SELECT COUNT(*) FROM " + TABLE_ORDERS + " WHERE " + TABLE_ORDERS + "." + COLUMN_ORDERS_PRODUCT_ID + " = " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + ") AS nr_sales" +
                " FROM " + TABLE_PRODUCTS +
                " LEFT JOIN " + TABLE_CATEGORIES +
                " ON " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_CATEGORY_ID +
                " = " + TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_ID
        );
    }

    public boolean deleteSingleProduct(int productId) {
        String sql = "DELETE FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, productId);
            int rows = statement.executeUpdate();
            System.out.println(rows + " record(s) deleted.");
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    public boolean insertNewProduct(String name, String description, int price, int quantity, int category_id) {

        String sql = "INSERT INTO " + TABLE_PRODUCTS + " ("
                + COLUMN_PRODUCTS_NAME + ", "
                + COLUMN_PRODUCTS_DESCRIPTION + ", "
                + COLUMN_PRODUCTS_PRICE + ", "
                + COLUMN_PRODUCTS_QUANTITY + ", "
                + COLUMN_PRODUCTS_CATEGORY_ID +
                ") VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setInt(3, price);
            statement.setInt(4, quantity);
            statement.setInt(5, category_id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    public Integer countAllProducts() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + TABLE_PRODUCTS)) {
            if (results.next()) {
                return results.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return 0;
        }
    }

    public Integer countAllCustomers() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + TABLE_ORDERS +
                 " LEFT JOIN " + TABLE_USERS +
                 " ON " + TABLE_ORDERS + "." + COLUMN_ORDERS_ID +
                 " = " + TABLE_USERS + "." + COLUMN_USERS_ID
             )
        ) {
            if (results.next()) {
                return results.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return 0;
        }
    }
}















