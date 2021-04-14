package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ClickedItemStore extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;
    DatabaseReference fireDBUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bm;//bitmap of image
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_item_store);
        final Intent intent = getIntent();
        final String itemID = intent.getStringExtra("ItemID");

        final TextView name = findViewById(R.id.ItemName2);
        final TextView category = findViewById(R.id.ItemCategory2);
        final TextView price = findViewById(R.id.ItemPrice2);
        final TextView manufacturer = findViewById(R.id.ItemManufacturer2);
        final TextView stock = findViewById(R.id.ItemStock2);
        final TextView description = findViewById(R.id.ItemDescription2);
        ImageView image = findViewById(R.id.imageView2);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String UserID=mUser.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        fireDB= FirebaseDatabase.getInstance().getReference("store").child("items").child(itemID);
        fireDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                item = snapshot.getValue(Item.class);
                name.setText(item.getName());
                category.setText(item.getCategory());
                price.setText(Double.toString(item.getPrice()));
                manufacturer.setText(item.getManufacturer());
                stock.setText(Integer.toString(item.getStock()));
                description.setText(item.getDescription());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

setImage(image,itemID);


        Button update = findViewById(R.id.UpdateButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireDBUser= FirebaseDatabase.getInstance().getReference("store").child("items").child(itemID);

                fireDBUser.child("name").setValue(name.getText().toString());
                fireDBUser.child("category").setValue(category.getText().toString());
                fireDBUser.child("manufacturer").setValue(manufacturer.getText().toString());
                fireDBUser.child("description").setValue(description.getText().toString());
                int stocklevel = Integer.parseInt(stock.getText().toString());
                fireDBUser.child("stock").setValue(stocklevel);
                fireDBUser.child("price").setValue(Double.parseDouble(price.getText().toString()));

            }
        });


    }

    private void setImage(final ImageView image, String itemID) {

        final String childref = "images/bookings/" + itemID;

        // Defining the child of storageReference
        StorageReference ref = storageReference.child(childref);

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                image.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }

        });
    }
}