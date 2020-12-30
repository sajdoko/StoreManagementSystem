package controller.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.Datasource;
import model.Product;

public class ProductsController {

    public Button btnProductsSearch;

    @FXML
    private TableView tblProductsView;


    public void btnProductsSearchOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void listProducts() {

        Task<ObservableList<Product>> task = new GetAllProductsTask();

        tblProductsView.itemsProperty().bind(task.valueProperty());

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