package controller.user.pages;

import controller.UserSessionController;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import model.Datasource;

/**
 * This class handles the users home page.
 * @author      Sajmir Doko
 */
public class UserHomeController {

    public Label productsCount;
    public Label ordersCount;

    /**
     * This method gets the products count for the user dashboard and sets it to the productsCount label.
     * @since                   1.0.0
     */
    public void getDashboardProdCount() {
        Task<Integer> getDashProdCount = new Task<Integer>() {
            @Override
            protected Integer call() {
                return Datasource.getInstance().countAllProducts();
            }
        };

        getDashProdCount.setOnSucceeded(e -> {
            productsCount.setText(String.valueOf(getDashProdCount.valueProperty().getValue()));
        });

        new Thread(getDashProdCount).start();
    }

    /**
     * This method gets the orders count for the user dashboard and sets it to the ordersCount label.
     * @since                   1.0.0
     */
    public void getDashboardOrdersCount() {
        Task<Integer> getDashOrderCount = new Task<Integer>() {
            @Override
            protected Integer call() {
                return Datasource.getInstance().countUserOrders(UserSessionController.getUserId());
            }
        };

        getDashOrderCount.setOnSucceeded(e -> {
            ordersCount.setText(String.valueOf(getDashOrderCount.valueProperty().getValue()));
        });

        new Thread(getDashOrderCount).start();
    }

}
