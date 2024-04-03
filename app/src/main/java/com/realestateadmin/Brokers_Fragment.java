package com.realestateadmin;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realestateadmin.Adapters.BrokersAdapter;
import com.realestateadmin.Models.Broker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Brokers_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private BrokersAdapter brokersAdapter;
    private List<Broker> brokersList;
    private DatabaseReference brokersRef;

    TextView logout,citybutton,propertybutton,Allusers,sizebutton,salebutton,historybutton,enquriedbutton;

    ImageView menu,edit;
//    private DrawerLayout drawerLayout;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brokers_fragment, container, false);

        citybutton=view.findViewById(R.id.citybutton);
        Allusers =view.findViewById(R.id.Allusers);
        logout=view.findViewById(R.id.checkoutbutton);
        sizebutton =view.findViewById(R.id.sizebutton);
        salebutton =view.findViewById(R.id.salebutton);
        historybutton=view.findViewById(R.id.historybutton);
        edit=view.findViewById(R.id.edit);
        enquriedbutton = view.findViewById(R.id.enquriedbutton);



        propertybutton=view.findViewById(R.id.propertybutton);

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


        propertybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProperty_Activity.class);
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

        Allusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Allusers_Activity.class);
                startActivity(intent);
            }
        });


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
        logout.setOnClickListener(v -> logoutUser());

        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        brokersList = new ArrayList<>();
        brokersRef = FirebaseDatabase.getInstance().getReference().child("Broker");
        fetchBrokersData();
        return view;




    }

    private void fetchBrokersData() {
        brokersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                brokersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Broker broker = snapshot.getValue(Broker.class);
                    if (broker != null) {
                        brokersList.add(broker);
                    }
                }
                brokersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        brokersAdapter = new BrokersAdapter(getContext(), brokersList);
        recyclerView.setAdapter(brokersAdapter);
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

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> logout());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
