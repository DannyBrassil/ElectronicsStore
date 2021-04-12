package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClickedItemCustomer extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;
    DatabaseReference fireDBUser;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_item_customer);
        final Intent intent = getIntent();
        final String itemID = intent.getStringExtra("ItemID");

        final TextView name = findViewById(R.id.ClickedItemName);
        final TextView category = findViewById(R.id.ClickedItemCategory);
        final TextView price = findViewById(R.id.clickedItemPrice);
        final TextView manufacturer = findViewById(R.id.ClickedItemManufacturer);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String UserID=mUser.getUid();



        fireDB= FirebaseDatabase.getInstance().getReference("store").child("items").child(itemID);
        fireDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 item = snapshot.getValue(Item.class);
                name.setText(item.getName());
                category.setText(item.getCategory());
                price.setText(Double.toString(item.getPrice()));
                manufacturer.setText(item.getManufacturer());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button addTocart = findViewById(R.id.AddToCartButton);
        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireDBUser= FirebaseDatabase.getInstance().getReference("users").child(UserID);

                fireDBUser.child("cart").push().setValue(item);

            }
        });


    }
}