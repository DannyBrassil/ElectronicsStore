package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth1;
    public DatabaseReference db;
    public boolean validdeatils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth1 = FirebaseAuth.getInstance();

        Button button = (Button) findViewById(R.id.signupbutton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createAccount();
            }
        });
    }

    public void createAccount(){
        validdeatils=true;
        EditText e1 = (EditText) findViewById(R.id.emailText);
        EditText e2 = (EditText) findViewById(R.id.passwordText);
        EditText e3 = (EditText) findViewById(R.id.firstnametext);
        EditText e4 = (EditText) findViewById(R.id.secondnametext);
        EditText e5 = (EditText) findViewById(R.id.number);
        EditText e6 = (EditText) findViewById(R.id.confirmpasswordText);

        EditText e7 = (EditText) findViewById(R.id.address);
        EditText e8 = (EditText) findViewById(R.id.address5);
        EditText e9 = (EditText) findViewById(R.id.address4);
        Spinner aSpinner=findViewById(R.id.spinnerCounty2);
        final String county = aSpinner.getSelectedItem().toString();


        final String firstname = e3.getText().toString();
        final String email = e4.getText().toString();
        final String password = e2.getText().toString();
        final String number = e5.getText().toString();

        final String passwordconfirm = e6.getText().toString();

        final String addressLine1 = e7.getText().toString();
        final String addressLine2 = e8.getText().toString();
        final String addressLine3 = e9.getText().toString();



        if(email.isEmpty()){
            e1.setError("Enter email");
            validdeatils=false;
            e1.requestFocus();
        }
        else if(password.isEmpty()){
            e2.setError("Enter password");
            validdeatils=false;
            e2.requestFocus();
        }
        else if(firstname.isEmpty()){
            e3.setError("Enter name");
            validdeatils=false;
            e3.requestFocus();
        }
        else if(number.isEmpty()){
            e5.setError("Enter number");
            validdeatils=false;
            e5.requestFocus();
        }
        else if(passwordconfirm.isEmpty()){
            e6.setError("confirm password");
            validdeatils=false;
            e6.requestFocus();
        }

        if (!password.equals(passwordconfirm)){
            validdeatils=false;
            e6.setError("passwords do not match");
            e6.requestFocus();
        }




        if(validdeatils){
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
                            db= FirebaseDatabase.getInstance().getReference(); // get reference from root

                            Address address = new Address(addressLine1, addressLine2, addressLine3, county);

                            User person = new User(email, password, firstname, number, address);

                            db.child("Users").child(uid).setValue(person).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUp.this, "sign up is successful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this, "sign up is not successful", Toast.LENGTH_LONG).show();
                                }
                            });


                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                        }

                        // ...
                        //    }
                    });
        }
        else{
            Toast.makeText(SignUp.this, "Signup unsuccessful", Toast.LENGTH_LONG).show();
        }




    }



}