package com.example.ElectronicsStore;

import java.util.ArrayList;
import java.util.Date;

public class Order {


    private double price;
    private ArrayList<Item> items;

    public Order(){

    }

    public Order(double price, ArrayList<Item> items) {
        this.price = price;
        this.items = items;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
