package com.example.proindividual;

import android.app.Activity;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CoachProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText nameEditText, surnameEditText, emailEditText,passwordEditText;
    private ImageView profileImage;

    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        profileImage = findViewById(R.id.profile_image);
        passwordEditText = findViewById(R.id.passwordEditText);


        nameEditText.setEnabled(false);
        surnameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoachProfile.this, CoachMain.class);
            startActivity(intent);
        });

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadCoachData();
                    }
                }
        );


        Button editProfileButton = findViewById(R.id.editprofile_button);
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoachProfile.this, CoachEditProfile.class);
            editProfileLauncher.launch(intent);
        });

        Button inviteButton = findViewById(R.id.invite_button);
        inviteButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoachProfile.this, InvitePlayer.class);
            startActivity(intent);
        });


        loadCoachData();



        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(CoachProfile.this, LoginActivity.class));
            finish();
        });
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
                        passwordEditText.setText(coach.getPassword());
                        emailEditText.setText(coach.getEmail());

                        if (coach.getProfileImageUrl() != null && !coach.getProfileImageUrl().isEmpty()) {
                            Glide.with(CoachProfile.this).load(coach.getProfileImageUrl()).into(profileImage);
                        } else {
                            profileImage.setImageResource(R.drawable.profile_image);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CoachProfile.this, "Failed to load coach data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
