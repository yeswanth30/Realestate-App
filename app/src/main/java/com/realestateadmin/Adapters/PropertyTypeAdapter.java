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
import com.realestateadmin.Models.PropertyType;
import com.realestateadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PropertyTypeAdapter extends RecyclerView.Adapter<PropertyTypeAdapter.ViewHolder> {

    private List<PropertyType> propertyTypeList;
    private DatabaseReference databaseReference;

    public PropertyTypeAdapter(List<PropertyType> propertyTypeList) {
        this.propertyTypeList = propertyTypeList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Property_Types");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_type_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        PropertyType propertyType = propertyTypeList.get(position);
        holder.textViewPropertyType.setText(propertyType.getType());

        Picasso.get().load(propertyType.getImageUrl()).into(holder.imageView);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndDeletePropertyType(context, propertyType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPropertyType;
        ImageView deleteButton;

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPropertyType = itemView.findViewById(R.id.textViewPropertyType);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            imageView = itemView.findViewById(R.id.leftIcon);

        }
    }

    private void checkAndDeletePropertyType(Context context, PropertyType propertyType) {
        propertyTypeIsAssociated(propertyType.getType(), new PropertyTypeCheckCallback() {
            @Override
            public void onResult(boolean associated) {
                if (associated) {
                    Toast.makeText(context, "This property type is associated with properties and cannot be deleted", Toast.LENGTH_SHORT).show();
                } else {
                    showDeleteDialog(context, propertyType);
                }
            }
        });
    }

    private void propertyTypeIsAssociated(String propertyType, PropertyTypeCheckCallback callback) {
        DatabaseReference propertyReference = FirebaseDatabase.getInstance().getReference().child("Property");
        Query query = propertyReference.orderByChild("propertyType").equalTo(propertyType);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onResult(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onResult(false);
            }
        });
    }

    private void showDeleteDialog(Context context, PropertyType propertyType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this property type?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePropertyType(propertyType);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deletePropertyType(PropertyType propertyType) {
        propertyTypeList.remove(propertyType);
        notifyDataSetChanged();
        databaseReference.child(propertyType.getId()).removeValue();
    }

    interface PropertyTypeCheckCallback {
        void onResult(boolean associated);
    }
}
