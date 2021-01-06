package controller;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import app.utils.PasswordUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

public class LoginController implements Initializable {
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

    Stage dialogStage = new Stage();
    Scene scene;

    public void handleLoginButtonAction(ActionEvent event) throws SQLException, IOException {
        String email = emailField.getText();
        String providedPassword = passwordField.getText();

        if ((email == null || email.isEmpty()) || (providedPassword == null || providedPassword.isEmpty())) {
            alertBox("Please enter the Email and Password", null, "Login Failed!");
        } else {

            User user = model.Datasource.getInstance().logIn(email);
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                alertBox("There is no user registered with that email address!", null, "Login Failed!");
            } else {
                boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, user.getPassword(), user.getSalt());

                if (passwordMatch) {
                    new UserSessionController(
                            (int) user.getId(),
                            user.getFullname(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getAdmin(),
                            user.getStatus()
                    );

                    Node node = (Node) event.getSource();
                    dialogStage = (Stage) node.getScene().getWindow();
                    dialogStage.close();
                    scene = new Scene(FXMLLoader.load(getClass().getResource("../view/main-dashboard.fxml")));
                    dialogStage.setScene(scene);
                    dialogStage.show();
                } else {
                    alertBox("Please enter correct Email and Password", null, "Login Failed!");
                }
            }
        }
    }

    public static void alertBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }
}
