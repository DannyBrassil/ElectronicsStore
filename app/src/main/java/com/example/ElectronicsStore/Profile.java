package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class Profile extends AppCompatActivity {
     FirebaseAuth mAuth;
     FirebaseUser mUser;
    DatabaseReference fireDB;
    DatabaseReference fireDBChart;

    User userObj = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final EditText editfirstname = (EditText) findViewById(R.id.firstnametextedit);
        final EditText editsecondname = (EditText) findViewById(R.id.secondnametextedit);
        final EditText editnumber = (EditText) findViewById(R.id.numbertextedit);
        final EditText editemail = (EditText) findViewById(R.id.emailTextedit2);


     //   createBarchart();

        //retrieve user details and updateUI
        fireDB= FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        //  fireDB.child("Notes")

        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userObj= snapshot.getValue(User.class);
                editfirstname.setText(userObj.getFirstName());
                editsecondname.setText(userObj.getSecondName());
                editnumber.setText(userObj.getNumber());
                editemail.setText(userObj.getEmail());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fireDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        fireDB.child("firstName").setValue(editfirstname.getText().toString());
                        fireDB.child("secondName").setValue(editsecondname.getText().toString());
                        fireDB.child("number").setValue(editnumber.getText().toString());
                        fireDB.child("email").setValue(editemail.getText().toString());
                        Toast.makeText(Profile.this, "profile updated", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        fireDB.child("Booking").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Purchase booking = snapshot.getValue(Purchase.class);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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


        fireDBChart = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Booking");






    }

}