package com.example.ElectronicsStore;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AdapterLocationList2 extends RecyclerView.Adapter<AdapterLocationList2.MyViewHolder> {
    private ArrayList<Store> mylistvalues;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Name;   //textview of row layout

        public MyViewHolder(View itemView){
            super(itemView);
            Name=(TextView) itemView.findViewById(R.id.NameOfItem);
        }
    }

    //constructor of MyAdapterclass-Provide the dataset to the Adapter
    // myDataset is passed when called to create an adapter object
    public AdapterLocationList2(ArrayList<Store> myDataset) {
        mylistvalues= myDataset;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public AdapterLocationList2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view –create a row –inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.store_row_layout, parent, false);
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterLocationList2.MyViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String title = mylistvalues.get(position).getName();  // name variable of title

        holder.Name.setText(title);


        holder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(), HomeMenu.class);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mylistvalues.size();
    }

    public void remove(int position){
        mylistvalues.remove(position);
        notifyItemRemoved(position);}

    public void addItemtoend(Store n){
        mylistvalues.add(n);
        notifyItemInserted(mylistvalues.size());
    }
    public void clear() {
        int size = mylistvalues.size();
        mylistvalues.clear();
        notifyItemRangeRemoved(0, size);
    }
}
