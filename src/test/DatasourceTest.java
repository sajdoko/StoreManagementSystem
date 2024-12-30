package test;

import model.Datasource;
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

