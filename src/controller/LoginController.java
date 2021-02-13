package controller;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import app.utils.HelperMethods;
import app.utils.PasswordUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

public class LoginController implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    Stage dialogStage = new Stage();
    Scene scene;

    public void handleLoginButtonAction(ActionEvent event) throws SQLException, IOException {
        String username = usernameField.getText();
        String providedPassword = passwordField.getText();

        if ((username == null || username.isEmpty()) || (providedPassword == null || providedPassword.isEmpty())) {
            HelperMethods.alertBox("Please enter the Username and Password", null, "Login Failed!");
        } else {

            User user = model.Datasource.getInstance().getUserByUsername(username);
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                HelperMethods.alertBox("There is no user registered with that username!", null, "Login Failed!");
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

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
