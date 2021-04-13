package com.example.ElectronicsStore;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterLocationList2 extends RecyclerView.Adapter<AdapterLocationList2.MyViewHolder> {
    private ArrayList<User> mylistvalues;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Name;   //textview of row layout
        public TextView orders;   //textview of row layout

        public MyViewHolder(View itemView){
            super(itemView);
            Name=(TextView) itemView.findViewById(R.id.NameOfCustomer);
            orders=(TextView) itemView.findViewById(R.id.textView11);
        }
    }

    //constructor of MyAdapterclass-Provide the dataset to the Adapter
    // myDataset is passed when called to create an adapter object
    public AdapterLocationList2(ArrayList<User> myDataset) {
        mylistvalues= myDataset;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public AdapterLocationList2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view –create a row –inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.customer_row_layout, parent, false);
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final AdapterLocationList2.MyViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String title = mylistvalues.get(position).getFirstName();  // name variable of title

        holder.Name.setText(title);

        final ArrayList<Order> orders = new ArrayList<>();
        fireDB = FirebaseDatabase.getInstance().getReference().child("users");
        fireDB.child(mylistvalues.get(position).getId()).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children: snapshot.getChildren()){
                    Order order = children.getValue(Order.class);
                    orders.add(order);
                }
                holder.orders.setText(orders.size()+" orders");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(), ClickedCustomer.class);
                intent.putExtra("CustomerID", mylistvalues.get(position).getId());
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

    public void addItemtoend(User n){
        mylistvalues.add(n);
        notifyItemInserted(mylistvalues.size());
    }
    public void clear() {
        int size = mylistvalues.size();
        mylistvalues.clear();
        notifyItemRangeRemoved(0, size);
    }
}
