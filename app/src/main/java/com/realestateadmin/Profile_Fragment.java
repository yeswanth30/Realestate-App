package com.realestateadmin;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.realestateadmin.Adapters.UserAdapter;
import com.realestateadmin.Models.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Profile_Fragment extends Fragment {

    TextView logout,citybutton,propertybutton,Allusers,sizebutton,salebutton,historybutton;

    Dialog dialog;
    private TextView Nametextview,enquriedbutton, Phonetextview, Emailtextview, Usernametextview, passwordtextview;
    private ImageView leftImageView, edit, back;
    private ProgressBar progressBar;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;

    ImageView menu;
    private DrawerLayout drawerLayout;

    private RecyclerView recyclerView;
    private UserAdapter usersAdapter;
    private List<User> userList;
    private DatabaseReference usersRef;



    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout=view.findViewById(R.id.checkoutbutton);
        citybutton=view.findViewById(R.id.citybutton);
        Allusers =view.findViewById(R.id.Allusers);
        sizebutton =view.findViewById(R.id.sizebutton);
        salebutton =view.findViewById(R.id.salebutton);
        historybutton=view.findViewById(R.id.historybutton);
        enquriedbutton = view.findViewById(R.id.enquriedbutton);
        propertybutton=view.findViewById(R.id.propertybutton);
        edit = view.findViewById(R.id.edit);
        Nametextview = view.findViewById(R.id.Nametextview);
        Phonetextview = view.findViewById(R.id.Phonetextview);
        Emailtextview = view.findViewById(R.id.Emailtextview);
        Usernametextview = view.findViewById(R.id.Usernametextview);
        passwordtextview = view.findViewById(R.id.passwordtextview);
        leftImageView = view.findViewById(R.id.leftImageView);
        progressBar = view.findViewById(R.id.progressBar);
        menu = view.findViewById(R.id.menu);
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);


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

        historybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Payments_Activity.class);
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



        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_details", MODE_PRIVATE);
                String imageUrl = sharedPreferences.getString("imageurl", "");
                showFullImageDialog(imageUrl);
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
                startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE);
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




        loadProfile();


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


//    private void showFullImageDialog(String imageUrl) {
//        dialog = new Dialog(requireContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_full_image);
//
//        ImageView fullImageView = dialog.findViewById(R.id.fullImageView);
//        Picasso.get().load(imageUrl).into(fullImageView);
//
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        dialog.show();
//    }

    private void loadProfile() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_details", MODE_PRIVATE);
        String Name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String username = sharedPreferences.getString("username", "");
        String phone = sharedPreferences.getString("phone", "");
        String password = sharedPreferences.getString("password", "");
        String imageurl = sharedPreferences.getString("imageurl", "");

        Nametextview.setText(Name);
        Phonetextview.setText(phone);
        Emailtextview.setText(email);
        Usernametextview.setText(username);
        passwordtextview.setText(password);

        if (imageurl != null && !imageurl.isEmpty()) {
            Picasso.get().load(imageurl)
                    .placeholder(R.drawable.authorrr)
                    .error(R.drawable.authorrr)
                    .into(leftImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            loadProfile();
        }
    }

    private void showFullImageDialog(String imageUrl) {
        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_full_image);

        ImageView fullImageView = dialog.findViewById(R.id.fullImageView);
        Picasso.get().load(imageUrl).into(fullImageView);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation1;

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
