package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpStore extends AppCompatActivity {
    public DatabaseReference db;
    private FirebaseAuth mAuth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_store);

        mAuth1 = FirebaseAuth.getInstance();

        Button button = (Button) findViewById(R.id.createmenubutton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createAccount();
            }
        });
    }

    private void createAccount() {


        EditText e1 = (EditText) findViewById(R.id.storename);
        EditText e7 =  (EditText) findViewById(R.id.Email);
        EditText e8 =  (EditText) findViewById(R.id.Password);
        final String email = e7.getText().toString();
        final String password = e8.getText().toString();
        final String name = e1.getText().toString();

        mAuth1.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if (!task.isSuccessful()) {
                        //      Toast.makeText(SignUp.this, "signup unsuccessful", Toast.LENGTH_LONG).show();
                        //  } else {
                        //    Toast.makeText(SignUp.this, "sign up success. please login", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth1.getCurrentUser();
                        String uid = user.getUid();

                        //add user to realtime database
                        db= FirebaseDatabase.getInstance().getReference(); // get reference from roo
                        Store r = new Store(uid, email, password, name, null);


                        db.child("Stores").child(uid).setValue(r).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUpStore.this, "sign up is successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUpStore.this, StockControl.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpStore.this, "sign up is not successful", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    // ...
                    //    }
                });

    }

}