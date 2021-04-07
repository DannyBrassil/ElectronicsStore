package com.example.ElectronicsStore;

import java.util.ArrayList;


public class Menu {
    private ArrayList<Item> items;

    public Menu(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
