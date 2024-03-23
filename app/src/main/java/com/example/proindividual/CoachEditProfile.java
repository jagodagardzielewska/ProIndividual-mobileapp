package com.example.proindividual;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proindividual.models.Coach;
import com.example.proindividual.models.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class CoachEditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText nameEditText, surnameEditText, emailEditText, passwordEditText;
    private ImageView prof_image;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    private Button save_button, changeProfileButton;

    private String currentProfileImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        prof_image = findViewById(R.id.profile_image);

        Button saveButton = findViewById(R.id.save_button);
        ImageButton backButton = findViewById(R.id.backbtn);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri imageUri = result.getData().getData();
                        uploadImageToFirebase(imageUri);
                    }
                }
        );

        Button changeProfileButton = findViewById(R.id.changeProfileButton);
        changeProfileButton.setOnClickListener(v -> openFileChooser());

        saveButton.setOnClickListener(v -> saveCoachData());
        backButton.setOnClickListener(v -> finish());

        loadCoachData();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void loadCoachData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Coach coach = dataSnapshot.getValue(Coach.class);
                    if (coach != null) {
                        nameEditText.setText(coach.getName());
                        surnameEditText.setText(coach.getSurname());
                        emailEditText.setText(coach.getEmail());
                        passwordEditText.getText().toString().trim();

                        if (coach.getProfileImageUrl() != null && !coach.getProfileImageUrl().isEmpty()) {
                            Glide.with(CoachEditProfile.this).load(coach.getProfileImageUrl()).into(prof_image);
                        } else {
                            prof_image.setImageResource(R.drawable.profile_image);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CoachEditProfile.this, "Failed to load coach data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveCoachData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Coach updatedCoach = new Coach (
                    emailEditText.getText().toString().trim(),
                    passwordEditText.getText().toString().trim(),
                    nameEditText.getText().toString().trim(),
                    surnameEditText.getText().toString().trim(),
                    "coach",
                    currentProfileImageUrl
            );

            mDatabase.child(userId).setValue(updatedCoach)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CoachEditProfile.this, "User data updated.", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        setResult(RESULT_OK, data);
                        finish();

                    })
                    .addOnFailureListener(e -> Toast.makeText(CoachEditProfile.this, "Failed to update user data.", Toast.LENGTH_SHORT).show());
        }


    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            StorageReference fileRef = storageReference.child("profileImages/" + UUID.randomUUID().toString());
            fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String profileImageUrl = uri.toString();
                    updateProfileImageUrl(profileImageUrl);
                });
            }).addOnFailureListener(e -> Toast.makeText(CoachEditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        }
    }
    private void updateProfileImageUrl(String imageUrl) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            this.currentProfileImageUrl = imageUrl;
            mDatabase.child(userId).child("profileImageUrl").setValue(imageUrl)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CoachEditProfile.this, "Image updated", Toast.LENGTH_SHORT).show();
                        Glide.with(CoachEditProfile.this).load(imageUrl).into(prof_image);
                    })
                    .addOnFailureListener(e -> Toast.makeText(CoachEditProfile.this, "Failed to update image", Toast.LENGTH_SHORT).show());
        }
    }
}
