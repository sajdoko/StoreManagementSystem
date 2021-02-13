package controller;

import app.utils.HelperMethods;
import app.utils.PasswordUtils;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Datasource;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    public TextField fullNameField;
    public TextField usernameField;
    public TextField emailField;
    public PasswordField passwordField;

    Stage dialogStage = new Stage();
    Scene scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleLoginButtonAction(ActionEvent actionEvent) throws IOException {
        Stage dialogStage;
        Node node = (Node) actionEvent.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    public void handleRegisterButtonAction(ActionEvent actionEvent) throws SQLException {
        String validationErrors = "";
        boolean errors = false;
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String providedPassword = passwordField.getText();

        if (fullName == null || fullName.isEmpty()) {
            validationErrors += "Please enter your Name and Surname! \n";
            errors = true;
        }

        if (username == null || username.isEmpty()) {
            validationErrors += "Please enter a username! \n";
            errors = true;
        } else {
            User userByUsername = model.Datasource.getInstance().getUserByUsername(username);
            if (userByUsername.getUsername() != null) {
                validationErrors += "There is already a user registered with this username! \n";
                errors = true;
            }
        }

        if (email == null || email.isEmpty() || !HelperMethods.validateEmail(email)) {
            validationErrors += "Please enter an email address! \n";
            errors = true;
        } else {
            User userByEmail = model.Datasource.getInstance().getUserByEmail(email);
            if (userByEmail.getEmail() != null) {
                validationErrors += "There is already a user registered with this email address! \n";
                errors = true;
            }
        }

        if (providedPassword == null || providedPassword.isEmpty()) {
            validationErrors += "Please enter the password! \n";
            errors = true;
        } else {
            if (!HelperMethods.passwordValidation(providedPassword)) {
                validationErrors += "Password must be at least 6 and maximum 16 characters! \n";
                errors = true;
            }
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
                    assert user != null;
                    new UserSessionController(
                            (int) user.getId(),
                            user.getFullname(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getAdmin(),
                            user.getStatus()
                    );

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
