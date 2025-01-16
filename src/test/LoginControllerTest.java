package test;

import static org.mockito.Mockito.*;

import app.utils.HelperMethods;
import controller.LoginController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

//class LoginControllerTest {
//
//    private LoginController loginController;
//
//    @Mock
//    private ActionEvent mockEvent;
//    @Mock
//    private Node mockNode;
//
//    @BeforeAll
//    static void initJavaFX() throws InterruptedException {
//        // Initialize JavaFX runtime
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.startup(() -> {
//            new JFXPanel(); // Initialize JavaFX runtime
//            latch.countDown();
//        });
//        if (!latch.await(5, TimeUnit.SECONDS)) {
//            throw new RuntimeException("JavaFX initialization timed out.");
//        }
//    }
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        loginController = new LoginController();
//        loginController.usernameField = new TextField();
//        loginController.passwordField = new PasswordField();
//
//        // Mock event source
//        when(mockEvent.getSource()).thenReturn(mockNode);
//        when(mockNode.getScene()).thenReturn(new Scene(new Node() {}.getParent()));
//    }
//
//    @Test
//    void testHandleLoginButtonAction_EmptyFields() throws InterruptedException {
//        // Arrange: Empty username and password
//        loginController.usernameField.setText("");
//        loginController.passwordField.setText("");
//
//        CountDownLatch latch = new CountDownLatch(1);
//
//        // Act: Run the method on the JavaFX thread
//        Platform.runLater(() -> {
//            try {
//                loginController.handleLoginButtonAction(mockEvent);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            } finally {
//                latch.countDown();
//            }
//        });
//
//        // Wait for JavaFX thread completion
//        latch.await(5, TimeUnit.SECONDS);
//
//        // Assert: Verify the alert box is called
//        //verifyStatic(HelperMethods.class, times(1));
//        HelperMethods.alertBox(eq("Please enter the Username and Password"), isNull(), eq("Login Failed!"));
//    }
//
//    @Test
//    void testHandleLoginButtonAction_InvalidUsername() throws InterruptedException {
//        // Arrange: Invalid username
//        loginController.usernameField.setText("invalid!");
//        loginController.passwordField.setText("password123");
//
//        CountDownLatch latch = new CountDownLatch(1);
//
//        // Mock HelperMethods.validateUsername behavior
//        mockStatic(HelperMethods.class);
//        when(HelperMethods.validateUsername(anyString())).thenReturn(false);
//
//        // Act: Run the method on the JavaFX thread
//        Platform.runLater(() -> {
//            try {
//                loginController.handleLoginButtonAction(mockEvent);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            } finally {
//                latch.countDown();
//            }
//        });
//
//        // Wait for JavaFX thread completion
//        latch.await(5, TimeUnit.SECONDS);
//
//        // Assert: Verify the alert box is called
//     //   verifyStatic(HelperMethods.class, times(1));
//        HelperMethods.alertBox(eq("Please enter a valid Username!"), isNull(), eq("Login Failed!"));
//    }
//}
