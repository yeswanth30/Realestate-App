package com.realestateadmin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.DishImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class PropertyMoreDetails_Activity extends AppCompatActivity {

    TextView propertyNameTextView, cityTextView, rentTextView, nearbyPlaceTextView, verify,Bedrooms,Gym,Parking,Floors;
    DatabaseReference propertyRef;
    String propertyId;
    ViewPager viewPager;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propertymoredetails);
        viewPager = findViewById(R.id.viewPager);
        propertyNameTextView = findViewById(R.id.name);
        cityTextView = findViewById(R.id.loaction);
        rentTextView = findViewById(R.id.price);
        nearbyPlaceTextView = findViewById(R.id.describe);
        verify = findViewById(R.id.verify);
        Gym = findViewById(R.id.gym);
        Bedrooms = findViewById(R.id.bedrooms);
        Floors = findViewById(R.id.floors);
        Parking = findViewById(R.id.parking);

        propertyId = getIntent().getStringExtra("propertyId");

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyMoreDetails_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        propertyRef = FirebaseDatabase.getInstance().getReference().child("Property").child(propertyId);
        propertyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int status = dataSnapshot.child("status").getValue(Integer.class);
                    String buttonText = (status == 1) ? "Verified" : "Verify";
                    verify.setText(buttonText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("VerifiedBrokerDetails", "Error fetching verification status: " + databaseError.getMessage());
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current status from button text
                String buttonText = verify.getText().toString();
                int currentStatus = buttonText.equals("Verified") ? 1 : 0;

                // Toggle status
                int newStatus = (currentStatus == 0) ? 1 : 0;

                // Update button text based on new status
                String newButtonText = (newStatus == 1) ? "Verified" : "Verify";
                verify.setText(newButtonText);

                // Update status in the database
                propertyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dataSnapshot.getRef().child("status").setValue(newStatus)
                                    .addOnSuccessListener(aVoid -> {
                                        String message = (newStatus == 1) ? "Verification status updated successfully" : "Verification status cancelled successfully";
                                        Toast.makeText(PropertyMoreDetails_Activity.this, message, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("VerifiedBrokerDetails", "Error updating verification status: " + e.getMessage());
                                        Toast.makeText(PropertyMoreDetails_Activity.this, "Failed to update verification status", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(PropertyMoreDetails_Activity.this, "Property details not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("VerifiedBrokerDetails", "Error fetching broker details: " + databaseError.getMessage());
                        Toast.makeText(PropertyMoreDetails_Activity.this, "Failed to fetch broker details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (propertyId != null) {
            propertyRef = FirebaseDatabase.getInstance().getReference().child("Property").child(propertyId);

            propertyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String propertyName = dataSnapshot.child("propertyName").getValue(String.class);
                        String city = dataSnapshot.child("city").getValue(String.class);
                        String rent = dataSnapshot.child("rent").getValue(String.class);
                        String nearbyPlace = dataSnapshot.child("nearbyPlace").getValue(String.class);

                        String bedrooms = dataSnapshot.child("rooms").getValue(String.class);
                        String floors = dataSnapshot.child("floors").getValue(String.class);
                        String parking = dataSnapshot.child("parking").getValue(String.class);
                        String gym = dataSnapshot.child("gym").getValue(String.class);

                        propertyNameTextView.setText(propertyName);
                        cityTextView.setText(city);
                        rentTextView.setText("Rs." + rent);
                        nearbyPlaceTextView.setText(nearbyPlace);
                        Bedrooms.setText(bedrooms);
                        Floors.setText(floors);
                        Parking.setText(parking);
                        Gym.setText( gym);
                    } else {
                        Toast.makeText(PropertyMoreDetails_Activity.this, "Property details not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("PropertyMoreDetails", "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Property ID is null", Toast.LENGTH_SHORT).show();
            Log.e("PropertyMoreDetails", "Property ID is null");
        }

        fetchImageUrlsAndSetupViewPager();
    }

    private void fetchImageUrlsAndSetupViewPager() {
        DatabaseReference imagesRef = FirebaseDatabase.getInstance().getReference("Property_images");

        imagesRef.orderByChild("propertyId").equalTo(propertyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> imageUrls = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String imageUrl = snapshot.child("imageurl").getValue(String.class);
                    imageUrls.add(imageUrl);
                }
                DishImageAdapter adapter = new DishImageAdapter(imageUrls, PropertyMoreDetails_Activity.this);
                viewPager.setAdapter(adapter);

                setupDotIndicators(imageUrls.size());

                if (imageUrls.isEmpty()) {
                    // Handle when there are no images
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void setupDotIndicators(int count) {
        LinearLayout dotsLayout = findViewById(R.id.dotsLayout);
        ImageView[] dots = new ImageView[count];

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.ic_dot_inactive);

            final int position = i;
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position);
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }

        if (dots.length > 0) {
            dots[0].setImageResource(R.drawable.ic_dot_active);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < count; i++) {
                    dots[i].setImageResource(R.drawable.ic_dot_inactive);
                }
                dots[position].setImageResource(R.drawable.ic_dot_active);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}

