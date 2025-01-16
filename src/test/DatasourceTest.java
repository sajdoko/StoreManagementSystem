package test;

import model.Datasource;
import model.Customer;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatasourceTest {

    private Datasource datasource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        datasource = Datasource.getInstance();

        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        datasource.open();
        datasource.conn = mockConnection;
    }

    @Test
    void testSearchCustomers_ValidSearchAscendingOrder() throws SQLException {
        String searchString = "Erdi";
        int sortOrder = Datasource.ORDER_BY_ASC;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("fullname")).thenReturn("Erdi Koci", "Erdi Ko");
        when(mockResultSet.getString("email")).thenReturn("erdi.koci@example.com", "erdi.ko@example.com");
        when(mockResultSet.getString("username")).thenReturn("erdik", "erdiko");
        when(mockResultSet.getInt("orders")).thenReturn(5, 3);
        when(mockResultSet.getString("status")).thenReturn("enabled", "disabled");

        List<Customer> customers = datasource.searchCustomers(searchString, sortOrder);

        assertNotNull(customers);
        assertEquals(2, customers.size());

        Customer customer1 = customers.getFirst();
        assertEquals(1, customer1.getId());
        assertEquals("Erdi Koci", customer1.getFullname());
        assertEquals("erdi.koci@example.com", customer1.getEmail());
        assertEquals("erdik", customer1.getUsername());
        assertEquals(5, customer1.getOrders());
        assertEquals("enabled", customer1.getStatus());

        Customer customer2 = customers.get(1);
        assertEquals(2, customer2.getId());
        assertEquals("Erdi Ko", customer2.getFullname());
        assertEquals("erdi.ko@example.com", customer2.getEmail());
        assertEquals("erdiko", customer2.getUsername());
        assertEquals(3, customer2.getOrders());
        assertEquals("disabled", customer2.getStatus());

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(3)).next(); // Two results + one false
    }

    @Test
    void testSearchCustomers_NoMatches() throws SQLException {
        String searchString = "NonExistent";
        int sortOrder = Datasource.ORDER_BY_NONE;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        List<Customer> customers = datasource.searchCustomers(searchString, sortOrder);

        assertNotNull(customers);
        assertTrue(customers.isEmpty());

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next(); // No results
    }

    @Test
    void testSearchCustomers_InvalidSortOrder() throws SQLException {
        String searchString = "Erdi";
        int sortOrder = 99;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        List<Customer> customers = datasource.searchCustomers(searchString, sortOrder);

        assertNotNull(customers);
        assertTrue(customers.isEmpty());

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
    }



    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private Datasource datasource;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        datasource = new Datasource(conn);

        when(conn.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    @DisplayName("Code Base Testing")
    void testSearchProductsWithValidSearchString() throws SQLException {
        String searchString = "Product";
        int sortOrder = Datasource.ORDER_BY_NONE;

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getString(2)).thenReturn("Product1");
        when(resultSet.getString(3)).thenReturn("Description1");
        when(resultSet.getDouble(4)).thenReturn(10.0);
        when(resultSet.getInt(5)).thenReturn(100);
        when(resultSet.getString(6)).thenReturn("Category1");
        when(resultSet.getInt(7)).thenReturn(5);

        List<Product> products = datasource.searchProducts(searchString, sortOrder);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product1", products.get(0).getName());
    }

    @Test
    @DisplayName("Boundary Value Testing")
    void testSearchProductsWithEmptySearchString() throws SQLException {
        String searchString = "";
        int sortOrder = Datasource.ORDER_BY_NONE;

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getString(2)).thenReturn("Product1");
        when(resultSet.getString(3)).thenReturn("Description1");
        when(resultSet.getDouble(4)).thenReturn(10.0);
        when(resultSet.getInt(5)).thenReturn(100);
        when(resultSet.getString(6)).thenReturn("Category1");
        when(resultSet.getInt(7)).thenReturn(5);

        List<Product> products = datasource.searchProducts(searchString, sortOrder);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product1", products.get(0).getName());
    }

    @Test
    @DisplayName("Boundary Value Testing")
    void testSearchProductsWithSpecialCharacters() throws SQLException {
        String searchString = "%$#";
        int sortOrder = Datasource.ORDER_BY_ASC;

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(2);
        when(resultSet.getString(2)).thenReturn("Product2");
        when(resultSet.getString(3)).thenReturn("Description2");
        when(resultSet.getDouble(4)).thenReturn(20.0);
        when(resultSet.getInt(5)).thenReturn(200);
        when(resultSet.getString(6)).thenReturn("Category2");
        when(resultSet.getInt(7)).thenReturn(10);

        List<Product> products = datasource.searchProducts(searchString, sortOrder);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product2", products.get(0).getName());
    }

    @Test
    @DisplayName("Code Base Testing")
    void testSearchProductsWithSQLException() throws SQLException {
        String searchString = "Product";
        int sortOrder = Datasource.ORDER_BY_ASC;

        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<Product> products = datasource.searchProducts(searchString, sortOrder);

        assertNull(products);
    }

    @Test
    @DisplayName("Code Base Testing")
    void testSearchProductsWithSortOrderNone() throws SQLException {
        String searchString = "Product";
        int sortOrder = Datasource.ORDER_BY_NONE;

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(3);
        when(resultSet.getString(2)).thenReturn("Product3");
        when(resultSet.getString(3)).thenReturn("Description3");
        when(resultSet.getDouble(4)).thenReturn(30.0);
        when(resultSet.getInt(5)).thenReturn(300);
        when(resultSet.getString(6)).thenReturn("Category3");
        when(resultSet.getInt(7)).thenReturn(15);

        List<Product> products = datasource.searchProducts(searchString, sortOrder);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product3", products.get(0).getName());
    }

    @Test
    @DisplayName("Code Base Testing")
    void testSearchProductsWithSortOrderAscending() throws SQLException {
        String searchString = "Product";
        int sortOrder = Datasource.ORDER_BY_ASC;

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(4);
        when(resultSet.getString(2)).thenReturn("Product4");
        when(resultSet.getString(3)).thenReturn("Description4");
        when(resultSet.getDouble(4)).thenReturn(40.0);
        when(resultSet.getInt(5)).thenReturn(400);
        when(resultSet.getString(6)).thenReturn("Category4");
        when(resultSet.getInt(7)).thenReturn(20);

        List<Product> products = datasource.searchProducts(searchString, sortOrder);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product4", products.get(0).getName());
    }

    @Test
    @DisplayName("Code Base Testing")
    void testSearchProductsWithSortOrderDescending() throws SQLException {
        String searchString = "Product";
        int sortOrder = Datasource.ORDER_BY_DESC;

        when(resultSet.next()).thenReturn(true).thenReturn(false); // Simulate one result
        when(resultSet.getInt(1)).thenReturn(5);
        when(resultSet.getString(2)).thenReturn("Product5");
        when(resultSet.getString(3)).thenReturn("Description5");
        when(resultSet.getDouble(4)).thenReturn(50.0);
        when(resultSet.getInt(5)).thenReturn(500);
        when(resultSet.getString(6)).thenReturn("Category5");
        when(resultSet.getInt(7)).thenReturn(25);

        List<Product> products = datasource.searchProducts(searchString, sortOrder);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product5", products.get(0).getName());
    }
}


