package com.example.ElectronicsStore;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//sort by price ascending
public class SortByPrice implements SortStrategy {

    @Override
    public ArrayList<Item> sort(ArrayList<Item> items) {


        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getPrice() > o2.getPrice())
                    return 1;
                if (o1.getPrice() < o2.getPrice())
                    return -1;
                return 0;
            }


        });
        Log.i("items size", ""+items.size());
        return items;
    }
}
