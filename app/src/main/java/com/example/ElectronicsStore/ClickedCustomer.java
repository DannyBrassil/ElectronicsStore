package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClickedCustomer extends AppCompatActivity {
    DatabaseReference fireDB;
    final ArrayList<Order> myDataset= new ArrayList<>();
    AdapterLocationListOrders mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_customer);
        Intent i = getIntent();
        String id = i.getStringExtra("CustomerID");

        fireDB = FirebaseDatabase.getInstance().getReference();

        final TextView email = findViewById(R.id.clickedCustomerEmail);
        final TextView name = findViewById(R.id.clickedCustomerName);
        final TextView number = findViewById(R.id.clickedCustomerNumber);
        final TextView address = findViewById(R.id.clickedCustomerAddress);

        mAdapter= new AdapterLocationListOrders(myDataset);
        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.orderItems);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);





        fireDB.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User user = snapshot.getValue(User.class);
               name.setText(user.getFirstName());
               email.setText(user.getEmail());
               number.setText(user.getNumber());
               address.setText(user.address.getLine1()+", \n"+user.address.getLine2()+", \n"+user.address.getLine3()+", \n"+user.address.getCounty());
                recyclerView(user.getId());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void recyclerView(String id) {
        fireDB.child("users").child(id).child("orders").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot snapshot1: snapshot.getChildren()){
                Order order = snapshot1.getValue(Order.class);
                mAdapter.addItemtoend(order);
            }


        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });
    }


}