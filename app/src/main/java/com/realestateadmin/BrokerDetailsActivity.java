package com.realestateadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.PropertyAdapter;
import com.realestateadmin.Models.Broker;
import com.realestateadmin.Models.Property;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrokerDetailsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    String brokerId;
    TextView verify;
    ImageView back;

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private List<Property> propertyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_details);

        verify=findViewById(R.id.verify);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.ImagesrecyclerView);

        propertyList = new ArrayList<>();
        adapter = new PropertyAdapter(this, propertyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Broker");

        brokerId = getIntent().getStringExtra("brokerId");

        if (brokerId != null) {
            fetchBrokerDetails(brokerId);
            fetchPropertiesForBroker(brokerId);

        } else {

            finish();
        }


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brokerId != null) {
                    Log.d("BrokerDetailsActivity", "Broker ID: " + brokerId);
                    Intent intent = new Intent(BrokerDetailsActivity.this, VerifiedBrokerDetails_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("brokerId", brokerId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Log.e("BrokerDetailsActivity", "Broker ID is null");
                }
            }
        });



    }

    private void fetchBrokerDetails(String brokerId) {
        mDatabase.child(brokerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Broker broker = dataSnapshot.getValue(Broker.class);
                    if (broker != null) {
                        bindBrokerDetails(broker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private void bindBrokerDetails(Broker broker) {
        ImageView imageView = findViewById(R.id.imageView);
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewPhone = findViewById(R.id.textViewPhone);
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        TextView textViewTimestamp = findViewById(R.id.textViewTimestamp);

        Picasso.get().load(broker.getImageurl()).into(imageView);
        textViewName.setText(broker.getName());
        textViewEmail.setText(broker.getEmail());
        textViewPhone.setText(broker.getPhone());
        textViewLocation.setText(broker.getLocation());
        textViewTimestamp.setText("Created At: " + broker.getTimestamp());

    }

    private void fetchPropertiesForBroker(String brokerId) {
        DatabaseReference propertyRef = FirebaseDatabase.getInstance().getReference().child("Property");

        propertyRef.orderByChild("brokerid").equalTo(brokerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Property property = snapshot.getValue(Property.class);
                        if (property != null) {
                            propertyList.add(property);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    TextView noPropertiesText = findViewById(R.id.noPropertiesText);
                    noPropertiesText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("BrokerDetailsActivity", "Database Error: " + databaseError.getMessage());
            }
        });
    }


}
