package com.example.ElectronicsStore;

import java.util.ArrayList;
import java.util.Date;

public class Order {

private String id;
    private double price;
    private ArrayList<Item> items;
    Date date;


    public Order(){

    }

    public Order(String id, double price, ArrayList<Item> items,  Date d) {
       this.id=id;
        this.price = price;
        this.items = items;
        this.date =d;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void applyDiscount(float discount) {
        this.price = price * (1F - discount);
    }
}
