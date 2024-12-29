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
}
