package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class ClickedItemCustomer extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;
    DatabaseReference fireDBUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bm;
    Item item;
    int stars=0;

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

        final Button seeReviews = findViewById(R.id.SeeReviewButton);
        seeReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeReviews(itemID);
            }
        });


        final Button leaveReview = findViewById(R.id.LeaveAReviewButton);
        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveReview();
            }
        });




        fireDB= FirebaseDatabase.getInstance().getReference("store").child("items").child(itemID);
        fireDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 item = snapshot.getValue(Item.class);
                name.setText(item.getName());
                category.setText(item.getCategory());
                price.setText("$"+Double.toString(item.getPrice()));
                manufacturer.setText(item.getManufacturer());
                setImage(itemID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Button addTocart = findViewById(R.id.UpdateButton);
        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireDBUser= FirebaseDatabase.getInstance().getReference("users").child(UserID);
                fireDBUser.child("cart").push().setValue(item);
                Toast.makeText(ClickedItemCustomer.this, "Item added to cart", Toast.LENGTH_LONG);
                startActivity(new Intent(ClickedItemCustomer.this, HomeMenu.class));

            }
        });


    }

    private void setImage(String itemID) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        final ImageView imageView = (ImageView)findViewById(R.id.itemImage2);
        String childref = "images/store/" + itemID;

        StorageReference ref = storageReference.child(childref);

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                imageView.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors

            }

        });
    }

    private void seeReviews(String itemID) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_all_reviews_dialog, null);

        final ArrayList<Review> myDataset= new ArrayList<>();
        final AdapterLocationListReviews mAdapter;
        DatabaseReference fireDBreviews;

        mAdapter= new AdapterLocationListReviews(myDataset);
        RecyclerView mRecyclerView= (RecyclerView) dialogView.findViewById(R.id.reviews);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(dialogView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dialogView.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        fireDBreviews = FirebaseDatabase.getInstance().getReference();

        fireDBreviews.child("store").child("items").child(itemID).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Review review = snapshot1.getValue(Review.class);
                    mAdapter.addItemtoend(review);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialogBuilder.setView(dialogView).setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

            }
        });

        dialogBuilder.create();
        dialogBuilder.show();

    }

    private void leaveReview() {


            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.add_review_dialog, null);
            final TextView star1 = (TextView)dialogView.findViewById(R.id.Star1);
            final TextView star2 = (TextView)dialogView.findViewById(R.id.Star2);
            final TextView star3 = (TextView)dialogView.findViewById(R.id.Star3);
            final TextView star4 = (TextView)dialogView.findViewById(R.id.Star4);
            final TextView star5 = (TextView)dialogView.findViewById(R.id.Star5);

            final EditText review = (EditText) dialogView.findViewById(R.id.reviewText);



            star1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    star1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star2.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    star3.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    star4.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    star5.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    stars=1;
                }
            });

            star2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    star1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star3.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    star4.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    star5.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    stars=2;
                }
            });
            star3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    star1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star3.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star4.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    star5.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    stars=3;
                }
            });
            star4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    star1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star3.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star4.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star5.setBackground(getResources().getDrawable(R.drawable.star_reviews));
                    stars=4;
                }
            });
            star5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    star1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star3.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star4.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    star5.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                    stars=5;
                }
            });


            dialogBuilder.setView(dialogView).setPositiveButton("Submit Feedback", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    if(stars!=0){
                        Review aReview = new Review(stars, review.getText().toString());
                        fireDB.child("reviews").push().setValue(aReview);
                        dialog.dismiss();
                        Toast.makeText(ClickedItemCustomer.this, "thank you for the review", Toast.LENGTH_LONG);
                    }
                    else {
                        star1.setError("choose a star rating first");
                        star1.requestFocus();
                    }
                }
            }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialogBuilder.create();
            dialogBuilder.show();

    }
}