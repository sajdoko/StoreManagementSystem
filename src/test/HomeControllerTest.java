package test;


import app.utils.JavaFXInitializer;
import controller.admin.pages.HomeController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import model.Datasource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

//test for getDashboardCostCount method
//has dependency to countAllCustomers method in Datasource class
public class HomeControllerTest {
    @Mock
    private Label customersCount;

    @InjectMocks
    private HomeController homeController;

    @BeforeAll
    public static void setUpAll(){
        new JavaFXInitializer().init();
    }

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getDashboardCostCount_test(){
        Platform.runLater(() -> {
            Datasource datasource = mock(Datasource.class);
            when(datasource.countAllCustomers()).thenReturn(10);
            mockStatic(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);

            homeController.getDashboardCostCount();

            verify(customersCount, timeout(1000)).setText("10");
        });
    }

    @Test
    public void getDashboardCostCount_boundaryValues_test(){
        Platform.runLater(() -> {
            Datasource datasource = mock(Datasource.class);
            mockStatic(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);

            // 0 customers
            when(datasource.countAllCustomers()).thenReturn(0);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("0");

            // 1 customer
            when(datasource.countAllCustomers()).thenReturn(1);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("1");

            // Integer.MAX_VALUE customers
            int largeNumber = Integer.MAX_VALUE;
            when(datasource.countAllCustomers()).thenReturn(largeNumber);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText(String.valueOf(largeNumber));
        });
    }

    @Test
    public void getDashboardCostCount_classEvaluation_test(){
        Platform.runLater(() -> {
            Datasource datasource = mock(Datasource.class);
            mockStatic(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);

            //must throw exception
            when(datasource.countAllCustomers()).thenReturn(-1);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("-1");
        });
    }

    @Test
    public void getDashboardCostCount_branchCoverage_test() {
        Platform.runLater(() -> {
            Datasource datasource = mock(Datasource.class);
            mockStatic(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);

            // Branch where countAllCustomers returns 0
            when(datasource.countAllCustomers()).thenReturn(0);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("0");

            // Branch where countAllCustomers returns a positive number
            when(datasource.countAllCustomers()).thenReturn(10);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("10");
        });
    }

    @Test
    public void getDashboardCostCount_conditionCoverage_test() {
        Platform.runLater(() -> {
            Datasource datasource = mock(Datasource.class);
            mockStatic(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);

            // Condition where countAllCustomers returns 0
            when(datasource.countAllCustomers()).thenReturn(0);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("0");

            // Condition where countAllCustomers returns a positive number
            when(datasource.countAllCustomers()).thenReturn(10);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("10");

            // Condition where countAllCustomers returns a negative number
            when(datasource.countAllCustomers()).thenReturn(-1);
            homeController.getDashboardCostCount();
            verify(customersCount, timeout(1000)).setText("-1");
        });
    }
}
