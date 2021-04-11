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
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StockControl extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;
    final ArrayList<Item> myDataset= new ArrayList<Item>();

    final AdapterLocationList mAdapterStarter= new AdapterLocationList(myDataset, "store",null);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_control);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        RecyclerView RecyclerViewStarters= (RecyclerView) findViewById(R.id.starters_rv);
        RecyclerViewStarters.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        RecyclerViewStarters.setLayoutManager(mLayoutManager);
        RecyclerViewStarters.setAdapter(mAdapterStarter);




        Button button = (Button) findViewById(R.id.addItem);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addItem();
            }
        });




        fireDB = FirebaseDatabase.getInstance().getReference().child("stores").child(mUser.getUid()).child("items");

        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAdapterStarter.clear();

                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Item items= userSnapshot.getValue(Item.class);

                    Log.i("name",""+items.getName());
                        mAdapterStarter.addItemtoend(items);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DBError", "Cancel Access DB");
            }
        });


        // bottom nav menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.StoreStock);
        //item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.StoreStock:
                        return true;
                    case R.id.StoreCustomer:
                        startActivity(new Intent(getApplicationContext(), StoreCustomers.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }



    private void addItem() {
        AddItemDialog dialog = new AddItemDialog();
        dialog.show(this.getSupportFragmentManager(),"item");
    }
}