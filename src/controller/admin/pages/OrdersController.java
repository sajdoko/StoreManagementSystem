package controller.admin.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Order;
import model.Datasource;

public class OrdersController {

    @FXML
    private TableView<Order> tableOrdersPage;

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
    }
}
