package test;

import app.utils.JavaFXInitializer;
import controller.RegisterController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Datasource;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @Mock
    private TextField fullNameField;
    @Mock
    private TextField usernameField;
    @Mock
    private TextField emailField;
    @Mock
    private PasswordField passwordField;
    @Mock
    private ActionEvent actionEvent;

    private RegisterController registerController;

    @BeforeAll
    public static void setUpAll() {
        new JavaFXInitializer().init();
    }

    @BeforeEach
    public void setUp() throws InterruptedException {
        MockitoAnnotations.openMocks(this);
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            registerController = new RegisterController();
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void handleRegisterButtonAction_test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Mock user input
            when(fullNameField.getText()).thenReturn("John Doe");
            when(usernameField.getText()).thenReturn("johndoe");
            when(emailField.getText()).thenReturn("johndoe@example.com");
            when(passwordField.getText()).thenReturn("password123");

            // Mock Datasource methods
            Datasource datasource = mock(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);
            try {
                when(datasource.getUserByUsername("johndoe")).thenReturn(new User());
                when(datasource.getUserByEmail("johndoe@example.com")).thenReturn(new User());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Call the method to be tested
            try {
                registerController.handleRegisterButtonAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Verify interactions
            try {
                verify(datasource, times(1)).getUserByUsername("johndoe");
                verify(datasource, times(1)).getUserByEmail("johndoe@example.com");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}