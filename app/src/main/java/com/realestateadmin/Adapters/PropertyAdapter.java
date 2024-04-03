package com.realestateadmin.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Models.Property;
import com.realestateadmin.PropertyMoreDetails_Activity;
import com.realestateadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private List<Property> properties;
    private Context context;

    public PropertyAdapter(Context context, List<Property> properties) {
        this.context = context;
        this.properties = properties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = properties.get(position);
        holder.areaTextView.setText(property.getPropertyName());
        holder.cityTextView.setText(property.getCity());
        holder.countryTextView.setText(property.getRooms() + " Bed ");
        holder.depositTextView.setText(property.getFloors() + " Floor");
        holder.floorsTextView.setText(property.getRoomType());
        holder.rent.setText("Rs." + property.getRent());

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PropertyMoreDetails_Activity.class);
                intent.putExtra("propertyId", property.getPropertyId());
                context.startActivity(intent);            }
        });

        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("Property_images");
        imagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String propertyId = childSnapshot.child("propertyId").getValue(String.class);
                    if (propertyId != null && propertyId.equals(property.getPropertyId())) {
                        String imageUrl = childSnapshot.child("imageurl").getValue(String.class);
                        if (imageUrl != null) {
                            Picasso.get().load(imageUrl).into(holder.leftIcon);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView areaTextView, cityTextView, countryTextView, depositTextView, floorsTextView,rent;

        LinearLayout linear;

        ImageView leftIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            areaTextView = itemView.findViewById(R.id.propertyname);
            cityTextView = itemView.findViewById(R.id.location);
            countryTextView = itemView.findViewById(R.id.noofrooms);
            depositTextView = itemView.findViewById(R.id.floor);
            floorsTextView = itemView.findViewById(R.id.roomtype);
            rent = itemView.findViewById(R.id.rent);
            linear = itemView.findViewById(R.id.linear);
            leftIcon = itemView.findViewById(R.id.leftIcon);

        }
    }
}

