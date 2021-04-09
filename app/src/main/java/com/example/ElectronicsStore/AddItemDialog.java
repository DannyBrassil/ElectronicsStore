package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static java.lang.Integer.parseInt;


public class AddItemDialog extends AppCompatDialogFragment {
    Bitmap bitmap;//qr code
    Uri imageUri;
    ImageView i1;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflator = getActivity().getLayoutInflater();
        View view = inflator.inflate(R.layout.activity_item_dialog, null);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


         final TextView name = (TextView) view.findViewById(R.id.ItemName);
        final TextView category = (TextView) view.findViewById(R.id.itemCategory);
        final TextView description = (TextView) view.findViewById(R.id.ItemDescription);
        final TextView manufacturer = (TextView) view.findViewById(R.id.itemManufacturer);
        final TextView price = (TextView) view.findViewById(R.id.ItemPrice);
        final TextView stock = (TextView) view.findViewById(R.id.itemStock);
        i1=view.findViewById(R.id.imageView2);
        Button addImage = (Button) view.findViewById(R.id.add_image_button);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i,1);
            }
        });




        builder.setView(view).setNegativeButton("Cancel         ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String n =  name.getText().toString();
                        final double p = Double.parseDouble(price.getText().toString());
                        final String c =category.getText().toString();
                        final String m =manufacturer.getText().toString();
                        final String d =description.getText().toString();
                        final int s =Integer.parseInt(stock.getText().toString());
                        final Item item = new Item( n, p, c, m, d, s, null );

                        uploadImage();

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser mUser = mAuth.getCurrentUser();

                        DatabaseReference fireDB;
                        fireDB = FirebaseDatabase.getInstance().getReference();
                        fireDB.child("stores").child(mUser.getUid()).child("items").push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Toast.makeText(this, "Write is successful", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Toast.makeText(notes.this, "Write is not successful", Toast.LENGTH_LONG).show();
                            }
                        });




                }
                });
        return builder.create();
    }

    //when image is selected by user
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        try{
            InputStream is = this.getContext().getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(is);
            i1.setImageBitmap(bitmap);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage() {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (bitmap != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            String childref="images/bookings/"+mUser.getUid();
            Log.i("ref",childref);
            // Defining the child of storageReference
            StorageReference ref = storageReference.child(childref);

            // adding listeners on upload

            //covert bitmap image of qr code to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });


        }
    }
}