package com.realestateadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserEditProfile_Activity extends AppCompatActivity {

    EditText usernameEditText, emailEditText, phoneEditText,bioEditText,addressEditText,lastnameeittext,firstnameeittext;
    ImageView userImageView,back;
    String userId;
    Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usereditprofile_activity);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        userImageView = findViewById(R.id.userImageView);
        bioEditText = findViewById(R.id.bioEditText);
        addressEditText = findViewById(R.id.addressEditText);
        lastnameeittext = findViewById(R.id.lastnameeittext);
        firstnameeittext = findViewById(R.id.firstnameeittext);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserEditProfile_Activity.this, Allusers_Activity.class);
                startActivity(intent);
            }
        });


        userId = getIntent().getStringExtra("userId");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String lastname = dataSnapshot.child("lastname").getValue(String.class);
                    String bio = dataSnapshot.child("bio").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String firstname = dataSnapshot.child("firstname").getValue(String.class);

                    String imageUrl = dataSnapshot.child("imageurl").getValue(String.class);

                    usernameEditText.setText(username);
                    emailEditText.setText(email);
                    phoneEditText.setText(phone);
                    lastnameeittext.setText(lastname);
                    firstnameeittext.setText(firstname);
                    bioEditText.setText(bio);
                    addressEditText.setText(address);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get().load(imageUrl).into(userImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                userImageView.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onSaveButtonClick(View view) {
        // Update user details locally
        String updatedUsername = usernameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedBio = bioEditText.getText().toString();
        String updatedLastName = lastnameeittext.getText().toString();
        String updatedFirstName = firstnameeittext.getText().toString();
        String updatedAddress = addressEditText.getText().toString();

        // Update UI with the new user details
        usernameEditText.setText(updatedUsername);
        emailEditText.setText(updatedEmail);
        phoneEditText.setText(updatedPhone);
        bioEditText.setText(updatedBio);
        lastnameeittext.setText(updatedLastName);
        firstnameeittext.setText(updatedFirstName);
        addressEditText.setText(updatedAddress);

        // Optionally, update user image if selected
        if (imageUri != null) {
            uploadImageToFirebase(imageUri);
        } else {
            // If no image selected, finish the activity
            finish();
        }

        // Update user details in the Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.child("username").setValue(updatedUsername);
        userRef.child("email").setValue(updatedEmail);
        userRef.child("phone").setValue(updatedPhone);
        userRef.child("bio").setValue(updatedBio);
        userRef.child("lastname").setValue(updatedLastName);
        userRef.child("firstname").setValue(updatedFirstName);
        userRef.child("address").setValue(updatedAddress);

        // Inside onSaveButtonClick method after updating user details
        setResult(RESULT_OK);
        finish();

    }


    // Method to upload image to Firebase Storage
    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("user_images/" + userId);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get download URL
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update user image URL in Firebase Database
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        userRef.child("imageurl").setValue(uri.toString());
                        finish();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }
}
