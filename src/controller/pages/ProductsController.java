package controller.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Datasource;
import model.Product;

public class ProductsController {

    public Button btnProductsSearch;

    @FXML
    private TableView<Product> tableProductsPage;


    public void btnProductsSearchOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void listProducts() {

        GetAllProductsTask getAllProductsTask = new GetAllProductsTask();

        tableProductsPage.itemsProperty().bind(getAllProductsTask.valueProperty());
        addEditButtonToTable();

        new Thread(getAllProductsTask).start();
    }

    private void addEditButtonToTable() {
        TableColumn colBtnEdit = new TableColumn("Actions");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<Product, Void>() {

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            System.out.println("product id: " + productData.getId());
                            System.out.println("product name: " + productData.getName());
                        });
                    }

                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());
                            System.out.println("product id: " + productData.getId());
                            System.out.println("product name: " + productData.getName());
                        });
                    }

                    final HBox buttonsPane = new HBox(editButton, deleteButton);

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

}


class GetAllProductsTask extends Task {

    @Override
    public ObservableList<Product> call() {
        return FXCollections.observableArrayList
                (Datasource.getInstance().getAllProducts(Datasource.ORDER_BY_NONE));
    }
}