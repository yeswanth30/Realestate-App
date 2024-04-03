package com.realestateadmin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserDetails_Activity extends AppCompatActivity  {

    ImageView edit, back, verificationStatusImageView;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        verificationStatusImageView = findViewById(R.id.verificationStatusImageView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetails_Activity.this, Allusers_Activity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetails_Activity.this, UserEditProfile_Activity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 1);
            }
        });

        userId = getIntent().getStringExtra("userId");

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String firstname = dataSnapshot.child("firstname").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String city = dataSnapshot.child("city").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);
                    String lastname = dataSnapshot.child("lastname").getValue(String.class);
                    String image = dataSnapshot.child("imageurl").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);

                    TextView usernameTextView = findViewById(R.id.usernameTextView);
                    TextView emailTextView = findViewById(R.id.emailTextView);
                    TextView Phonetextview = findViewById(R.id.Phonetextview);
                    TextView cityTextView = findViewById(R.id.cityTextView);
                    TextView addressTextView = findViewById(R.id.addressTextView);
                    TextView bioTextView = findViewById(R.id.bioTextView);
                    TextView lastnametextview = findViewById(R.id.lastnametextview);
                    ImageView profileImageView = findViewById(R.id.leftImageView);

                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    Phonetextview.setText(phone);
                    cityTextView.setText(city);
                    addressTextView.setText(address);
                    bioTextView.setText(bio);
                    lastnametextview.setText(firstname +" " + lastname);
                    Picasso.get().load(image).into(profileImageView);

                    if (status.equals("1")) {
                        verificationStatusImageView.setImageResource(R.drawable.verified);
                    } else {
                        verificationStatusImageView.setImageResource(R.drawable.delete);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                refreshUserDetails();
            }
        }
    }

    private void refreshUserDetails() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String firstname = dataSnapshot.child("firstname").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String city = dataSnapshot.child("city").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);
                    String image = dataSnapshot.child("imageurl").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);
                    String lastname = dataSnapshot.child("lastname").getValue(String.class);

                    TextView usernameTextView = findViewById(R.id.usernameTextView);
                    TextView emailTextView = findViewById(R.id.emailTextView);
                    TextView Phonetextview = findViewById(R.id.Phonetextview);
                    TextView cityTextView = findViewById(R.id.cityTextView);
                    TextView addressTextView = findViewById(R.id.addressTextView);
                    TextView bioTextView = findViewById(R.id.bioTextView);
                    ImageView profileImageView = findViewById(R.id.profileImageView);
                    TextView lastnametextview = findViewById(R.id.lastnametextview);


                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    Phonetextview.setText(phone);
                    cityTextView.setText(city);
                    addressTextView.setText(address);
                    bioTextView.setText(bio);
                    lastnametextview.setText(firstname +" " + lastname);
                    Picasso.get().load(image).into(profileImageView);


                    if (status.equals("1")) {
                        verificationStatusImageView.setImageResource(R.drawable.verified);
                    } else {
                        verificationStatusImageView.setImageResource(R.drawable.delete);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}

