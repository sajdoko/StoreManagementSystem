package controller.admin.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Order;
import model.Datasource;

/**
 * This class handles the admin orders page.
 * @author      Sajmir Doko
 */
public class OrdersController {

    @FXML
    private TableView<Order> tableOrdersPage;

    /**
     * This method lists all the orders to the view table.
     * It starts a new Task, gets all the products from the database then bind the results to the view.
     * @since       1.0.0
     */
    @FXML
    public void listOrders() {
        Task<ObservableList<Order>> getAllOrdersTask = new Task<ObservableList<Order>>() {
            @Override
            protected ObservableList<Order> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllOrders(Datasource.ORDER_BY_NONE));
            }
        };
        tableOrdersPage.itemsProperty().bind(getAllOrdersTask.valueProperty());
        new Thread(getAllOrdersTask).start();
    }

    public void btnOrdersSearchOnAction(ActionEvent actionEvent) {
        // TODO
        //  Add orders search functionality.
        System.out.println("TODO: Add orders search functionality.");
    }
}
