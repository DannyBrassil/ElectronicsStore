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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Button button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });


        //signup button
        Button button2 = (Button) findViewById(R.id.signUpButton);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signup();
            }
        });
        //signup button

    }



    public void signup(){
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    }

    public void login() {
        EditText e1 = (EditText) findViewById(R.id.emailText);
        EditText e2 = (EditText) findViewById(R.id.passwordText);
        final String email = e1.getText().toString();
        final String password = e2.getText().toString();
        if (email.isEmpty()) {
            e1.setError("Enter email");
            e1.requestFocus();
        } else if (password.isEmpty()) {
            e2.setError("Enter password");
            e2.requestFocus();
        } else {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //check if email is in users database
                                DatabaseReference dbusers;
                                dbusers = FirebaseDatabase.getInstance().getReference("users");
                                dbusers.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                            User user = snapshot2.getValue(User.class);
                                            if (email.equals(user.getEmail())) {
                                                //if email is in users direct to the user homepage
                                                Intent intent = new Intent(Login.this, HomeMenu.class);
                                                intent.putExtra("username", user.getEmail());
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                //check if email is in store database
                                DatabaseReference dbStore;
                                dbStore = FirebaseDatabase.getInstance().getReference("store");
                                dbStore.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        Store c = snapshot.getValue(Store.class);
                                        if (email.equals(c.getEmail())) {

                                            Intent intent = new Intent(Login.this, StockControl.class);
                                            intent.putExtra("username", c.getEmail());
                                            startActivity(intent);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Login unsuccessful", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }
    }
}