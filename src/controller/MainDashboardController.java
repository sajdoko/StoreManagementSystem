package controller;

import controller.pages.ProductsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainDashboardController implements Initializable {
    public Button btnHome;
    public Button btnProducts;
    public Button btnCustomers;
    public Button btnOrders;
    public Button btnSettings;
    public Button lblLogOut;
    @FXML
    private StackPane acContent;
    @FXML
    private Label lblUsrName;


    public void btnHomeOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/pages/home/home.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane root = fxmlLoader.getRoot();
        acContent.getChildren().clear();
        acContent.getChildren().add(root);

    }

    public void btnProductsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/pages/products/products.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        acContent.getChildren().clear();
        acContent.getChildren().add(root);

        ProductsController controller = fxmlLoader.getController();
        controller.listProducts();
    }

    public void btnOrdersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/pages/orders/orders.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane root = fxmlLoader.getRoot();
        acContent.getChildren().clear();
        acContent.getChildren().add(root);
    }

    public void btnCustomersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/pages/customers/customers.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane root = fxmlLoader.getRoot();
        acContent.getChildren().clear();
        acContent.getChildren().add(root);
    }

    public void btnSettingsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/pages/settings/settings.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane root = fxmlLoader.getRoot();
        acContent.getChildren().clear();
        acContent.getChildren().add(root);
    }

    public void btnLogOutOnClick(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure that you want to log out?");
        alert.setTitle("Log Out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            UserSessionController.cleanUserSession();
            Stage dialogStage;
            dialogStage = new Stage();
            Node node = (Node) actionEvent.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/login.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsrName.setText(UserSessionController.getUserFullName());
    }
}
