package com.realestateadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.realestateadmin.BrokerDetailsActivity;
import com.realestateadmin.Models.Broker;
import com.realestateadmin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrokersAdapter extends RecyclerView.Adapter<BrokersAdapter.ViewHolder> {
    private List<Broker> brokersList;
    private Context context;

    public BrokersAdapter(Context context, List<Broker> brokersList) {
        this.context = context;
        this.brokersList = brokersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_broker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Broker broker = brokersList.get(position);
        holder.bind(broker);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrokerDetailsActivity.class);
                intent.putExtra("brokerId", broker.getBorkerid());
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(broker);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brokersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, locationTextView;
        ImageView leftImageView;
        ProgressBar loadingIndicator;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.username);
            emailTextView = itemView.findViewById(R.id.userid);
            locationTextView = itemView.findViewById(R.id.number);
            leftImageView = itemView.findViewById(R.id.leftImageView);
            loadingIndicator = itemView.findViewById(R.id.loadingIndicator);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Broker broker) {
            nameTextView.setText(broker.getName());
            emailTextView.setText(broker.getEmail());
            locationTextView.setText(broker.getPhone());

            loadingIndicator.setVisibility(View.VISIBLE);

            Picasso.get().load(broker.getImageurl()).into(leftImageView, new Callback() {
                @Override
                public void onSuccess() {
                    loadingIndicator.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    loadingIndicator.setVisibility(View.GONE);
                }
            });
        }
    }

    private void showDeleteConfirmationDialog(Broker broker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Broker");
        builder.setMessage("Are you sure you want to delete this broker?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBroker(broker);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteBroker(Broker broker) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Broker");
        databaseReference.child(broker.getBorkerid()).removeValue()
                .addOnSuccessListener(task -> {

                    brokersList.remove(broker);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {

                });
    }
}
