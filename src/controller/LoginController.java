package controller;


import java.io.IOException;
import java.sql.SQLException;

import app.utils.HelperMethods;
import app.utils.PasswordUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

/**
 * This class handles the login operations of the application.
 * @author      Sajmir Doko
 */
public class LoginController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    Stage dialogStage = new Stage();
    Scene scene;

    /**
     * This method handles the login button action event.
     * It gets the user entered data and makes the proper validations.
     * If the entered details are correct, it creates an new UserSessionController instance
     * and transitions the user screen to the appropriate dashboard.
     * @param event             Accepts ActionEvent.
     * @throws IOException      If an input or output exception occurred.
     * @throws SQLException     If an SQL error occurred.
     * @since                   1.0.0
     */
    public void handleLoginButtonAction(ActionEvent event) throws SQLException, IOException {
        String username = usernameField.getText();
        String providedPassword = passwordField.getText();

        if ((username == null || username.isEmpty()) || (providedPassword == null || providedPassword.isEmpty())) {
            HelperMethods.alertBox("Please enter the Username and Password", null, "Login Failed!");
        } else if (!HelperMethods.validateUsername(username)) {
            HelperMethods.alertBox("Please enter a valid Username!", null, "Login Failed!");
        } else {

            User user = model.Datasource.getInstance().getUserByUsername(username);
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                HelperMethods.alertBox("There is no user registered with that username!", null, "Login Failed!");
            } else {
                boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, user.getPassword(), user.getSalt());

                if (passwordMatch) {
                    UserSessionController.setUserId(user.getId());
                    UserSessionController.setUserFullName(user.getFullname());
                    UserSessionController.setUserName(user.getUsername());
                    UserSessionController.setUserEmail(user.getEmail());
                    UserSessionController.setUserAdmin(user.getAdmin());
                    UserSessionController.setUserStatus(user.getStatus());

                    Node node = (Node) event.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    if (user.getAdmin() == 0) {
                        scene = new Scene(FXMLLoader.load(getClass().getResource("../view/user/main-dashboard.fxml")));
                    } else if (user.getAdmin() == 1) {
                        scene = new Scene(FXMLLoader.load(getClass().getResource("../view/admin/main-dashboard.fxml")));
                    }
                    dialogStage.setScene(scene);
                    dialogStage.show();
                } else {
                    HelperMethods.alertBox("Please enter correct Email and Password", null, "Login Failed!");
                }
            }
        }
    }

    /**
     * This method handles the register button action event.
     * It transfers the user screen to the registration view.
     * @param actionEvent       Accepts ActionEvent.
     * @throws IOException      If an input or output exception occurred.
     * @since                   1.0.0
     */
    public void handleRegisterButtonAction(ActionEvent actionEvent) throws IOException {
        Stage dialogStage;
        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/register.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }
}
