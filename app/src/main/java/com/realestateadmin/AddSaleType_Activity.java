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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.SaleTypeAdapter;
import com.realestateadmin.Models.SaleType;

import java.util.ArrayList;
import java.util.List;

public class AddSaleType_Activity extends AppCompatActivity {

    private EditText editTextSaleType;
    private Button buttonAddSaleType;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private SaleTypeAdapter adapter;

    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_type);

        editTextSaleType = findViewById(R.id.editTextSaleType);
        buttonAddSaleType = findViewById(R.id.buttonAddSaleType);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back = findViewById(R.id.back);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("sale_types");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSaleType_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonAddSaleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSaleTypeToFirebase();
            }
        });

        adapter = new SaleTypeAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadSaleTypes();
    }

    private void loadSaleTypes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<SaleType> saleTypes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SaleType saleType = snapshot.getValue(SaleType.class);
                    if (saleType != null) {
                        saleTypes.add(saleType);
                    }
                }
                // Update adapter with the fetched sale types
                adapter.updateSaleTypes(saleTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddSaleType_Activity.this, "Failed to load sale types", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void addSaleTypeToFirebase() {
        String saleType = editTextSaleType.getText().toString().trim();
        if (!saleType.isEmpty()) {
            String saleTypeId = databaseReference.push().getKey();

            SaleType newSaleType = new SaleType(saleTypeId, saleType);

            databaseReference.child(saleTypeId).setValue(newSaleType, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        Toast.makeText(AddSaleType_Activity.this, "Sale Type Added Successfully", Toast.LENGTH_SHORT).show();
                        editTextSaleType.setText("");
                    } else {
                        Toast.makeText(AddSaleType_Activity.this, "Failed to Add Sale Type: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter a sale type", Toast.LENGTH_SHORT).show();
        }
    }
}
