package com.realestateadmin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Models.RoomType;
import com.realestateadmin.R;

import java.util.List;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.ViewHolder> {

    private List<RoomType> roomTypeList;
    private DatabaseReference databaseReference;

    public RoomTypeAdapter(List<RoomType> roomTypeList) {
        this.roomTypeList = roomTypeList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("room_types");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_type_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        RoomType roomType = roomTypeList.get(position);
        holder.textViewRoomType.setText(roomType.getType());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndDeleteRoomType(context, roomType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRoomType;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomType = itemView.findViewById(R.id.textViewRoomType);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void checkAndDeleteRoomType(Context context, RoomType roomType) {
        // Check if the room type is associated with properties
        roomTypeIsAssociated(roomType.getType(), new RoomTypeCheckCallback() {
            @Override
            public void onResult(boolean associated) {
                if (associated) {
                    // RoomType is associated with properties, show message that it cannot be deleted
                    Toast.makeText(context, "This room type is associated with properties and cannot be deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // RoomType is not associated with properties, show dialog for confirmation
                    showDeleteDialog(context, roomType);
                }
            }
        });
    }

    private void roomTypeIsAssociated(String roomType, RoomTypeCheckCallback callback) {
        DatabaseReference propertyReference = FirebaseDatabase.getInstance().getReference().child("Property");
        Query query = propertyReference.orderByChild("roomType").equalTo(roomType);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onResult(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                callback.onResult(false); // Assume not associated in case of error
            }
        });
    }

    private void showDeleteDialog(Context context, RoomType roomType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this room type?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRoomType(roomType);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteRoomType(RoomType roomType) {
        roomTypeList.remove(roomType);
        notifyDataSetChanged();
        // Remove room type from Firebase database
        databaseReference.child(roomType.getId()).removeValue();
    }

    interface RoomTypeCheckCallback {
        void onResult(boolean associated);
    }
}
