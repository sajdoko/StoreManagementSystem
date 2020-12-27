package model;

import controller.UserSessionController;

import java.sql.*;
import java.util.Date;

public class Product {

    private long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;


    public long getId() {
        return id;
    }

    public void setId(long id) {
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


    public String[] getAllProducts() {
        String[] allProductsList = {};
        String sql = "SELECT * FROM products";

//        preparedStatement = connection.prepareStatement(sql);
//        resultSet = preparedStatement.executeQuery();
//        if (resultSet.next()) {
//            return allProductsList;
//        } else {
//            return allProductsList;
//        }
        return allProductsList;
    }
}
