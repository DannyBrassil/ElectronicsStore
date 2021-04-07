package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    final AdapterLocationList mAdapterStarter= new AdapterLocationList(myDataset);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        RecyclerView RecyclerViewStarters= (RecyclerView) findViewById(R.id.starters_rv);
        RecyclerViewStarters.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        RecyclerViewStarters.setLayoutManager(mLayoutManager);
        RecyclerViewStarters.setAdapter(mAdapterStarter);




        Button button = (Button) findViewById(R.id.addStarter);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addStarter();
            }
        });


        Button button5 = (Button) findViewById(R.id.buttonFinishMenu);
        button5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(StockControl.this, Login.class);
                startActivity(intent);
            }
        });

        fireDB = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(mUser.getUid()).child("Menu");

        fireDB.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {//every time change data the event listener
                // will execute on datachange method for
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    Item items= userSnapshot.getValue(Item.class);

                        mAdapterStarter.addItemtoend(items);

                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DBError", "Cancel Access DB");
            }
        });



    }

    private void addDrink() {
        MenuDialog dialog = new MenuDialog("drink");
        dialog.show(this.getSupportFragmentManager(),"item");
    }

    private void addDessert() {
        MenuDialog dialog = new MenuDialog("dessert");
        dialog.show(this.getSupportFragmentManager(),"item");
    }

    private void addMain() {

        MenuDialog dialog = new MenuDialog("main");
        dialog.show(this.getSupportFragmentManager(),"item");
    }

    private void addStarter() {
        MenuDialog dialog = new MenuDialog("starter");
        dialog.show(this.getSupportFragmentManager(),"item");
    }
}