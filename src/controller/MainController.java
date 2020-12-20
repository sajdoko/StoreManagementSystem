package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Products;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField categoryField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Products> TableView;

    @FXML
    private TableColumn<Products, Integer> idColumn;

    @FXML
    private TableColumn<Products, String> nameColumn;

    @FXML
    private TableColumn<Products, Integer> priceColumn;

    @FXML
    private TableColumn<Products, Integer> quantityColumn;

    @FXML
    private TableColumn<Products, Integer> nrSalesColumn;

    @FXML
    private TableColumn<Products, String> actionsColumn;

    @FXML
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/store_manager","root","");
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProducts();
    }

    @FXML
    public void insertButton(ActionEvent actionEvent) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String query = "INSERT INTO `products` (`name`, `description`, `price`, `quantity`, `category`, `createdAt`, `updatedAt`) VALUES ('"+
                nameField.getText()+"', '"+
                descriptionField.getText()+"', "+
                priceField.getText()+", "+
                quantityField.getText()+", '"+
                categoryField.getText()+"', '"+
                formatter.format(date)+"', '"+
                formatter.format(date)+"'" +
                ")";
        executeQuery(query);
        showProducts();
    }

    @FXML
    public void updateButton(ActionEvent actionEvent) {
        String query = "UPDATE products SET name='"+
                nameField.getText()+"',description='"+
                descriptionField.getText()+"',price="+
                priceField.getText()+",quantity="+
                quantityField.getText()+",category="+
                categoryField.getText()+" WHERE ID="+idField.getText()+"";
        executeQuery(query);
        showProducts();
    }

    @FXML
    public void deleteButton(ActionEvent actionEvent) {
        String query = "DELETE FROM products WHERE ID="+idField.getText()+"";
        executeQuery(query);
        showProducts();
    }

    @FXML
    public ObservableList<Products> getProductsList(){
        ObservableList<Products> productsList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM products ";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Products products;
            while(rs.next()) {
                products = new Products(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getInt("quantity"),
                        rs.getString("category")
                );
                productsList.add(products);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productsList;
    }

    @FXML
    public void showProducts() {
        ObservableList<Products> list = getProductsList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        nrSalesColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>(""));

        TableView.setItems(list);
    }

}
