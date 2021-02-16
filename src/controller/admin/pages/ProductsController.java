package controller.admin.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Datasource;
import model.Product;

import java.io.IOException;
import java.util.Optional;

/**
 * This class handles the admin products page.
 * @author      Sajmir Doko
 * @since       1.0.0
 */
public class ProductsController {

    @FXML
    public TextField fieldProductsSearch;
    @FXML
    public TextField fieldAddProductName;
    @FXML
    public TextField fieldAddProductPrice;
    @FXML
    public TextField fieldAddProductQuantity;
    @FXML
    public TextArea fieldAddProductDescription;
    @FXML
    public ComboBox fieldAddProductCategoryId;
    @FXML
    public Text viewProductResponse;
    @FXML
    public GridPane formEditProductView;
    @FXML
    private TextField fieldEditProductPrice;
    @FXML
    private StackPane productsContent;
    @FXML
    private TableView<Product> tableProductsPage;

    /**
     * This method lists all the product to the view table.
     * It starts a new Task, gets all the products from the database then bind the results to the view.
     * @author                  Sajmir Doko
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
     * @author                  Sajmir Doko
     * @since                   1.0.0
     */
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
                            btnViewProduct(productData.getId());
                            System.out.println("View Product");
                            System.out.println("product id: " + productData.getId());
                            System.out.println("product name: " + productData.getName());
                        });
                    }

                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setOnAction((ActionEvent event) -> {
                            Product productData = getTableView().getItems().get(getIndex());

                            btnEditProduct(productData.getId());
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

    /**
     * This private method handles the products search functionality.
     * It creates a new task, gets the search results from the database and binds them to the view table.
     * @author                  Sajmir Doko
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
     * This private method loads the add product view page.
     * @author                  Sajmir Doko
     * @since                   1.0.0
     */
    @FXML
    private void btnAddProductOnClick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/products/add-product.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        productsContent.getChildren().clear();
        productsContent.getChildren().add(root);

    }

    /**
     * This private method handles the add product button functionality.
     * It validates user input fields and adds the values to the database.
     * @author                  Sajmir Doko
     * @since                   1.0.0
     */
    @FXML
    private void btnAddProductOnAction() {
        if (isAddProductInputsValid()) {
            String productName = fieldAddProductName.getText();
            String productDescription = fieldAddProductDescription.getText();
            int productPrice = Integer.parseInt(fieldAddProductPrice.getText());
            int productQuantity = Integer.parseInt(fieldAddProductQuantity.getText());
            int productCategoryId = Integer.parseInt(fieldAddProductCategoryId.getValue().toString());

            Task<Boolean> addProductTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().insertNewProduct(productName, productDescription, productPrice, productQuantity, productCategoryId);
                }
            };

            addProductTask.setOnSucceeded(e -> {
                if (addProductTask.valueProperty().get()) {
                    viewProductResponse.setVisible(true);
                    System.out.println("Product added!");
                }
            });

            new Thread(addProductTask).start();
        }
    }

    /**
     * This private method validates the user input fields for the product.
     * @return boolean          Returns true or false.
     * @author                  Sajmir Doko
     * @since                   1.0.0
     */
    @FXML
    private boolean isAddProductInputsValid() {
        // TODO
        //  Better validate inputs.
        String errorMessage = "";

        if (fieldAddProductName.getText() == null || fieldAddProductName.getText().length() == 0) {
            errorMessage += "Not valid name!\n";
        }
        if (fieldAddProductDescription.getText() == null || fieldAddProductDescription.getText().length() == 0) {
            errorMessage += "Not valid Description!\n";
        }
        if (fieldAddProductPrice.getText() == null || fieldAddProductPrice.getText().length() == 0) {
            errorMessage += "Not valid Price!\n";
        }

        if (fieldAddProductQuantity.getText() == null || fieldAddProductQuantity.getText().length() == 0) {
            errorMessage += "Not valid quantity!\n";
        }

        if (fieldAddProductCategoryId.getValue().toString() == null || fieldAddProductCategoryId.getValue().toString().length() == 0) {
            errorMessage += "Not valid category id!\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }

    }

    /**
     * This private method loads the edit product view page.
     * @param product_id        Product id.
     * @author                  Sajmir Doko
     * @since                   1.0.0
     */
    @FXML
    private void btnEditProduct(int product_id) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/products/edit-product.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        productsContent.getChildren().clear();
        productsContent.getChildren().add(root);
        fillEditProduct(product_id);


    }

    /**
     * This private method loads single add product view page.
     * @param product_id        Product id.
     * @author                  Sajmir Doko
     * @since                   1.0.0
     */
    @FXML
    private void btnViewProduct(int product_id) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.load(getClass().getResource("/view/admin/pages/products/view-product.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane root = fxmlLoader.getRoot();
        productsContent.getChildren().clear();
        productsContent.getChildren().add(root);

        fillEditProduct(product_id);

    }

    /**
     * This private method gets the single product data from the database and binds them to the view.
     * @param product_id        Product id.
     * @author                  Sajmir Doko
     * @since                   1.0.0
     */
    @FXML
    private void fillEditProduct(int product_id) {
//        ObservableList<Product> fillProductTask = FXCollections.observableArrayList(Datasource.getInstance().getOneProduct(product_id));
//
////
////        System.out.println("pr name:" + fillProductTask.valueProperty().getValue().get(0).getName());
//        fieldEditProductPrice.setText("hi");
    }

}