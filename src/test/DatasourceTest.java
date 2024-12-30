package test;

import model.Datasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import model.Customer;
import java.sql.*;
import java.util.List;

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
}










