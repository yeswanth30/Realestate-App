package com.realestateadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class VerifiedBrokerDetails_Activity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String brokerId;

    TextView Verify;
    ImageView back;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified_broker_details);

        Verify = findViewById(R.id.verify);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifiedBrokerDetails_Activity.this, BrokerDetailsActivity.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("verify_broker");

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            brokerId = extras.getString("brokerId");

            if (brokerId != null) {
                fetchVerifiedBrokerDetails(brokerId);
            } else {
                // Handle null brokerId
            }
        }

        // Set OnClickListener for Verify button
        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current status from button text
                String buttonText = Verify.getText().toString();
                int currentStatus = buttonText.equals("Verified") ? 1 : 0;

                // Toggle status
                int newStatus = (currentStatus == 0) ? 1 : 0;

                // Update button text based on new status
                String newButtonText = (newStatus == 1) ? "Verified" : "Verify";
                Verify.setText(newButtonText);

                // Update status in the database
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String verifiedBrokerId = snapshot.child("borkerid").getValue(String.class);
                                if (verifiedBrokerId != null && verifiedBrokerId.equals(brokerId)) {
                                    snapshot.getRef().child("status").setValue(newStatus)
                                            .addOnSuccessListener(aVoid -> {
                                                String message = (newStatus == 1) ? "Verification status updated successfully" : "Verification status cancelled successfully";
                                                Toast.makeText(VerifiedBrokerDetails_Activity.this, message, Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("VerifiedBrokerDetails", "Error updating verification status: " + e.getMessage());
                                                Toast.makeText(VerifiedBrokerDetails_Activity.this, "Failed to update verification status", Toast.LENGTH_SHORT).show();
                                            });
                                    return;
                                }
                            }
                        }
                        Toast.makeText(VerifiedBrokerDetails_Activity.this, "Broker ID not found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("VerifiedBrokerDetails", "Error fetching broker details: " + databaseError.getMessage());
                        Toast.makeText(VerifiedBrokerDetails_Activity.this, "Failed to fetch broker details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void fetchVerifiedBrokerDetails(String brokerId) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isBrokerVerified = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String verifiedBrokerId = snapshot.child("borkerid").getValue(String.class);
                        if (verifiedBrokerId != null && verifiedBrokerId.equals(brokerId)) {
                            int verificationStatus = snapshot.child("status").getValue(Integer.class);
                            updateVerifyButton(verificationStatus); // Update button text based on verification status
                            isBrokerVerified = true;

                            // Fetch other details if required
                            String aadhaarNumber = snapshot.child("aadhaar_number").getValue(String.class);
                            String aadhaarImageUrl = snapshot.child("aadhaar_image_url").getValue(String.class);
                            String panNumber = snapshot.child("pan_number").getValue(String.class);
                            String panImageUrl = snapshot.child("pan_image_url").getValue(String.class);

                            TextView aadhaarNumberTextView = findViewById(R.id.aadhaarNumberTextView);
                            TextView panNumberTextView = findViewById(R.id.panNumberTextView);
                            ImageView aadhaarImageView = findViewById(R.id.adhar);
                            ImageView panImageView = findViewById(R.id.pan);

                            aadhaarNumberTextView.setText(aadhaarNumber);
                            panNumberTextView.setText(panNumber);

                            Picasso.get().load(aadhaarImageUrl).into(aadhaarImageView);
                            Picasso.get().load(panImageUrl).into(panImageView);

                            Log.d("BrokerDetailsActivity", "Broker ID from Intent: " + brokerId);
                            Log.d("BrokerDetailsActivity", "Verified Broker ID: " + verifiedBrokerId);

                            break;
                        }
                    }
                    if (!isBrokerVerified) {
                        // Broker not found in database
                        Toast.makeText(VerifiedBrokerDetails_Activity.this, "Broker ID not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("BrokerDetailsActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(VerifiedBrokerDetails_Activity.this, "Failed to fetch broker details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVerifyButton(int verificationStatus) {
        if (verificationStatus == 1) {
            // Broker is verified
            Verify.setText("Verified");
        } else {
            // Broker is not verified
            Verify.setText("Verify");
        }
    }
}
