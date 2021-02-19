package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all the queries to the database.
 * It is constructed with the Singleton Design Pattern.
 *
 * This pattern involves a single class which is responsible to create an object while making sure that only single
 * object gets created. This class provides a way to access its only object which can be accessed directly without
 * need to instantiate the object of the class.
 *
 * @author      Sajmir Doko
 */
public class Datasource extends Product {

    public static final String DB_NAME = "store_manager.sqlite";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:src\\app\\db\\" + DB_NAME;

    // All the database tables and their columns are stored as String variables.
    // This to facilitate later changing of table/columns names, if needed, for example when expanding
    // the Datasource Class.
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
    public static final String COLUMN_ORDERS_ORDER_DATE = "order_date";
    public static final String COLUMN_ORDERS_ORDER_STATUS = "order_status";

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERS_ID = "id";
    public static final String COLUMN_USERS_FULLNAME = "fullname";
    public static final String COLUMN_USERS_USERNAME = "username";
    public static final String COLUMN_USERS_EMAIL = "email";
    public static final String COLUMN_USERS_PASSWORD = "password";
    public static final String COLUMN_USERS_SALT = "salt";
    public static final String COLUMN_USERS_ADMIN = "admin";
    public static final String COLUMN_USERS_STATUS = "status";

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    private Connection conn;

    /**
     * Create an object of Datasource
     */
    private static final Datasource instance = new Datasource();

    /**
     * Make the constructor private so that this class cannot be instantiated
     */
    private Datasource() { }

    /**
     * Get the only object available
     * @return      Datasource instance.
     * @since                   1.0.0
     */
    public static Datasource getInstance() {
        return instance;
    }

