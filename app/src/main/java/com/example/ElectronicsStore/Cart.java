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
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;

public class Cart extends AppCompatActivity {
     FirebaseAuth mAuth;
     FirebaseUser mUser;
    DatabaseReference fireDBOrders;
    final ArrayList<Item> myDataset= new ArrayList<>();
    AdapterLocationList mAdapter;
     DatabaseReference fireDB;

     ArrayList<Item>items = new ArrayList<>();
    ArrayList<Item>tempitems = new ArrayList<>();
     double price;
    double tempPrice=0.0;

    TextView subtotal;


    public static TextView discountApplied;
    public static TextView totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        subtotal= findViewById(R.id.subTotal);
        discountApplied = findViewById(R.id.discountApplied);
        totalPrice = findViewById(R.id.TotalPrice);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        fireDB = FirebaseDatabase.getInstance().getReference();

        fireDBOrders= FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid()).child("orders");


        mAdapter= new AdapterLocationList(myDataset, "customer");
        RecyclerView mRecyclerView= (RecyclerView) findViewById(R.id.RV_Booking);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        //populate recycler view with items in cart


            fireDB.child("users").child(mUser.getUid()).child("cart").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {//every time change data the event listener
                    mAdapter.clear();
                    // will execute on datachange method for
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        Item r= userSnapshot.getValue(Item.class);
                        mAdapter.addItemtoend(r);
                    }
                    getOrder();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("DBError", "Cancel Access DB");
                }
            });


        //get subtotal of cart items
        getSubTotal();









        Button button = (Button) findViewById(R.id.payButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){



                final DatabaseReference pushRef = fireDB.child("users").child(mUser.getUid()).child("orders").push();

                final String id= pushRef.getKey();


                fireDB.child("users").child(mUser.getUid()).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        items.clear();
                        price=0.0;
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            Item item = snapshot1.getValue(Item.class);
                            items.add(item);
                            price=price+item.getPrice();
                        }
                        Date today = new Date();
                        today= Calendar.getInstance().getTime();
                        Order order = new Order(id,price,items,today);


                        applyDiscounts(order);


                        if(items.size()>0) {
                            pushRef.setValue(order);
                        }else{
                            Toast.makeText(Cart.this, "you must have items in your cart", Toast.LENGTH_LONG);
                        }
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

    private void getOrder() {
        totalPrice.setText("");
        discountApplied.setText("");


        fireDB.child("users").child(mUser.getUid()).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempitems.clear();
                tempPrice=0.0;
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Item item = snapshot1.getValue(Item.class);
                    tempitems.add(item);
                    tempPrice=tempPrice+item.getPrice();
                    Log.i("item",""+item.getPrice());
                }

                Order tempOrder = new Order(null,tempPrice,tempitems,null);

                Log.i("order price", ""+tempOrder.getPrice());
                subtotal.setText("$"+tempOrder.getPrice());
                applyDiscounts(tempOrder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void applyDiscounts(Order order) {

        DiscountChain firstcheck = createChainOfResponsibility();
        DiscountOption discountRules = new DiscountOption(order, false,false);
        firstcheck.apply(discountRules);
        totalPrice.setText("$"+order.getPrice());

    }

    private void getSubTotal() {

        fireDB.child("users").child(mUser.getUid()).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Item item = snapshot1.getValue(Item.class);
                    items.add(item);
                    price=price+item.getPrice();
                }
                subtotal.setText("$"+price);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private DiscountChain createChainOfResponsibility(){
        DiscountChain firstOrder = new firstOrderDiscount();
        DiscountChain over50 = new DiscountOver50();
        DiscountChain over100 = new DiscountOver100();

        DiscountChain firstchain = new DiscountOver200();
        firstchain.setNextChain(over100);
        over100.setNextChain(over50);
        return firstchain;
    }

}