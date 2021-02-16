package controller;

import app.utils.HelperMethods;
import app.utils.PasswordUtils;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Datasource;
import model.User;

import java.io.IOException;
import java.sql.SQLException;


/**
 * This class handles the registration operations of the application.
 * @author      Sajmir Doko
 */
public class RegisterController {

    @FXML
    public TextField fullNameField;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

    Stage dialogStage = new Stage();
    Scene scene;

    /**
     * This method handles the login button action event.
     * It transfers the user screen to the login view.
     * @param actionEvent       Accepts ActionEvent.
     * @throws IOException      If an input or output exception occurred.
     * @since                   1.0.0
     */
    public void handleLoginButtonAction(ActionEvent actionEvent) throws IOException {
        Stage dialogStage;
        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    /**
     * This method handles the register button action event.
     * It gets the user entered data and makes the proper validations.
     * If the entered details are correct, it saves the user data to the database,
     * creates an new UserSessionController instance and transitions the user screen
     * to the appropriate dashboard.
     * @param actionEvent       Accepts ActionEvent.
     * @throws SQLException     If an SQL error occurred.
     * @since                   1.0.0
     */
    public void handleRegisterButtonAction(ActionEvent actionEvent) throws SQLException {
        String validationErrors = "";
        boolean errors = false;
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String providedPassword = passwordField.getText();

        // Validate Full Name
        if (fullName == null || fullName.isEmpty()) {
            validationErrors += "Please enter your Name and Surname! \n";
            errors = true;
        } else if (!HelperMethods.validateFullName(fullName)) {
            validationErrors += "Please enter a valid Name and Surname! \n";
            errors = true;
        }

        // Validate Username
        if (username == null || username.isEmpty()) {
            validationErrors += "Please enter a username! \n";
            errors = true;
        } else if (!HelperMethods.validateUsername(username)) {
            validationErrors += "Please enter a valid Username! \n";
            errors = true;
        } else {
            User userByUsername = model.Datasource.getInstance().getUserByUsername(username);
            if (userByUsername.getUsername() != null) {
                validationErrors += "There is already a user registered with this username! \n";
                errors = true;
            }
        }

        // Validate Email
        if (email == null || email.isEmpty()) {
            validationErrors += "Please enter an email address! \n";
            errors = true;
        } else if (!HelperMethods.validateEmail(email)) {
            validationErrors += "Please enter a valid Email address! \n";
            errors = true;
        } else {
            User userByEmail = model.Datasource.getInstance().getUserByEmail(email);
            if (userByEmail.getEmail() != null) {
                validationErrors += "There is already a user registered with this email address! \n";
                errors = true;
            }
        }

        // Validate Password
        if (providedPassword == null || providedPassword.isEmpty()) {
            validationErrors += "Please enter the password! \n";
            errors = true;
        } else if (!HelperMethods.validatePassword(providedPassword)){
            validationErrors += "Password must be at least 6 and maximum 16 characters! \n";
            errors = true;
        }

        if (errors) {
            HelperMethods.alertBox(validationErrors, null, "Registration Failed!");
        } else {

            String salt = PasswordUtils.getSalt(30);
            String securePassword = PasswordUtils.generateSecurePassword(providedPassword, salt);

            Task<Boolean> addUserTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().insertNewUser(fullName, username, email, securePassword, salt);
                }
            };

            addUserTask.setOnSucceeded(e -> {
                if (addUserTask.valueProperty().get()) {
                    User user = null;
                    try {
                        user = Datasource.getInstance().getUserByEmail(email);
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }

                    // Method invocation 'getId' may produce 'NullPointerException'
                    assert user != null;

                    UserSessionController.setUserId(user.getId());
                    UserSessionController.setUserFullName(user.getFullname());
                    UserSessionController.setUserName(user.getUsername());
                    UserSessionController.setUserEmail(user.getEmail());
                    UserSessionController.setUserAdmin(user.getAdmin());
                    UserSessionController.setUserStatus(user.getStatus());

                    Node node = (Node) actionEvent.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    if (user.getAdmin() == 0) {
                        try {
                            scene = new Scene(FXMLLoader.load(getClass().getResource("../view/user/main-dashboard.fxml")));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else if (user.getAdmin() == 1) {
                        try {
                            scene = new Scene(FXMLLoader.load(getClass().getResource("../view/admin/main-dashboard.fxml")));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    dialogStage.setScene(scene);
                    dialogStage.show();
                }
            });

            new Thread(addUserTask).start();

        }
    }
}
