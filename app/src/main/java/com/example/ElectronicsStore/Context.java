package com.example.ElectronicsStore;


import java.util.ArrayList;

public class Context {

    private SortStrategy strategy;

    public Context(SortStrategy strategy){
        this.strategy = strategy;
    }

    public ArrayList<Item> executeStrategy(ArrayList<Item> items) {
        return strategy.sort(items);
    }

}