package com.example.ElectronicsStore;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//sort by price ascending
public class SortByTitle implements SortStrategy {

    @Override
    public ArrayList<Item> sort(ArrayList<Item> items) {


        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getName().compareTo(o2.getName());
            }


        });
        Log.i("items size", ""+items.size());
        return items;
    }
}
