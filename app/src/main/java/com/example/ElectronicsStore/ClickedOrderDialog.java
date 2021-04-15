package com.example.ElectronicsStore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClickedOrderDialog extends AppCompatDialogFragment {
    final ArrayList<Item> myDataset= new ArrayList<>();
    AdapterLocationList mAdapter;
    DatabaseReference fireDB;
    ArrayList<Item> items =new ArrayList<>();


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflator = getActivity().getLayoutInflater();
        View view = inflator.inflate(R.layout.activity_clicked_order_dialog, null);

        if (getArguments() != null) {
            items = (ArrayList<Item>)getArguments().getSerializable("ARRAYLIST");


        }
        //Bundle args = intent.getBundleExtra("BUNDLE");
        //ArrayList<Object> object = (ArrayList<Object>) args.getSerializable("ARRAYLIST");

        mAdapter= new AdapterLocationList(myDataset, "notcustomer");
        RecyclerView mRecyclerView= (RecyclerView) view.findViewById(R.id.reviews);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager= new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        fireDB = FirebaseDatabase.getInstance().getReference();

        recyclerView();

        builder.setView(view).setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }

    private void recyclerView() {
                for(int i =0; i<items.size();i++) {
                    mAdapter.addItemtoend(items.get(i));
                }
    }
}