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
import com.realestateadmin.Models.City;
import com.realestateadmin.R;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<City> cityList;
    private DatabaseReference databaseReference;

    public CityAdapter(List<City> cityList) {
        this.cityList = cityList;
        this.databaseReference = FirebaseDatabase.getInstance().getReference().child("cities");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        City city = cityList.get(position);
        holder.textViewCityName.setText(city.getName());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndDeleteCity(context, city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCityName;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCityName = itemView.findViewById(R.id.textViewCityName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void checkAndDeleteCity(Context context, City city) {
        // Check if the city is associated with properties
        cityIsAssociated(city.getName(), new CityCheckCallback() {
            @Override
            public void onResult(boolean associated) {
                if (associated) {
                    // City is associated with properties, show message that city cannot be deleted
                    Toast.makeText(context, "This city is associated with properties and cannot be deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // City is not associated with properties, show dialog for confirmation
                    showDeleteDialog(context, city);
                }
            }
        });
    }


    private void cityIsAssociated(String cityName, CityCheckCallback callback) {
        DatabaseReference propertyReference = FirebaseDatabase.getInstance().getReference().child("Property");
        Query query = propertyReference.orderByChild("city").equalTo(cityName);

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

    private void showDeleteDialog(Context context, City city) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this city?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCity(city);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteCity(City city) {
        cityList.remove(city);
        notifyDataSetChanged();
        // Remove city from Firebase database
        databaseReference.child(city.getId()).removeValue();
       // Toast.makeText(context, "City deleted successfully", Toast.LENGTH_SHORT).show();
    }

    interface CityCheckCallback {
        void onResult(boolean associated);
    }
}
