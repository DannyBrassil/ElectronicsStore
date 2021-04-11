package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoreCustomers extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference db;
    DatabaseReference fireDB;
     String title;

    final ArrayList<Item> myDataset= new ArrayList<Item>();
    final AdapterLocationList mAdapter= new AdapterLocationList(myDataset, "customer",null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_customers);

        final Intent intent = getIntent();
        title = intent.getStringExtra("title");

        mAuth = FirebaseAuth.getInstance();

        TextView t1 = (TextView) findViewById(R.id.BookingRestaurantName);
        t1.setText(title);







        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.menu_RV);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        recyclerView();

        Button button = (Button) findViewById(R.id.gotocart);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MakePuchase();
            }
        });

        // bottom nav menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // home is selected
        bottomNavigationView.setSelectedItemId(R.id.StoreCustomer);
        //item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.StoreCustomer:
                        return true;
                    case R.id.StoreStock:
                        startActivity(new Intent(getApplicationContext(), StockControl.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    private void recyclerView() {
        fireDB = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//every time change data the event listener
                // will execute on datachange method for
                for (final DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Store r = userSnapshot.getValue(Store.class);
                    String key = userSnapshot.getKey();

                    if(r.getName().equalsIgnoreCase(title)){
                        fireDB.child(key).child("Menu").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {//every time change data the event listener
                                // will execute on datachange method for
                                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                                    Item items= userSnapshot.getValue(Item.class);
                                    // myDataset.add(notes);
                                        mAdapter.addItemtoend(items);
                                }



                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("DBError", "Cancel Access DB");
                            }
                        });


                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DBError", "Cancel Access DB");
            }
        });
    }

    private void MakePuchase() {
        db= FirebaseDatabase.getInstance().getReference(); // get reference from roo
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        Purchase b = new Purchase();

        db.child("Users").child(uid).child("Booking").push().setValue(b).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(StoreCustomers.this, "Booking is successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StoreCustomers.this, HomeMenu.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StoreCustomers.this, "Booking is not successful", Toast.LENGTH_LONG).show();
            }
        });


    }


}