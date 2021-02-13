package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {

    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty product_id;
    private final SimpleStringProperty product_name;
    private final SimpleIntegerProperty user_id;
    private final SimpleStringProperty user_full_name;
    private final SimpleStringProperty shipping_address;
    private final SimpleStringProperty order_email;
    private final SimpleStringProperty order_date;
    private final SimpleStringProperty order_status;
    private final SimpleDoubleProperty order_price;

    public Order() {
        this.id = new SimpleIntegerProperty();
        this.product_id = new SimpleIntegerProperty();
        this.product_name = new SimpleStringProperty();
        this.user_id = new SimpleIntegerProperty();
        this.user_full_name = new SimpleStringProperty();
        this.shipping_address = new SimpleStringProperty();
        this.order_email = new SimpleStringProperty();
        this.order_date = new SimpleStringProperty();
        this.order_status = new SimpleStringProperty();
        this.order_price = new SimpleDoubleProperty();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getProduct_id() {
        return product_id.get();
    }

    public SimpleIntegerProperty product_idProperty() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id.set(product_id);
    }

    public String getProduct_name() {
        return product_name.get();
    }

    public SimpleStringProperty product_nameProperty() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name.set(product_name);
    }

    public int getUser_id() {
        return user_id.get();
    }

    public SimpleIntegerProperty user_idProperty() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id.set(user_id);
    }

    public String getUser_full_name() {
        return user_full_name.get();
    }

    public SimpleStringProperty user_full_nameProperty() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name.set(user_full_name);
    }

    public String getShipping_address() {
        return shipping_address.get();
    }

    public SimpleStringProperty shipping_addressProperty() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address.set(shipping_address);
    }

    public String getOrder_email() {
        return order_email.get();
    }

    public SimpleStringProperty order_emailProperty() {
        return order_email;
    }

    public void setOrder_email(String order_email) {
        this.order_email.set(order_email);
    }

    public String getOrder_date() {
        return order_date.get();
    }

    public SimpleStringProperty order_dateProperty() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date.set(order_date);
    }

    public String getOrder_status() {
        return order_status.get();
    }

    public SimpleStringProperty order_statusProperty() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status.set(order_status);
    }

    public double getOrder_price() {
        return order_price.get();
    }

    public SimpleDoubleProperty order_priceProperty() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price.set(order_price);
    }
}
