package com.example.ElectronicsStore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;


public class HomeMenu extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;
    final ArrayList<Item> myDataset= new ArrayList<>();


    AdapterLocationList mAdapter;

    final ArrayList<Item> items = new ArrayList<>();


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        Intent intent = getIntent();

        fireDB= FirebaseDatabase.getInstance().getReference("store");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final TextView storename = (TextView) findViewById(R.id.textView12);



        fireDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Store store = snapshot.getValue(Store.class);
               // storename.setText(store.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        mAdapter= new AdapterLocationList(myDataset, "customer");
        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.RV_Booking);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        populateRecyclerView();



                // bottom nav menu
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // home is selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        //item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Cart.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });


        final Spinner sortBySpinner = (Spinner)findViewById(R.id.spinner);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Context context;
                switch (position){
                    case 1://sort by cheapest
                        Log.i("items size", ""+items.size());
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






    private void populateRecyclerView(){


        fireDB.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//every time change data the event listener
                // will execute on datachange method for
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Item r= userSnapshot.getValue(Item.class);
                    if(r.getStock()>0){
                        items.add(r);
                    }
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
