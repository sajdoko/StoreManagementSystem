package controller.admin;

import controller.UserSessionController;
import controller.admin.pages.CustomersController;
import controller.admin.pages.HomeController;
import controller.admin.pages.OrdersController;
import controller.admin.pages.ProductsController;
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
    @FXML
    public Button btnHome;
    @FXML
    public Button btnProducts;
    @FXML
    public Button btnCustomers;
    @FXML
    public Button btnOrders;
    @FXML
    public Button btnSettings;
    @FXML
    public Button lblLogOut;
    @FXML
    public AnchorPane dashHead;
    @FXML
    private StackPane dashContent;
    @FXML
    private Label lblUsrName;


    public void btnHomeOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/home/home.fxml");
        HomeController homeController = fxmlLoader.getController();
        homeController.getDashboardProdCount();
        homeController.getDashboardCostCount();
    }

    public void btnProductsOnClick(ActionEvent actionEvent) {

        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/products/products.fxml");

        ProductsController controller = fxmlLoader.getController();
        controller.listProducts();
    }

    public void btnOrdersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/orders/orders.fxml");

        OrdersController orders = fxmlLoader.getController();
        orders.listOrders();
    }

    public void btnCustomersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/customers/customers.fxml");

        CustomersController controller = fxmlLoader.getController();
        controller.listCustomers();
    }

    public void btnSettingsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/settings/settings.fxml");
    }

    public void btnLogOutOnClick(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure that you want to log out?");
        alert.setTitle("Log Out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            UserSessionController.cleanUserSession();
            Stage dialogStage;
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

        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/home/home.fxml");
        HomeController homeController = fxmlLoader.getController();
        homeController.getDashboardProdCount();
        homeController.getDashboardCostCount();
    }
}
