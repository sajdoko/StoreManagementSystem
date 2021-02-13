package controller.user.pages;

import controller.UserSessionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import model.Datasource;
import model.Order;

import java.sql.SQLException;

public class UserOrdersController {
    public TableView tableOrdersPage;

    @FXML
    public void listOrders() {

        Task<ObservableList<Order>> getAllOrdersTask = new Task<ObservableList<Order>>() {
            @Override
            protected ObservableList<Order> call() throws SQLException {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllUserOrders(Datasource.ORDER_BY_NONE, UserSessionController.getUserId()));
            }
        };

        tableOrdersPage.itemsProperty().bind(getAllOrdersTask.valueProperty());
        new Thread(getAllOrdersTask).start();

    }
    public void btnOrdersSearchOnAction(ActionEvent actionEvent) {
    }
}
