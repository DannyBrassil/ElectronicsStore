package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StockControl extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;
    final ArrayList<Item> myDataset= new ArrayList<Item>();

    final AdapterLocationList mAdapter= new AdapterLocationList(myDataset, "store");

    final ArrayList<Item> items = new ArrayList<>();
    final ArrayList<Item> searchItems = new ArrayList<>();

    String name;
    String filter="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_control);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        fireDB = FirebaseDatabase.getInstance().getReference().child("store").child("items");

        RecyclerView RecyclerViewStarters = (RecyclerView) findViewById(R.id.starters_rv);
        RecyclerViewStarters.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerViewStarters.setLayoutManager(mLayoutManager);
        RecyclerViewStarters.setAdapter(mAdapter);


        Button button = (Button) findViewById(R.id.addItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });



        populateRecyclerView();


        // bottom nav menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.StoreStock);
        //item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.StoreStock:
                        return true;
                    case R.id.StoreCustomer:
                        startActivity(new Intent(getApplicationContext(), StoreCustomers.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });


        final Spinner filterBySpinner = (Spinner) findViewById(R.id.spinner4);

        filterBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://filter by name
                        filter = "";
                        break;
                    case 1://filter by name
                        filter = "name";
                        break;
                    case 2://filter by manufactuer
                        filter = "manufacturer";
                        break;
                    case 3://filter by categpry
                        filter = "category";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button search = findViewById(R.id.button3);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        final Spinner sortBySpinner = (Spinner) findViewById(R.id.spinner3);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Context context;
                switch (position) {
                    case 1://sort by cheapest
                        Log.i("items size", "" + items.size());
                        context = new Context(new SortByPrice());
                        recyclerView(context.executeStrategy(items));
                        break;
                    case 2://sort by expensive
                        context = new Context(new SortByPrice());
                        ArrayList<Item> expensive = context.executeStrategy(items);
                        Collections.reverse(expensive);
                        recyclerView(expensive);
                        break;
                    case 3://sort by title ascending
                        context = new Context(new SortByTitle());
                        recyclerView(context.executeStrategy(items));
                        break;
                    case 4://sort by title descending
                        context = new Context(new SortByTitle());
                        ArrayList<Item> title = context.executeStrategy(items);
                        Collections.reverse(title);
                        recyclerView(title);
                        break;
                    case 5://sort by manufacturer ascending
                        context = new Context(new SortByManufacturer());
                        recyclerView(context.executeStrategy(items));
                        break;
                    case 6://sort by title descending
                        context = new Context(new SortByTitle());
                        ArrayList<Item> manufacturer = context.executeStrategy(items);
                        Collections.reverse(manufacturer);
                        recyclerView(manufacturer);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
        private void search() {
            mAdapter.clear();
            searchItems.clear();

            TextView SearchName = (TextView) findViewById(R.id.itemNameSearch2);
            name = SearchName.getText().toString();


            fireDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Item r = userSnapshot.getValue(Item.class);
                            if (filter.equalsIgnoreCase("name")) {
                                if (r.getName().equalsIgnoreCase(name)) {
                                    Log.i("correct","ys");
                                    searchItems.add(r);
                                }
                            } else if (filter.equalsIgnoreCase("manufacturer")) {
                                Log.i("manufacturer", "" + r.getManufacturer());
                                if (r.getManufacturer().contains(name)) {
                                    searchItems.add(r);
                                }
                            } else if (filter.equalsIgnoreCase("category")) {
                                Log.i("category", "" + r.getCategory());
                                if (r.getCategory().contains(name)) {
                                    searchItems.add(r);
                                }
                            }else{
                                searchItems.add(r);
                            }
                    }
                    recyclerView(searchItems);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("DBError", "Cancel Access DB");
                }
            });


        }



    private void addItem() {
        AddItemDialog dialog = new AddItemDialog();
        dialog.show(this.getSupportFragmentManager(),"item");
    }

    private void populateRecyclerView() {
        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAdapter.clear();
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Item item1= userSnapshot.getValue(Item.class);
                    items.add(item1);
                }
                recyclerView(items);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DBError", "Cancel Access DB");
            }
        });
    }

    private void recyclerView(ArrayList<Item> items) {
        mAdapter.clear();
        for(int i = 0; i<items.size(); i++){
            mAdapter.addItemtoend(items.get(i));
        }
    }
}