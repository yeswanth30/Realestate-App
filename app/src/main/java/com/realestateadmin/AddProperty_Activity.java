package com.realestateadmin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.realestateadmin.Models.PropertyType;
import android.content.Intent;
import android.net.Uri;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.realestateadmin.Adapters.PropertyTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class AddProperty_Activity extends AppCompatActivity {

    private DatabaseReference propertiesRef;
    private StorageReference storageRef;

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText propertyTypeEditText;
    private ImageView selectedImageView;
    private Uri imageUri;

    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private PropertyTypeAdapter adapter;
    private List<PropertyType> propertyTypeList;

    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        propertiesRef = database.getReference("Property_Types");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("property_images");

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycleview);

        propertyTypeEditText = findViewById(R.id.propertyTypeEditText);
        selectedImageView = findViewById(R.id.selectedImageView);
        Button addButton = findViewById(R.id.addButton);
        Button chooseImageButton = findViewById(R.id.chooseImageButton);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProperty_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String propertyType = propertyTypeEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(propertyType) && imageUri != null) {
                    uploadImageAndAddProperty(propertyType);
                } else {
                    Toast.makeText(AddProperty_Activity.this, "Please enter a property type and select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        propertyTypeList = new ArrayList<>();
        adapter = new PropertyTypeAdapter(propertyTypeList);
        recyclerView.setLayoutManager(new SingleItemLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchPropertyTypes();
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
            selectedImageView.setVisibility(View.VISIBLE);
            selectedImageView.setImageURI(imageUri);
        }
    }


    private void uploadImageAndAddProperty(final String propertyType) {
        progressBar.setVisibility(View.VISIBLE);

        if (imageUri != null) {
            StorageReference imageRef = storageRef.child(System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    addPropertyType(propertyType, imageUrl);
                                    progressBar.setVisibility(View.GONE);
                                    resetFields();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProperty_Activity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void addPropertyType(String propertyType, String imageUrl) {
        String propertyId = propertiesRef.push().getKey();
        if (propertyId != null) {
            PropertyType property = new PropertyType(propertyId, propertyType, imageUrl);

            propertiesRef.child(propertyId).setValue(property)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddProperty_Activity.this, "Property type added successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProperty_Activity.this, "Failed to add property type", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void resetFields() {
        propertyTypeEditText.setText("");
        selectedImageView.setImageURI(null);
        selectedImageView.setVisibility(View.GONE);
    }

    private void fetchPropertyTypes() {
        progressBar.setVisibility(View.VISIBLE);
        propertiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                propertyTypeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PropertyType propertyType = snapshot.getValue(PropertyType.class);
                    propertyTypeList.add(propertyType);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddProperty_Activity.this, "Failed to retrieve property types", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
