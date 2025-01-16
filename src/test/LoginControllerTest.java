package test;


import static org.mockito.Mockito.*;
import app.utils.HelperMethods;
import controller.LoginController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


//test for handleRegisterButtonAction method
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

    @Test
    public void handleRegisterButtonAction_boundaryValues_test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Datasource datasource = mock(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);

            // Empty Strings
            when(fullNameField.getText()).thenReturn("");
            when(usernameField.getText()).thenReturn("");
            when(emailField.getText()).thenReturn("");
            when(passwordField.getText()).thenReturn("");
            try {
                registerController.handleRegisterButtonAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Minimum length strings
            when(fullNameField.getText()).thenReturn("a");
            when(usernameField.getText()).thenReturn("a");
            when(emailField.getText()).thenReturn("a@b.com");
            when(passwordField.getText()).thenReturn("a");

            // Maximum length strings
            String maxLengthString = "a".repeat(255);
            when(fullNameField.getText()).thenReturn(maxLengthString);
            when(usernameField.getText()).thenReturn(maxLengthString);
            when(emailField.getText()).thenReturn(maxLengthString + "@example.com");
            try{
                registerController.handleRegisterButtonAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void handleRegisterButtonAction_coverage_test() throws SQLException, IOException, InterruptedException{
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Datasource datasource = mock(Datasource.class);
            when(Datasource.getInstance()).thenReturn(datasource);

            // Valid input
            when(fullNameField.getText()).thenReturn("John Doe");
            when(usernameField.getText()).thenReturn("johndoe");
            when(emailField.getText()).thenReturn("johndoe@example.com");
            when(passwordField.getText()).thenReturn("password123");
            try {
                when(datasource.getUserByUsername("johndoe")).thenReturn(null);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                when(datasource.getUserByEmail("johndoe@example.xom")).thenReturn(null);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                registerController.handleRegisterButtonAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                verify(datasource, times(1)).getUserByUsername("johndoe");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                verify(datasource, times(1)).getUserByEmail("johndoe@example.com");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Test with existing username
            try {
                when(datasource.getUserByUsername("johndoe")).thenReturn(new User());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                registerController.handleRegisterButtonAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                verify(datasource, times(2)).getUserByUsername("johndoe");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Test with existing email
            try {
                when(datasource.getUserByUsername("johndoe")).thenReturn(null);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                when(datasource.getUserByEmail("johndoe@example.com")).thenReturn(new User());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                registerController.handleRegisterButtonAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                verify(datasource, times(3)).getUserByUsername("johndoe");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                verify(datasource, times(2)).getUserByEmail("johndoe@example.com");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Test with empty fields
            when(fullNameField.getText()).thenReturn("");
            when(usernameField.getText()).thenReturn("");
            when(emailField.getText()).thenReturn("");
            when(passwordField.getText()).thenReturn("");
            try {
                registerController.handleRegisterButtonAction(actionEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}
