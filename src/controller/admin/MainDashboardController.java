package controller.admin;

import controller.UserSessionController;
import controller.admin.pages.CustomersController;
import controller.admin.pages.HomeController;
import controller.admin.pages.OrdersController;
import controller.admin.pages.products.ProductsController;
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

/**
 * This class handles the admin user dashboard interactions.
 * @author      Sajmir Doko
 */
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


    /**
     * This method handles the Home button click.
     * It loads the home page and it's contents.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnHomeOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/home/home.fxml");
        HomeController homeController = fxmlLoader.getController();
        homeController.getDashboardProdCount();
        homeController.getDashboardCostCount();
    }

    /**
     * This method handles the products button click.
     * It loads the products page and it's contents.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnProductsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/products/products.fxml");
        ProductsController controller = fxmlLoader.getController();
        controller.listProducts();
    }

    /**
     * This method handles the orders button click.
     * It loads the orders page and it's contents.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnOrdersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/orders/orders.fxml");
        OrdersController orders = fxmlLoader.getController();
        orders.listOrders();
    }

    /**
     * This method customers the Home button click.
     * It loads the customers page and it's contents.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnCustomersOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/customers/customers.fxml");
        CustomersController controller = fxmlLoader.getController();
        controller.listCustomers();
    }

    /**
     * This method handles the settings button click.
     * @param actionEvent       Accepts ActionEvent.
     * @since                   1.0.0
     */
    public void btnSettingsOnClick(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/settings/settings.fxml");
    }

    /**
     * This method handles the LogOut button click.
     * On click and confirmation it opens the login view and clears the user session instance.
     * @param actionEvent       Accepts ActionEvent.
     * @throws IOException      If an input or output exception occurred.
     * @since                   1.0.0
     */
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

    /**
     * This private helper method loads the view file.
     * @param view_path         Accepts path of view file.
     * @since                   1.0.0
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsrName.setText(UserSessionController.getUserFullName());

        FXMLLoader fxmlLoader = loadFxmlPage("/view/admin/pages/home/home.fxml");
        HomeController homeController = fxmlLoader.getController();
        homeController.getDashboardProdCount();
        homeController.getDashboardCostCount();
    }
}
