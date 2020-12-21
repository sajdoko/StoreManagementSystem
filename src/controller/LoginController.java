package controller;

import utils.ConnectionUtil;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
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

import javax.xml.soap.Text;

public class LoginController implements Initializable {
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

//    @FXML
//    private Text actionTarget;

//    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
//        System.out.println(usernameField.getText());
//        if (usernameField.getText().trim().length() > 0 && passwordField.getText().trim().length() > 0) {
//            actionTarget.setText("Credentials entered!");
//        } else {
//            actionTarget.setText("Enter credentials!");
//        }
//    }

    Stage dialogStage = new Stage();
    Scene scene;

    Connection connection = ConnectionUtil.getConnection();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

//    public void FXMLDocumentController() {
//        connection = ConnectionUtil.getConnection();
//    }
//


    public void handleSubmitButtonAction(ActionEvent event){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        String sql = "SELECT * FROM users WHERE email = ? and password = ?";

        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                infoBox("Please enter correct Email and Password", null, "Failed");
            }else{
//                infoBox("Login Successful",null,"Success" );
                Node node = (Node)event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                scene = new Scene(FXMLLoader.load(getClass().getResource("../view/main-dashboard.fxml")));
                dialogStage.setScene(scene);
                dialogStage.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    public static void infoBox(String infoMessage, String headerText, String title){
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