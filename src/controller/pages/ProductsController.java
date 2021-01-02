package controller.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Datasource;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ProductsController {

    @FXML
    public TextField fieldProductsSearch;

    @FXML
    private TableView<Product> tableProductsPage;

    @FXML
    public void listProducts() {

        GetAllProductsTask getAllProductsTask = new GetAllProductsTask();

        tableProductsPage.itemsProperty().bind(getAllProductsTask.valueProperty());
        addActionButtonsToTable();

        new Thread(getAllProductsTask).start();
    }

    @FXML
    private void addActionButtonsToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {

                    private final Button viewButton = new Button("View");
                    {
                        viewButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            System.out.println("View Product");
                            System.out.println("product id: " + productData.getId());
                            System.out.println("product name: " + productData.getName());
                        });
                    }

                    private final Button editButton = new Button("Edit");
                    {
                        editButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            System.out.println("Edit Product");
                            System.out.println("product id: " + productData.getId());
                            System.out.println("product name: " + productData.getName());
                        });
                    }

                    private final Button deleteButton = new Button("Delete");
                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setHeaderText("Are you sure that you want to delete " + productData.getName() + " ?");
                            alert.setTitle("Delete " + productData.getName() + " ?");
                            Optional<ButtonType> deleteConfirmation = alert.showAndWait();

                            if (deleteConfirmation.get() == ButtonType.OK) {
                                System.out.println("Delete Product");
                                System.out.println("product id: " + productData.getId());
                                System.out.println("product name: " + productData.getName());
                                if (Datasource.getInstance().deleteSingleProduct(productData.getId())) {
                                    getTableView().getItems().remove(getIndex());
                                }
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();
                    {
                        buttonsPane.setSpacing(10);
                        buttonsPane.getChildren().add(viewButton);
                        buttonsPane.getChildren().add(editButton);
                        buttonsPane.getChildren().add(deleteButton);
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

        colBtnEdit.setCellFactory(cellFactory);

        tableProductsPage.getColumns().add(colBtnEdit);

    }

    @FXML
    public void btnProductsSearchOnAction(ActionEvent actionEvent) {
        Task<ObservableList<Product>> task = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() throws Exception {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().searchProducts(fieldProductsSearch.getText().toLowerCase(), Datasource.ORDER_BY_NONE));
            }
        };
        tableProductsPage.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }
}

class GetAllProductsTask extends Task {

    @Override
    public ObservableList<Product> call() {
        return FXCollections.observableArrayList
                (Datasource.getInstance().getAllProducts(Datasource.ORDER_BY_NONE));
    }
}