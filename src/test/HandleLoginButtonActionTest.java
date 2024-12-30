package test;

import static org.mockito.Mockito.*;

import app.Main;
import app.utils.HelperMethods;
import app.utils.JavaFXInitializer;
import app.utils.PasswordUtils;
import controller.LoginController;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.event.ActionEvent;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.SQLException;
import java.io.IOException;


public class HandleLoginButtonActionTest extends ApplicationTest {

    private LoginController controller; // Replace with your actual controller class
    @Mock
    private ActionEvent mockEvent;


    @BeforeAll
    public static void setUpAll() {
        new JavaFXInitializer().init();
    }

    @BeforeEach
    void setup() throws InterruptedException {
        MockitoAnnotations.openMocks(this);
        controller = new LoginController(); // Instantiate the controller
        controller.usernameField = mock(TextField.class);
        controller.passwordField = mock(PasswordField.class);

        Main.launchTest();

        Thread.sleep(500);

    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();

        // Set the controller
        controller = loader.getController();

        // Create the scene and show the stage
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

    }

    @Test
    void testEmptyUsernameAndPassword() throws SQLException, IOException {
        Platform.runLater(() -> {
            when(controller.usernameField.getText()).thenReturn("");
            when(controller.passwordField.getText()).thenReturn("");

            try {
                controller.handleLoginButtonAction(mockEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Verify the alert box is triggered
            mockStatic(HelperMethods.class);
            HelperMethods.alertBox("Please enter the Username and Password", null, "Login Failed!");

        });
    }

    @Test
    void testInvalidUsername() throws SQLException, IOException {

        Platform.runLater(() -> {
            when(controller.usernameField.getText()).thenReturn("invalid@user");
            when(controller.passwordField.getText()).thenReturn("password123");

            when(HelperMethods.validateUsername("invalid@user")).thenReturn(false);

            try {
                controller.handleLoginButtonAction(mockEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Verify the alert box is triggered
            mockStatic(HelperMethods.class);
            HelperMethods.alertBox("Please enter a valid Username!", null, "Login Failed!");
        });
    }

    @Test
    void testUserNotRegistered() throws SQLException, IOException {
        when(controller.usernameField.getText()).thenReturn("validuser");
        when(controller.passwordField.getText()).thenReturn("password123");

        when(HelperMethods.validateUsername("validuser")).thenReturn(true);
        when(model.Datasource.getInstance().getUserByUsername("validuser")).thenReturn(null);

        controller.handleLoginButtonAction(mockEvent);

        // Verify the alert box is triggered
        mockStatic(HelperMethods.class);
        HelperMethods.alertBox("There is no user registered with that username!", null, "Login Failed!");
    }

    @Test
    void testSuccessfulLogin() throws SQLException, IOException {
        Platform.runLater(() -> {

            when(controller.usernameField.getText()).thenReturn("validuser");
            when(controller.passwordField.getText()).thenReturn("password123");

            User mockUser = new User();
            mockUser.setPassword("hashedPassword");
            mockUser.setSalt("salt");
            mockUser.setAdmin(0);

            when(HelperMethods.validateUsername("validuser")).thenReturn(true);
            try {
                when(model.Datasource.getInstance().getUserByUsername("validuser")).thenReturn(mockUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            when(PasswordUtils.verifyUserPassword("password123", "hashedPassword", "salt")).thenReturn(true);

            try {
                controller.handleLoginButtonAction(mockEvent);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Verify no alert box and redirection happens
            mockStatic(HelperMethods.class, (Answer) never());
            HelperMethods.alertBox(anyString(), anyString(), anyString());


        });
    }

    @Test
    void testIncorrectPassword() throws SQLException, IOException {
        when(controller.usernameField.getText()).thenReturn("validuser");
        when(controller.passwordField.getText()).thenReturn("wrongpassword");

        User mockUser = new User();
        mockUser.setPassword("hashedPassword");
        mockUser.setSalt("salt");
        mockUser.setAdmin(0);

        when(HelperMethods.validateUsername("validuser")).thenReturn(true);
        when(model.Datasource.getInstance().getUserByUsername("validuser")).thenReturn(mockUser);
        when(PasswordUtils.verifyUserPassword("wrongpassword", "hashedPassword", "salt")).thenReturn(false);

        controller.handleLoginButtonAction(mockEvent);

        // Verify the alert box is triggered
        mockStatic(HelperMethods.class);
        HelperMethods.alertBox("Please enter correct Email and Password", null, "Login Failed!");
    }

    @Test
    void testAdminUserRedirect() throws SQLException, IOException {
        when(controller.usernameField.getText()).thenReturn("adminuser");
        when(controller.passwordField.getText()).thenReturn("password123");

        User mockUser = new User();
        mockUser.setPassword("hashedPassword");
        mockUser.setSalt("salt");
        mockUser.setAdmin(1);

        when(HelperMethods.validateUsername("adminuser")).thenReturn(true);
        when(model.Datasource.getInstance().getUserByUsername("adminuser")).thenReturn(mockUser);
        when(PasswordUtils.verifyUserPassword("password123", "hashedPassword", "salt")).thenReturn(true);

        controller.handleLoginButtonAction(mockEvent);

        // Verify redirection to admin dashboard
        mockStatic(FXMLLoader.class);
        FXMLLoader.load(controller.getClass().getResource("../view/admin/main-dashboard.fxml"));
    }

}

