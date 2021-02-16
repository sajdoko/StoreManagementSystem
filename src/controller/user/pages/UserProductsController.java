package controller.user.pages;

import app.utils.HelperMethods;
import controller.UserSessionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Datasource;
import model.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * This class handles the users products page.
 * @author      Sajmir Doko
 */
public class UserProductsController {


    @FXML
    public TextField fieldProductsSearch;

    @FXML
    private TableView<Product> tableProductsPage;

    /**
     * This method lists all the product to the view table.
     * It starts a new Task, gets all the products from the database then bind the results to the view.
     * @since                   1.0.0
     */
    @FXML
    public void listProducts() {

        Task<ObservableList<Product>> getAllProductsTask = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().getAllProducts(Datasource.ORDER_BY_NONE));
            }
        };

        tableProductsPage.itemsProperty().bind(getAllProductsTask.valueProperty());
        addActionButtonsToTable();
        new Thread(getAllProductsTask).start();

    }

    /**
     * This private method adds the action buttons to the table rows.
     * @since                   1.0.0
     */
    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnBuy = new TableColumn("Actions");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {

                    private final Button buyButton = new Button("Buy");

                    {
                        buyButton.getStyleClass().add("button");
                        buyButton.getStyleClass().add("xs");
                        buyButton.getStyleClass().add("success");
                        buyButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            if (productData.getQuantity() <= 0) {
                                HelperMethods.alertBox("You can't buy this product because there is no stock!", "", "No Stock");
                            } else {
                                btnBuyProduct(productData.getId(), productData.getName());
                                System.out.println("Buy Product");
                                System.out.println("product id: " + productData.getId());
                                System.out.println("product name: " + productData.getName());
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(buyButton);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsPane);
                        }
                    }
                };
            }
        };

        colBtnBuy.setCellFactory(cellFactory);

        tableProductsPage.getColumns().add(colBtnBuy);

    }

    /**
     * This private method handles the products search functionality.
     * It creates a new task, gets the search results from the database and binds them to the view table.
     * @since                   1.0.0
     */
    @FXML
    private void btnProductsSearchOnAction() {
        Task<ObservableList<Product>> searchProductsTask = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().searchProducts(fieldProductsSearch.getText().toLowerCase(), Datasource.ORDER_BY_NONE));
            }
        };
        tableProductsPage.itemsProperty().bind(searchProductsTask.valueProperty());

        new Thread(searchProductsTask).start();
    }


    /**
     * This private method handles the buy product functionality.
     * @param product_id        Product id.
     * @param product_name      Product name.
     * @since                   1.0.0
     */
    @FXML
    private void btnBuyProduct(int product_id, String product_name) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You are about to buy " + product_name);
        alert.setTitle("Buy product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                int user_id = UserSessionController.getUserId();
                String order_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String order_status = "Received";

                Task<Boolean> addProductTask = new Task<Boolean>() {
                    @Override
                    protected Boolean call() {
                        return Datasource.getInstance().insertNewOrder(product_id, user_id, order_date, order_status);
                    }
                };

                addProductTask.setOnSucceeded(e -> {
                    if (addProductTask.valueProperty().get()) {
                        Datasource.getInstance().decreaseStock(product_id);
                        System.out.println("Order placed!");
                    }
                });

                new Thread(addProductTask).start();
                System.out.println(product_id);
            }
        }

    }

}