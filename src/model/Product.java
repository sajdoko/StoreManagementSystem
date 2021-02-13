package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty description;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty category;
    private final SimpleIntegerProperty nr_sales;

    public Product() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty();
        this.quantity = new SimpleIntegerProperty();
        this.category = new SimpleStringProperty();
        this.nr_sales = new SimpleIntegerProperty();
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public int getNr_sales() {
        return nr_sales.get();
    }

    public SimpleIntegerProperty nr_salesProperty() {
        return nr_sales;
    }

    public void setNr_sales(int nr_sales) {
        this.nr_sales.set(nr_sales);
    }
}
