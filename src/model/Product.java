package model;

public class Product {

    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;
    private int nr_sales;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNr_sales() {
        return nr_sales;
    }

    public void setNr_sales(int nr_sales) {
        this.nr_sales = nr_sales;
    }
}
