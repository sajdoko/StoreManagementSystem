package test;

import app.utils.HelperMethods;
import controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginControllerTestFX extends ApplicationTest {

    private LoginController loginController;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();

        // Set the controller
        loginController = loader.getController();

        // Create the scene and show the stage
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

    }


    @Test
    void testInvalidUsername_ShouldShowAlert() throws Exception {
        // Mock static HelperMethods.alertBox
        try (MockedStatic<HelperMethods> mockedHelperMethods = mockStatic(HelperMethods.class)) {
            // Set invalid username
            loginController.usernameField.setText("invalid_username!");
            loginController.passwordField.setText("password123");

            // Mock alertBox behavior
            mockedHelperMethods.when(() -> HelperMethods.alertBox(anyString(), any(), anyString()))
                    .then(invocation -> {
                        String message = invocation.getArgument(0);
                        Assertions.assertEquals("Please enter a valid Username!", message);
                        return null;
                    });

            // Run the login action
            interact(() -> {
                try {
                    loginController.handleLoginButtonAction(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Wait for FX thread events
            WaitForAsyncUtils.waitForFxEvents();

            // Verify that the alert box was called
            mockedHelperMethods.verify(() -> HelperMethods.alertBox(
                    eq("Please enter a valid Username!"), any(), eq("Login Failed!")
            ), times(1));
        }
    }
    @Test
    void testEmptyFields_ShouldShowAlert() throws Exception {
        interact(() -> {
            clickOn("#usernameField").write(""); // Empty username
            clickOn("#passwordField").write(""); // Empty password
            try {
                loginController.handleLoginButtonAction(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Ensure FX thread completes processing
        WaitForAsyncUtils.waitForFxEvents();

        // Assertions for alerts or other components
    }

}
