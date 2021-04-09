package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseStore extends AppCompatActivity {
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
    final ArrayList<Store> myDataset= new ArrayList<Store>();
    final AdapterLocationList2 mAdapter= new AdapterLocationList2(myDataset);
    public static LatLng latLng;



    //DatabaseReference fireDBUser = FirebaseDatabase.getInstance().getReference("carparks");

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_store);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //e1 = (TextView) findViewById(R.id.textView1);
        //e1.setText(mUser.getDisplayName());







        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.RV_Booking);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        recyclerView();

    }








    private void recyclerView() {
        fireDB= FirebaseDatabase.getInstance().getReference("stores");

        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//every time change data the event listener
                // will execute on datachange method for

                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Store r= userSnapshot.getValue(Store.class);

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