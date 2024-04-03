package com.realestateadmin;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.EnquiryAdapter;
import com.realestateadmin.Models.EnquiryModel;
import java.util.ArrayList;
import java.util.List;

public class EnquiredProperties_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EnquiryAdapter adapter;
    private DatabaseReference databaseReference;


    private  DatabaseReference data;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquired_properties);


        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnquiredProperties_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        data = FirebaseDatabase.getInstance().getReference("message");


        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new SingleItemLinearLayoutManager(this));

        adapter = new EnquiryAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("enquired");

        fetchEnquiriesFromFirebase();
    }

    private void fetchEnquiriesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<EnquiryModel> enquiryList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EnquiryModel enquiry = snapshot.getValue(EnquiryModel.class);
                    enquiryList.add(enquiry);
                }
                adapter = new EnquiryAdapter(EnquiredProperties_Activity.this, enquiryList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EnquiredProperties", "onCancelled: " + databaseError.getMessage());
            }

        });
    }



    private void fetchmessages() {
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<EnquiryModel>  enquirylist = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    EnquiryModel mod = snapshot1.getValue(EnquiryModel.class);
                    enquirylist.add(mod);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
