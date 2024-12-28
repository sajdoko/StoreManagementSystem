package Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import app.utils.HelperMethods;
import app.utils.PasswordUtils;
import controller.LoginController;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.event.ActionEvent;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.sql.SQLException;
import java.io.IOException;

public class HandleLoginButtonActionTest {

    private LoginController controller;
    @Mock
    private ActionEvent mockEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller=new LoginController();
        controller.usernameField=mock(TextField.class);
        controller.passwordField=mock(PasswordField.class);
    }

    @Test
    void testEmptyUsernameAndPassword()throws SQLException,IOException {
        when(controller.usernameField.getText()).thenReturn("");
        when(controller.passwordField.getText()).thenReturn("");

        controller.handleLoginButtonAction(mockEvent);

        verify(HelperMethods.class);
        HelperMethods.alertBox("Please enter the Username and Password",null,"Login Failed!");
    }

    @Test
    void testInvalidUsername()throws SQLException,IOException {
        when(controller.usernameField.getText()).thenReturn("invalid@user");
        when(controller.passwordField.getText()).thenReturn("password123");

        when(HelperMethods.validateUsername("invalid@user")).thenReturn(false);
        controller.handleLoginButtonAction(mockEvent);

        verify(HelperMethods.class);
        HelperMethods.alertBox("Please enter a valid Username",null,"Login Failed!");
    }

    @Test
    void testUserNotRegistered()throws SQLException,IOException {
        when(controller.usernameField.getText()).thenReturn("validuser");
        when(controller.passwordField.getText()).thenReturn("password123");

        when(HelperMethods.validateUsername("validuser")).thenReturn(true);
        when(model.Datasource.getInstance().getUserByUsername("validuser")).thenReturn(null);
        controller.handleLoginButtonAction(mockEvent);
        verify(HelperMethods.class);
        HelperMethods.alertBox("There is no user registered with that username",null,"Login Failed!");
    }

    @Test
    void testSuccessfulLogin()throws SQLException,IOException {
        when(controller.usernameField.getText()).thenReturn("validuser");
        when(controller.passwordField.getText()).thenReturn("password123");

        User mockUser=new User();
        mockUser.setPassword("hashedPassword");
        mockUser.setSalt("salt");
        mockUser.setAdmin(0);

        when(HelperMethods.validateUsername("validuser")).thenReturn(true);
        when(model.Datasource.getInstance().getUserByUsername("validuser")).thenReturn(mockUser);
        when(PasswordUtils.verifyUserPassword("password123","hashedPassword","salt")).thenReturn(true);

        controller.handleLoginButtonAction(mockEvent);

        verify(HelperMethods.class);
        HelperMethods.alertBox(anyString(),anyString(),anyString());

    }
}
