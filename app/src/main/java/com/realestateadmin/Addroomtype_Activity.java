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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.RoomTypeAdapter;
import com.realestateadmin.Models.RoomType;

import java.util.ArrayList;
import java.util.List;

public class Addroomtype_Activity extends AppCompatActivity {

    private EditText editTextRoomType;
    private Button buttonAddRoomType;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    private RoomTypeAdapter adapter;
    private List<RoomType> roomTypeList;

    private ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomtype);

        editTextRoomType = findViewById(R.id.editTextRoomType);
        buttonAddRoomType = findViewById(R.id.buttonAddRoomType);
        recyclerView = findViewById(R.id.recycleview);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Addroomtype_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("room_types");

        roomTypeList = new ArrayList<>();
        adapter = new RoomTypeAdapter(roomTypeList);

        recyclerView.setLayoutManager(new SingleItemLinearLayoutManager(this));
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        buttonAddRoomType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoomTypeToFirebase();
            }
        });

        loadRoomTypes();
    }

    private void addRoomTypeToFirebase() {
        String roomType = editTextRoomType.getText().toString().trim();
        if (!roomType.isEmpty()) {
            String roomTypeId = databaseReference.push().getKey();

            RoomType newRoomType = new RoomType(roomTypeId, roomType);

            databaseReference.child(roomTypeId).setValue(newRoomType, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        Toast.makeText(Addroomtype_Activity.this, "Room Type Added Successfully", Toast.LENGTH_SHORT).show();
                        editTextRoomType.setText("");
                    } else {
                        Toast.makeText(Addroomtype_Activity.this, "Failed to Add Room Type: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter a room type", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRoomTypes() {
        Query query = databaseReference.orderByChild("type");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomTypeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RoomType roomType = snapshot.getValue(RoomType.class);
                    roomTypeList.add(roomType);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Addroomtype_Activity.this, "Failed to load room types: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
