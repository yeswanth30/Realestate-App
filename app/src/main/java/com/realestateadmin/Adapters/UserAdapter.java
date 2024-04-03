package com.realestateadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.realestateadmin.Models.User;
import com.realestateadmin.R;
import com.realestateadmin.UserDetails_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewUsername.setText(user.getUsername());
        holder.textViewAddress.setText(user.getEmail());
        holder.textViewBio.setText(user.getPhone());

        Picasso.get().load(user.getImageurl()).into(holder.imageView);

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetails_Activity.class);
                intent.putExtra("userId", user.getUserid());
                context.startActivity(intent);
            }
        });

        // Set click listener for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewAddress, textViewBio, textViewCity;
        ImageView imageView;
        LinearLayout linear;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.leftImageView);
            textViewUsername = itemView.findViewById(R.id.username);
            textViewAddress = itemView.findViewById(R.id.userid);
            textViewBio = itemView.findViewById(R.id.number);
            linear = itemView.findViewById(R.id.linear);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // Method to show delete confirmation dialog
    private void showDeleteConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this user?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBroker(user);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Method to delete a user
    private void deleteBroker(User user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUserid()).removeValue()
                .addOnSuccessListener(task -> {
                    // User deleted successfully from the database
                    // Now remove it from the local list and notify the adapter
                    userList.remove(user);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Failed to delete user from the database
                    // Handle the failure, if needed
                    Toast.makeText(context, "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
