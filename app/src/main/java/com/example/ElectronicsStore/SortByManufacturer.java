package com.example.ElectronicsStore;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//sort by price ascending
public class SortByManufacturer implements SortStrategy {

    @Override
    public ArrayList<Item> sort(ArrayList<Item> items) {

        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getManufacturer().compareTo(o2.getManufacturer());
            }
        });
        return items;
    }
}
