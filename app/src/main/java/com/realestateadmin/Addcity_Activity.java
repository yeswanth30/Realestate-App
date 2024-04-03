package com.realestateadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.CityAdapter;
import com.realestateadmin.Models.City;

import java.util.ArrayList;
import java.util.List;

public class Addcity_Activity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonAddCity;
    ImageView back;

    private DatabaseReference databaseReference;

    RecyclerView recyclerView;
    CityAdapter adapter;
    List<City> cityList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcity);

        editTextCity = findViewById(R.id.editTextCity);
        buttonAddCity = findViewById(R.id.buttonAddCity);
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycleview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Addcity_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cities");

        buttonAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCityToDatabase();
            }
        });

        // Initialize cityList and adapter
        cityList = new ArrayList<>();
        adapter = new CityAdapter(cityList);

        // Set layout manager and adapter for RecyclerView
        recyclerView.setLayoutManager(new SingleItemLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Call method to fetch cities
        fetchCities();
    }

    private void fetchCities() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cityList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    City city = snapshot.getValue(City.class);
                    cityList.add(city);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addCityToDatabase() {
        String cityName = editTextCity.getText().toString().trim();

        if (!cityName.isEmpty()) {
            String cityId = databaseReference.push().getKey();
            City city = new City(cityId, cityName);
            databaseReference.child(cityId).setValue(city);
            Toast.makeText(this, "City added successfully", Toast.LENGTH_SHORT).show();
            editTextCity.setText("");
        } else {
            Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
        }
    }
}




