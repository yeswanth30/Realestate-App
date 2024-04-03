package com.realestateadmin;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Property_Fragment extends Fragment {

    TextView logout,citybutton,propertybutton,Allusers,sizebutton,salebutton,historybutton;

    ImageView menu;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.property_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        citybutton=view.findViewById(R.id.citybutton);
        Allusers =view.findViewById(R.id.Allusers);
        logout=view.findViewById(R.id.checkoutbutton);
        sizebutton =view.findViewById(R.id.sizebutton);
        salebutton =view.findViewById(R.id.salebutton);
        historybutton=view.findViewById(R.id.historybutton);


        propertybutton=view.findViewById(R.id.propertybutton);

        menu = view.findViewById(R.id.menu);
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);

        historybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Payments_Activity.class);
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

        Allusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Allusers_Activity.class);
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

      //  return view;
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