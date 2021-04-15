package com.example.ElectronicsStore;

import android.os.Bundle;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterLocationListOrders extends RecyclerView.Adapter<AdapterLocationListOrders.MyViewHolder> {
    private ArrayList<Order> mylistvalues;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    DatabaseReference fireDB;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView date;   //textview of row layout
        public TextView price;   //textview of row layout


        public MyViewHolder(View itemView){
            super(itemView);
            date=(TextView) itemView.findViewById(R.id.CommentOfRating);
            price=(TextView) itemView.findViewById(R.id.RatingOfReview);
        }
    }

    //constructor of MyAdapterclass-Provide the dataset to the Adapter
    // myDataset is passed when called to create an adapter object
    public AdapterLocationListOrders(ArrayList<Order> myDataset) {
        mylistvalues= myDataset;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public AdapterLocationListOrders.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view –create a row –inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.row_layout_order, parent, false);
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterLocationListOrders.MyViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        // -get element from your dataset at this position
        // -replace the contents of the view with that element
        final double price = mylistvalues.get(position).getPrice();  // name variable of title
        final Date date = mylistvalues.get(position).getDate();  // name variable of date
        Log.i("Date",""+price);
        final ArrayList<Item> items = mylistvalues.get(position).getItems();
        final String d = new SimpleDateFormat("dd-MM-yyyy").format(date);
        holder.date.setText(d);
        holder.price.setText("$"+String.valueOf(price));
        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(view.getContext(), title, Toast.LENGTH_LONG).show();
                ClickedOrderDialog dialog = new ClickedOrderDialog();


                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)items);
                dialog.setArguments(args);
                dialog.show(((ClickedCustomer)view.getContext()).getSupportFragmentManager(),"items");

              //  intent.putExtra("BUNDLE",args);
               // view.getContext().startActivity(intent);
             //   intent.putExtra("title",mylistvalues.get(position).getTitle() );
              //  intent.putExtra("message", mylistvalues.get(position).getMessage() );
              //  intent.putExtra("date", d );

               // view.getContext().startActivity(new Intent(view.getContext(), clickedNote.class));
             //   view.getContext().startActivity(intent);

            }
            /*
                dialogBuilder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater= view.getContext().getLayoutInflater();
                final View popupview = getLayoutInflater().inflate(R.layout.popup, null);



                dialogBuilder.setView(popupview);
                dialog = dialogBuilder.create();
                dialog.show();

                Button button = (Button) popupview.findViewById(R.id.pay);
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        dialog.dismiss();
                    }
                });
            }*/
        });

  /*      holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "delete", Toast.LENGTH_LONG).show();
                //remove(mylistvalues.get(position));
                mylistvalues.remove(mylistvalues.get(position));


                Query q2 = FirebaseDatabase.getInstance().getReference().child("Users").child("Notes").orderByChild("message").equalTo(mylistvalues.get(position).getMessage());

                q2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s: snapshot.getChildren()) {
                            s.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                notifyDataSetChanged();
            }

        });*/
    }





    @Override
    public int getItemCount() {
        return mylistvalues.size();
    }

    public void remove(int position){
        mylistvalues.remove(position);
        notifyItemRemoved(position);}

    public void addItemtoend(Order n){
        mylistvalues.add(n);
        notifyItemInserted(mylistvalues.size());
    }
    public void clear() {
        int size = mylistvalues.size();
        mylistvalues.clear();
        notifyItemRangeRemoved(0, size);
    }
}
