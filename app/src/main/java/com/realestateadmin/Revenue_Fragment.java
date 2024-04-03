package com.realestateadmin;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.PaymentAdapter;
import com.realestateadmin.Models.PaymentModel;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

import java.util.ArrayList;
import java.util.List;

public class Revenue_Fragment extends Fragment {

    TextView logout, citybutton, propertybutton, Allusers, sizebutton, salebutton, enquriedbutton, historybutton, totalAmountTextView, twoPercentTextView, propertiesBookedTextView;

    ImageView menu,edit;

    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private List<PaymentModel> paymentList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.revenue_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        citybutton = view.findViewById(R.id.citybutton);
        Allusers = view.findViewById(R.id.Allusers);
        logout = view.findViewById(R.id.checkoutbutton);
        sizebutton = view.findViewById(R.id.sizebutton);
        salebutton = view.findViewById(R.id.salebutton);
        enquriedbutton = view.findViewById(R.id.enquriedbutton);
        propertybutton = view.findViewById(R.id.propertybutton);
        historybutton = view.findViewById(R.id.historybutton);
        edit = view.findViewById(R.id.edit);

        totalAmountTextView = view.findViewById(R.id.totalAmountTextView);
        twoPercentTextView = view.findViewById(R.id.twoPercentTextView);
        propertiesBookedTextView = view.findViewById(R.id.propertiesBookedTextView);

        menu = view.findViewById(R.id.menu);
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_details", MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("imageurl", "");

        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.authorrr)
                .error(R.drawable.authorrr)
                .into(edit);

        historybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Payments_Activity.class);
                startActivity(intent);
            }
        });

        enquriedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EnquiredProperties_Activity.class);
                startActivity(intent);
            }
        });

        citybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Addcity_Activity.class);
                startActivity(intent);
            }
        });

        sizebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Addroomtype_Activity.class);
                startActivity(intent);
            }
        });

        salebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSaleType_Activity.class);
                startActivity(intent);
            }
        });

        propertybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProperty_Activity.class);
                startActivity(intent);
            }
        });

        Allusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Allusers_Activity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(v -> logoutUser());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewPayments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        paymentList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(paymentList, getContext());
        recyclerView.setAdapter(paymentAdapter);

        fetchPayments();





        DatabaseReference paymentsRef = FirebaseDatabase.getInstance().getReference("payments");

        // Retrieve payments data
        paymentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalAmount = 0;
                int propertiesBooked = 0;

                for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                    int paymentAmount = Integer.parseInt(paymentSnapshot.child("total_amount").getValue(String.class));

                    totalAmount += paymentAmount;

                    propertiesBooked++;
                }

                double twoPercentDouble = totalAmount * 0.02;

                totalAmountTextView.setText(" Rs." + totalAmount);

                twoPercentTextView.setText(" Rs." + twoPercentDouble);

                propertiesBookedTextView.setText(" " + propertiesBooked);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving payments data", databaseError.toException());
            }
        });
    }

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logout());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_SHORT).show();

        startActivity(intent);
        requireActivity().finish();
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
