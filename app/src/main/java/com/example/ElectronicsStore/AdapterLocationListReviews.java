package com.example.ElectronicsStore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;

public class AdapterLocationListReviews extends RecyclerView.Adapter<AdapterLocationListReviews.MyViewHolder> {
    private ArrayList<Review> mylistvalues;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView comment;   //textview of row layout
        public TextView rating;   //textview of row layout


        public MyViewHolder(View itemView){
            super(itemView);
            comment=(TextView) itemView.findViewById(R.id.CommentOfRating);
            rating=(TextView) itemView.findViewById(R.id.RatingOfReview);
        }
    }

    //constructor of MyAdapterclass-Provide the dataset to the Adapter
    // myDataset is passed when called to create an adapter object
    public AdapterLocationListReviews(ArrayList<Review> myDataset) {
        mylistvalues= myDataset;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public AdapterLocationListReviews.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view –create a row –inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.row_layout_review, parent, false);
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterLocationListReviews.MyViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        // -get element from your dataset at this position
        // -replace the contents of the view with that element
        final int rating = mylistvalues.get(position).getRating();  // name variable of title
        final String comment = mylistvalues.get(position).getComment();  // name variable of date

        holder.comment.setText(comment);
        holder.rating.setText(rating+"/5 stars");

    }





    @Override
    public int getItemCount() {
        return mylistvalues.size();
    }

    public void remove(int position){
        mylistvalues.remove(position);
        notifyItemRemoved(position);}

    public void addItemtoend(Review n){
        mylistvalues.add(n);
        notifyItemInserted(mylistvalues.size());
    }
    public void clear() {
        int size = mylistvalues.size();
        mylistvalues.clear();
        notifyItemRangeRemoved(0, size);
    }
}
