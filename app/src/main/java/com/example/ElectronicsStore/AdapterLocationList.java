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

public class AdapterLocationList extends RecyclerView.Adapter<AdapterLocationList.MyViewHolder> {
    private ArrayList<Item> mylistvalues;
    private String Person;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Name;   //textview of row layout

        public TextView Price;   //textview of row layout




        public MyViewHolder(View itemView){
            super(itemView);
            Name=(TextView) itemView.findViewById(R.id.CommentOfRating);
            Price=(TextView) itemView.findViewById(R.id.RatingOfReview);





        }
    }

    //constructor of MyAdapterclass-Provide the dataset to the Adapter
    // myDataset is passed when called to create an adapter object
    public AdapterLocationList(ArrayList<Item> myDataset, String person) {
        mylistvalues= myDataset;
        this.Person=person;

    }

    //Create new views (invoked by the layout manager)
    @Override
    public AdapterLocationList.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view –create a row –inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.row_layout2, parent, false);
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterLocationList.MyViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        // -get element from your dataset at this position
        // -replace the contents of the view with that element
        final String title = mylistvalues.get(position).getName();  // name variable of title
        final double price = mylistvalues.get(position).getPrice();  // name variable of date
        final String p = Double.toString(price);

        holder.Name.setText(title);
        holder.Price.setText("$ "+p);


        holder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Person.equals("customer")){
                    Intent intent= new Intent(view.getContext(), ClickedItemCustomer.class);
                    intent.putExtra("ItemID", mylistvalues.get(position).getId());
                    view.getContext().startActivity(intent);
                }else if (Person.equals("store")){
                    Intent intent= new Intent(view.getContext(), ClickedItemStore.class);
                    intent.putExtra("ItemID", mylistvalues.get(position).getId());
                    view.getContext().startActivity(intent);

                }
                else{

                }

              //  intent.putExtra("StoreID", mylistvalues.get(position).);
              //  view.getContext().startActivity(intent);
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

    public void addItemtoend(Item n){
        mylistvalues.add(n);
        notifyItemInserted(mylistvalues.size());
    }
    public void clear() {
        int size = mylistvalues.size();
        mylistvalues.clear();
        notifyItemRangeRemoved(0, size);
    }
}
