package com.example.ElectronicsStore;

import java.util.ArrayList;

public class Item {

    private String name;
    private double price;
    private String category;
    private String manufacturer;
    private ArrayList<Review> reviews;

    public Item(){ }

    public Item( String name, double price, String category, String manufacturer, ArrayList<Review> reviews) {
        this.name = name;
        this.price = price;
        this.category=category;
        this.manufacturer=manufacturer;
        this.reviews=reviews;
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
}
