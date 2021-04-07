package com.example.ElectronicsStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Integer.parseInt;

public class MenuDialog extends AppCompatDialogFragment {
    private String category;

    public MenuDialog(String c){
        this.category=c;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflator = getActivity().getLayoutInflater();
        View view = inflator.inflate(R.layout.activity_item_dialog, null);



         final TextView n = (TextView) view.findViewById(R.id.ItemName);
        final TextView p = (TextView) view.findViewById(R.id.ItemPrice);




        builder.setView(view).setNegativeButton("Cancel         ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("Make Booking", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String name =  n.getText().toString();
                        final double price = Double.parseDouble(p.getText().toString());
                        final String cat =category;
                        final Item item = new Item( name, price);


                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser mUser = mAuth.getCurrentUser();

                        DatabaseReference fireDB;
                        fireDB = FirebaseDatabase.getInstance().getReference();
                        fireDB.child("Restaurants").child(mUser.getUid()).child("Menu").push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}