    /**
     * This method makes the connection to the database and assigns the Connection to the conn variable.
     * It is designed to be called in the application's Main method.
     * @return boolean      Returns true or false.
     * @since               1.0.0
     */
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * This method closes the connection to the database.
     * It is designed to be called in the application's Main method.
     * @since       1.0.0
     */
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    // BEGIN PRODUCTS QUERIES
    /**
     * This method get all the products from the database.
     * @param sortOrder     Results sort order.
     * @return List         Returns Product array list.
     * @since                   1.0.0
     */
    public List<Product> getAllProducts(int sortOrder) {

        StringBuilder queryProducts = queryProducts();

        if (sortOrder != ORDER_BY_NONE) {
            queryProducts.append(" ORDER BY ");
            queryProducts.append(COLUMN_PRODUCTS_NAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryProducts.append(" DESC");
            } else {
                queryProducts.append(" ASC");
            }
        }
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
                product.setCategory_name(results.getString(6));
                product.setNr_sales(results.getInt(7));
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method get one product from the database based on the provided product_id.
     * @param product_id    Product id.
     * @return List         Returns Product array list.
     * @since                   1.0.0
     */
    public List<Product> getOneProduct(int product_id) {

        StringBuilder queryProducts = queryProducts();
        queryProducts.append(" WHERE " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + " = ? LIMIT 1");
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
                product.setCategory_name(results.getString(6));
                product.setNr_sales(results.getInt(7));
                product.setCategory_id(results.getInt(8));
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method searches products from the database based on the provided searchString.
     * @param searchString  String to search product name or product description.
     * @param sortOrder     Results sort order.
     * @return List         Returns Product array list.
     * @since                   1.0.0
     */
    public List<Product> searchProducts(String searchString, int sortOrder) {
        StringBuilder queryProducts = queryProducts();
        queryProducts.append(" WHERE (" + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + " LIKE ? OR " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_DESCRIPTION + " LIKE ?)");

        if (sortOrder != ORDER_BY_NONE) {
            queryProducts.append(" ORDER BY ");
            queryProducts.append(COLUMN_PRODUCTS_NAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryProducts.append(" DESC");
            } else {
                queryProducts.append(" ASC");
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
                product.setCategory_name(results.getString(6));
                product.setNr_sales(results.getInt(7));
                products.add(product);
            }
            return products;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This private method returns an default query for the products.
     * @return StringBuilder
     * @since                   1.0.0
     */
    private StringBuilder queryProducts() {
        return new StringBuilder("SELECT " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_DESCRIPTION + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_PRICE + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_QUANTITY + ", " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_NAME + ", " +
                " (SELECT COUNT(*) FROM " + TABLE_ORDERS + " WHERE " + TABLE_ORDERS + "." + COLUMN_ORDERS_PRODUCT_ID + " = " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID + ") AS nr_sales" + ", " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_ID +
                " FROM " + TABLE_PRODUCTS +
                " LEFT JOIN " + TABLE_CATEGORIES +
                " ON " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_CATEGORY_ID +
                " = " + TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_ID
        );
    }

    /**
     * This method deletes one product based on the productId provided.
     * @param productId     Product id.
     * @return boolean      Returns true or false.
     * @since                   1.0.0
     */
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

    /**
     * This method insert one product to the database.
     * @param name          Product name.
     * @param description   Product description.
     * @param price         Product price.
     * @param quantity      Product quantity.
     * @param category_id   Product category_id.
     * @return boolean      Returns true or false.
     * @since                   1.0.0
     */
    public boolean insertNewProduct(String name, String description, double price, int quantity, int category_id) {

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
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.setInt(5, category_id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * This method updates one product to the database.
     * @param product_id    Product id.
     * @param name          Product name.
     * @param description   Product description.
     * @param price         Product price.
     * @param quantity      Product quantity.
     * @param category_id   Product category_id.
     * @return boolean      Returns true or false.
     * @since                   1.0.0
     */
    public boolean updateOneProduct(int product_id, String name, String description, double price, int quantity, int category_id) {

        String sql = "UPDATE " + TABLE_PRODUCTS + " SET "
                + COLUMN_PRODUCTS_NAME + " = ?" + ", "
                + COLUMN_PRODUCTS_DESCRIPTION + " = ?" + ", "
                + COLUMN_PRODUCTS_PRICE + " = ?" + ", "
                + COLUMN_PRODUCTS_QUANTITY + " = ?" + ", "
                + COLUMN_PRODUCTS_CATEGORY_ID + " = ?" +
                " WHERE " + COLUMN_PRODUCTS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.setInt(5, category_id);
            statement.setInt(6, product_id);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * This method decreases the product stock by one based on the provided product_id.
     * @param product_id    Product id.
     * @since                   1.0.0
     */
    public void decreaseStock(int product_id) {

        String sql = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_PRODUCTS_QUANTITY + " = " + COLUMN_PRODUCTS_QUANTITY + " - 1 WHERE " + COLUMN_PRODUCTS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, product_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
    // END PRODUCTS QUERIES

    // BEGIN CATEGORIES QUERIES

    /**
     * This method gets all the product categories from the database.
     * @param sortOrder     Results sort order.
     * @return List         Returns Categories array list.
     * @since                   1.0.0
     */
    public List<Categories> getProductCategories(int sortOrder) {
        StringBuilder queryCategories = new StringBuilder("SELECT " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_ID + ", " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_NAME + ", " +
                TABLE_CATEGORIES + "." + COLUMN_CATEGORIES_DESCRIPTION +
                " FROM " + TABLE_CATEGORIES
        );

        if (sortOrder != ORDER_BY_NONE) {
            queryCategories.append(" ORDER BY ");
            queryCategories.append(COLUMN_CATEGORIES_ID);
            if (sortOrder == ORDER_BY_DESC) {
                queryCategories.append(" DESC");
            } else {
                queryCategories.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryCategories.toString())) {

            List<Categories> categories = new ArrayList<>();
            while (results.next()) {
                Categories category = new Categories();
                category.setId(results.getInt(1));
                category.setName(results.getString(2));
                categories.add(category);
            }
            return categories;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
    // END CATEGORIES QUERIES

    // BEGIN CUSTOMERS QUERIES
    /**
     * This method get all the customers from the database.
     * @param sortOrder     Results sort order.
     * @return List         Returns Customer array list.
     * @since                   1.0.0
     */
    public List<Customer> getAllCustomers(int sortOrder) {

        StringBuilder queryCustomers = queryCustomers();

        if (sortOrder != ORDER_BY_NONE) {
            queryCustomers.append(" ORDER BY ");
            queryCustomers.append(COLUMN_USERS_FULLNAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryCustomers.append(" DESC");
            } else {
                queryCustomers.append(" ASC");
            }
        }
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryCustomers.toString())) {

            List<Customer> customers = new ArrayList<>();
            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(1));
                customer.setFullname(results.getString(2));
                customer.setEmail(results.getString(3));
                customer.setUsername(results.getString(4));
                customer.setOrders(results.getInt(5));
                customer.setStatus(results.getString(6));
                customers.add(customer);
            }
            return customers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method get one customer from the database based on the provided product_id.
     * @param customer_id   Customer id.
     * @return List         Returns Product array list.
     * @since                   1.0.0
     */
    public List<Customer> getOneCustomer(int customer_id) {

        StringBuilder queryCustomers = queryCustomers();
        queryCustomers.append(" AND " + TABLE_USERS + "." + COLUMN_USERS_ID + " = ?");
        try (PreparedStatement statement = conn.prepareStatement(String.valueOf(queryCustomers))) {
            statement.setInt(1, customer_id);
            ResultSet results = statement.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(1));
                customer.setFullname(results.getString(2));
                customer.setEmail(results.getString(3));
                customer.setUsername(results.getString(4));
                customer.setOrders(results.getInt(5));
                customer.setStatus(results.getString(6));
                customers.add(customer);
            }
            return customers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method searches customers from the database based on the provided searchString.
     * @param searchString  String to search product name or product description.
     * @param sortOrder     Results sort order.
     * @return List         Returns Product array list.
     * @since                   1.0.0
     */
    public List<Customer> searchCustomers(String searchString, int sortOrder) {

        StringBuilder queryCustomers = queryCustomers();

        queryCustomers.append(" AND (" + TABLE_USERS + "." + COLUMN_USERS_FULLNAME + " LIKE ? OR " + TABLE_USERS + "." + COLUMN_USERS_USERNAME + " LIKE ?)");

        if (sortOrder != ORDER_BY_NONE) {
            queryCustomers.append(" ORDER BY ");
            queryCustomers.append(COLUMN_USERS_FULLNAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryCustomers.append(" DESC");
            } else {
                queryCustomers.append(" ASC");
            }
        }

        try (PreparedStatement statement = conn.prepareStatement(queryCustomers.toString())) {
            statement.setString(1, "%" + searchString + "%");
            statement.setString(2, "%" + searchString + "%");
            ResultSet results = statement.executeQuery();

            List<Customer> customers = new ArrayList<>();
            while (results.next()) {
                Customer customer = new Customer();
                customer.setId(results.getInt(1));
                customer.setFullname(results.getString(2));
                customer.setEmail(results.getString(3));
                customer.setUsername(results.getString(4));
                customer.setOrders(results.getInt(5));
                customer.setStatus(results.getString(6));
                customers.add(customer);
            }
            return customers;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This private method returns an default query for the customers.
     * @return StringBuilder
     * @since                   1.0.0
     */
    private StringBuilder queryCustomers() {
        return new StringBuilder("SELECT " +
                TABLE_USERS + "." + COLUMN_USERS_ID + ", " +
                TABLE_USERS + "." + COLUMN_USERS_FULLNAME + ", " +
                TABLE_USERS + "." + COLUMN_USERS_EMAIL + ", " +
                TABLE_USERS + "." + COLUMN_USERS_USERNAME + ", " +
                " (SELECT COUNT(*) FROM " + TABLE_ORDERS + " WHERE " + TABLE_ORDERS + "." + COLUMN_ORDERS_USER_ID + " = " + TABLE_USERS + "." + COLUMN_USERS_ID + ") AS orders" + ", " +
                TABLE_USERS + "." + COLUMN_USERS_STATUS +
                " FROM " + TABLE_USERS +
                " WHERE " + TABLE_USERS + "." + COLUMN_USERS_ADMIN + " = 0"
        );
    }

    /**
     * This method deletes one customer based on the customerId provided.
     * @param customerId    Customer id.
     * @return boolean      Returns true or false.
     * @since                   1.0.0
     */
    public boolean deleteSingleCustomer(int customerId) {
        String sql = "DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_ID + " = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            int rows = statement.executeUpdate();
            System.out.println(rows + " " + TABLE_USERS + " record(s) deleted.");


            String sql2 = "DELETE FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDERS_USER_ID + " = ?";

            try (PreparedStatement statement2 = conn.prepareStatement(sql2)) {
                statement2.setInt(1, customerId);
                int rows2 = statement2.executeUpdate();
                System.out.println(rows2 + " " + TABLE_ORDERS + " record(s) deleted.");
                return true;
            } catch (SQLException e) {
                System.out.println("Query failed: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }
    // END CUSTOMERS QUERIES

    // BEGIN CUSTOMERS QUERIES

    /**
     * This method gets one user from the database based on the email provided.
     * @param email             Accepts email string.
     * @throws SQLException     If an SQL error occurred.
     * @return User             Returns the User Object.
     * @since                   1.0.0
     */
    public User getUserByEmail(String email) throws SQLException {

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

    /**
     * This method gets one user from the database based on the username provided.
     * @param username          Accepts username string.
     * @throws SQLException     If an SQL error occurred.
     * @return User             Returns the User Object.
     * @since                   1.0.0
     */
    public User getUserByUsername(String username) throws SQLException {

        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERS_USERNAME + " = ?");
        preparedStatement.setString(1, username);
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

    /**
     * This method insert one simple user to the database.
     * @param fullName      Users full name.
     * @param username      Users username
     * @param email         Users email.
     * @param password      Users password.
     * @param salt          Users salt.
     * @return boolean      Returns true or false.
     * @since                   1.0.0
     */
    public boolean insertNewUser(String fullName, String username, String email, String password, String salt) {

        String sql = "INSERT INTO " + TABLE_USERS + " ("
                + COLUMN_USERS_FULLNAME + ", "
                + COLUMN_USERS_USERNAME + ", "
                + COLUMN_USERS_EMAIL + ", "
                + COLUMN_USERS_PASSWORD + ", "
                + COLUMN_USERS_SALT + ", "
                + COLUMN_USERS_ADMIN + ", "
                + COLUMN_USERS_STATUS +
                ") VALUES (?, ?, ?, ?, ?, 0, 'enabled')";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, fullName);
            statement.setString(2, username);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, salt);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }
    // END CUSTOMERS QUERIES

    // BEGIN ORDERS QUERIES
    /**
     * This method gets all orders from the database.
     * @param sortOrder     Results sort order.
     * @return List         Returns Order array list.
     * @since                   1.0.0
     */
    public List<Order> getAllOrders(int sortOrder) {

        StringBuilder queryOrders = new StringBuilder("SELECT " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_ID + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_PRODUCT_ID + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_USER_ID + ", " +
                TABLE_USERS + "." + COLUMN_USERS_FULLNAME + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_ORDER_DATE + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_ORDER_STATUS + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_PRICE +
                " FROM " + TABLE_ORDERS
        );

        queryOrders.append("" +
                " LEFT JOIN " + TABLE_PRODUCTS +
                " ON " + TABLE_ORDERS + "." + COLUMN_ORDERS_PRODUCT_ID +
                " = " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID);
        queryOrders.append("" +
                " LEFT JOIN " + TABLE_USERS +
                " ON " + TABLE_ORDERS + "." + COLUMN_ORDERS_USER_ID +
                " = " + TABLE_USERS + "." + COLUMN_USERS_ID);

        if (sortOrder != ORDER_BY_NONE) {
            queryOrders.append(" ORDER BY ");
            queryOrders.append(COLUMN_USERS_FULLNAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryOrders.append(" DESC");
            } else {
                queryOrders.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryOrders.toString())) {

            List<Order> orders = new ArrayList<>();
            while (results.next()) {
                Order order = new Order();
                order.setId(results.getInt(1));
                order.setProduct_id(results.getInt(2));
                order.setUser_id(results.getInt(3));
                order.setUser_full_name(results.getString(4));
                order.setOrder_date(results.getString(5));
                order.setOrder_status(results.getString(6));
                order.setProduct_name(results.getString(7));
                order.setOrder_price(results.getDouble(8));
                orders.add(order);
            }
            return orders;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method gets all orders of the simple user from the database.
     * @param sortOrder     Results sort order.
     * @param user_id       Provided user id.
     * @return List         Returns Order array list.
     * @since                   1.0.0
     */
    public List<Order> getAllUserOrders(int sortOrder, int user_id) {

        StringBuilder queryOrders = new StringBuilder("SELECT " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_ID + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_PRODUCT_ID + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_USER_ID + ", " +
                TABLE_USERS + "." + COLUMN_USERS_FULLNAME + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_ORDER_DATE + ", " +
                TABLE_ORDERS + "." + COLUMN_ORDERS_ORDER_STATUS + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_NAME + ", " +
                TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_PRICE +
                " FROM " + TABLE_ORDERS
        );

        queryOrders.append("" +
                " LEFT JOIN " + TABLE_PRODUCTS +
                " ON " + TABLE_ORDERS + "." + COLUMN_ORDERS_PRODUCT_ID +
                " = " + TABLE_PRODUCTS + "." + COLUMN_PRODUCTS_ID);
        queryOrders.append("" +
                " LEFT JOIN " + TABLE_USERS +
                " ON " + TABLE_ORDERS + "." + COLUMN_ORDERS_USER_ID +
                " = " + TABLE_USERS + "." + COLUMN_USERS_ID);
        queryOrders.append(" WHERE " + TABLE_ORDERS + "." + COLUMN_ORDERS_USER_ID + " = ").append(user_id);

        if (sortOrder != ORDER_BY_NONE) {
            queryOrders.append(" ORDER BY ");
            queryOrders.append(COLUMN_USERS_FULLNAME);
            if (sortOrder == ORDER_BY_DESC) {
                queryOrders.append(" DESC");
            } else {
                queryOrders.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(queryOrders.toString())) {

            List<Order> orders = new ArrayList<>();
            while (results.next()) {
                Order order = new Order();
                order.setId(results.getInt(1));
                order.setProduct_id(results.getInt(2));
                order.setUser_id(results.getInt(3));
                order.setUser_full_name(results.getString(4));
                order.setOrder_date(results.getString(5));
                order.setOrder_status(results.getString(6));
                order.setProduct_name(results.getString(7));
                order.setOrder_price(results.getDouble(8));
                orders.add(order);
            }
            return orders;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method insert one order to the database.
     * @param product_id    Product id.
     * @param user_id       Users id.
     * @param order_date    Order date.
     * @param order_status  Order status.
     * @return boolean      Returns true or false.
     * @since                   1.0.0
     */
    public boolean insertNewOrder(int product_id, int user_id, String order_date, String order_status) {

        String sql = "INSERT INTO " + TABLE_ORDERS + " ("
                + COLUMN_ORDERS_PRODUCT_ID + ", "
                + COLUMN_ORDERS_USER_ID + ", "
                + COLUMN_ORDERS_ORDER_DATE + ", "
                + COLUMN_ORDERS_ORDER_STATUS +
                ") VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, product_id);
            statement.setInt(2, user_id);
            statement.setString(3, order_date);
            statement.setString(4, order_status);

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }
    // END ORDERS QUERIES

    /**
     * This method counts all the products on the database.
     * @return int      Returns count of the products.
     * @since                   1.0.0
     */
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

    /**
     * This method counts all the simple users on the database.
     * @return int      Returns count of the simple users.
     * @since                   1.0.0
     */
    public Integer countAllCustomers() {
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery("SELECT COUNT(*) FROM " + TABLE_USERS +
                 " WHERE " + COLUMN_USERS_ADMIN + "= 0"
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

    /**
     * This method counts all the orders on the database.
     * @param user_id       Provided user id.
     * @return int      Returns count of the orders.
     * @since                   1.0.0
     */
    public Integer countUserOrders(int user_id) {

        try (PreparedStatement statement = conn.prepareStatement(String.valueOf("SELECT COUNT(*) FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDERS_USER_ID + "= ?"))) {
            statement.setInt(1, user_id);
            ResultSet results = statement.executeQuery();

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















