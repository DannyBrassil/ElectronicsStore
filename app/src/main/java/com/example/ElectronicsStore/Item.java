package com.example.ElectronicsStore;

import java.util.ArrayList;

public class Item {

    private String id;
    private String name;
    private double price;
    private String category;
    private String manufacturer;
    private String description;
    private int stock;
    private ArrayList<Review> reviews;

    public Item(){ }

    public Item(String id, String name, double price, String category, String manufacturer, String description, int stock, ArrayList<Review> reviews) {
        this.id=id;
        this.name = name;
        this.price = price;
        this.category=category;
        this.manufacturer=manufacturer;
        this.description =description;
        this.stock=stock;
        this.reviews=reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
