package controller.admin.pages.products;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import model.Categories;
import model.Datasource;
import model.Product;

/**
 * {@inheritDoc}
 */
public class EditProductController extends ProductsController {

    @FXML
    public Text viewProductResponse;
    public TextField fieldEditProductName;
    public TextField fieldEditProductPrice;
    public TextField fieldEditProductQuantity;
    public ComboBox<Categories> fieldEditProductCategoryId;
    public TextArea fieldEditProductDescription;
    public TextField fieldEditProductId;
    public Text viewProductName;

    @FXML
    private void initialize() {
        fieldEditProductCategoryId.setItems(FXCollections.observableArrayList(Datasource.getInstance().getProductCategories(Datasource.ORDER_BY_ASC)));

        TextFormatter<Double> textFormatterDouble = formatDoubleField();
        TextFormatter<Integer> textFormatterInt = formatIntField();
        fieldEditProductPrice.setTextFormatter(textFormatterDouble);
        fieldEditProductQuantity.setTextFormatter(textFormatterInt);
    }

    /**
     * This private method handles the add product button functionality.
     * It validates user input fields and adds the values to the database.
     * @since                   1.0.0
     */
    @FXML
    private void btnEditProductOnAction() {
        Categories category = fieldEditProductCategoryId.getSelectionModel().getSelectedItem();
        int cat_id = 0;
        if (category != null) {
            cat_id = category.getId();
        }

        assert category != null;
        if (areProductInputsValid(fieldEditProductName.getText(), fieldEditProductDescription.getText(), fieldEditProductPrice.getText(), fieldEditProductQuantity.getText(), cat_id)) {

            int productId = Integer.parseInt(fieldEditProductId.getText());
            String productName = fieldEditProductName.getText();
            String productDescription = fieldEditProductDescription.getText();
            double productPrice = Double.parseDouble(fieldEditProductPrice.getText());
            int productQuantity = Integer.parseInt(fieldEditProductQuantity.getText());
            int productCategoryId = category.getId();

            Task<Boolean> addProductTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return Datasource.getInstance().updateOneProduct(productId, productName, productDescription, productPrice, productQuantity, productCategoryId);
                }
            };

            addProductTask.setOnSucceeded(e -> {
                if (addProductTask.valueProperty().get()) {
                    viewProductResponse.setVisible(true);
                    System.out.println("Product edited!");
                }
            });

            new Thread(addProductTask).start();
        }
    }

    /**
     * This method gets the data for one product from the database and binds the values to editing fields.
     * @param product_id        Product id.
     * @since                   1.0.0
     */
    public void fillEditingProductFields(int product_id) {
        Task<ObservableList<Product>> fillProductTask = new Task<ObservableList<Product>>() {
            @Override
            protected ObservableList<Product> call() {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().getOneProduct(product_id));
            }
        };
        fillProductTask.setOnSucceeded(e -> {
            viewProductName.setText("Editing: " + fillProductTask.valueProperty().getValue().get(0).getName());
            fieldEditProductId.setText(String.valueOf(fillProductTask.valueProperty().getValue().get(0).getId()));
            fieldEditProductName.setText(fillProductTask.valueProperty().getValue().get(0).getName());
            fieldEditProductPrice.setText(String.valueOf(fillProductTask.valueProperty().getValue().get(0).getPrice()));
            fieldEditProductQuantity.setText(String.valueOf(fillProductTask.valueProperty().getValue().get(0).getQuantity()));
            fieldEditProductDescription.setText(fillProductTask.valueProperty().getValue().get(0).getDescription());

            Categories category = new Categories();
            category.setId(fillProductTask.valueProperty().getValue().get(0).getCategory_id());
            category.setName(fillProductTask.valueProperty().getValue().get(0).getCategory_name());
            fieldEditProductCategoryId.getSelectionModel().select(category);
        });

        new Thread(fillProductTask).start();
    }
}
