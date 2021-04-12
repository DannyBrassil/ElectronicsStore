package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {
     FirebaseAuth mAuth;
     FirebaseUser mUser;
    DatabaseReference fireDB;
    DatabaseReference fireDBOrders;
    final ArrayList<Item> myDataset= new ArrayList<>();
    AdapterLocationList mAdapter;


     ArrayList<Item>items = new ArrayList<>();
     double price;

    User userObj = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        fireDB = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        fireDBOrders= FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("orders");


        mAdapter= new AdapterLocationList(myDataset, "customer");
        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.RV_Booking);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        recyclerView();






        Button button = (Button) findViewById(R.id.payButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                fireDB.child("users").child(mUser.getUid()).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            Item item = snapshot1.getValue(Item.class);
                            items.add(item);
                            price=price+item.getPrice();
                        }
                        Order order = new Order(price,items);
                        fireDBOrders.push().setValue(order);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                fireDB.child("users").child(mUser.getUid()).child("cart").removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Cart.this, "Payment successful", Toast.LENGTH_LONG);
                        startActivity(new Intent(getApplicationContext(), HomeMenu.class));
                    }
                });

            }
        });




        // bottom nav menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // home is selected
        bottomNavigationView.setSelectedItemId(R.id.profile);
        //item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeMenu.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });





    }

    private void recyclerView() {

        fireDB.child("users").child(mUser.getUid()).child("cart").addValueEventListener(new ValueEventListener() {
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