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


public class HomeMenu extends AppCompatActivity {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    public static SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;
    public static TextView e1;
    public static ArrayList<Store> parsedRestaurants = new ArrayList<>();
    final ArrayList<Item> myDataset= new ArrayList<>();

    public static LatLng latLng;

    AdapterLocationList mAdapter;


    //DatabaseReference fireDBUser = FirebaseDatabase.getInstance().getReference("carparks");

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

        recyclerView();



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



    }








    private void recyclerView() {


        fireDB.child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//every time change data the event listener
                // will execute on datachange method for
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Item r= userSnapshot.getValue(Item.class);
                    mAdapter.addItemtoend(r);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DBError", "Cancel Access DB");
            }
        });



    }









}
