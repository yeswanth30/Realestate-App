package com.realestateadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.realestateadmin.Adapters.PaymentAdapter;
import com.realestateadmin.Models.PaymentModel;

import java.util.ArrayList;
import java.util.List;

public class Payments_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private List<PaymentModel> paymentList;

    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payments_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewPayments);
        recyclerView.setLayoutManager(new SingleItemLinearLayoutManager(this));


        paymentList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(paymentList, this);
        recyclerView.setAdapter(paymentAdapter);

        fetchPayments();
    }

    private void fetchPayments() {
        DatabaseReference paymentsRef = FirebaseDatabase.getInstance().getReference().child("payments");
        paymentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                paymentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PaymentModel payment = snapshot.getValue(PaymentModel.class);
                    paymentList.add(payment);
                }
                paymentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
