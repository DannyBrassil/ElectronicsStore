package com.example.ElectronicsStore;

import java.util.ArrayList;
import java.util.Date;

public class Purchase {

    private String storeID;
    private int price;
    private ArrayList<String> items;

    public Purchase(){

    }

    public Purchase(String storeID, int price, ArrayList<String> items) {
        this.storeID = storeID;
        this.price = price;
        this.items = items;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }
}
