package controller.user;

import controller.UserSessionController;
import controller.user.pages.UserHomeController;
import controller.user.pages.UserProductsController;
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

public class UserMainDashboardController implements Initializable {
    public Button btnHome;
    public Button btnProducts;
    public Button btnOrders;
    public Button lblLogOut;
    public AnchorPane dashHead;
    @FXML
    private StackPane dashContent;
    @FXML
    private Label lblUsrName;


    public void btnHomeOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/home/home.fxml");
        UserHomeController homeController = fxmlLoader.getController();
        homeController.getDashboardProdCount();
        homeController.getDashboardCostCount();
    }

    public void btnOrdersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/orders/orders.fxml");
    }

    public void btnSettingsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/settings/settings.fxml");
    }

    public void btnProductsOnClick(ActionEvent actionEvent) {

        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/products/products.fxml");

        UserProductsController userController = fxmlLoader.getController();
        userController.listProducts();
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
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
        }
    }

    private FXMLLoader loadFxmlPage(String view_path) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource(view_path).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnchorPane root = fxmlLoader.getRoot();
        dashContent.getChildren().clear();
        dashContent.getChildren().add(root);

        return fxmlLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsrName.setText(UserSessionController.getUserFullName());

        FXMLLoader fxmlLoader = loadFxmlPage("/view/user/pages/home/home.fxml");
        UserHomeController homeController = fxmlLoader.getController();
        homeController.getDashboardProdCount();
        homeController.getDashboardCostCount();
    }
}
