package com.example.proindividual;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proindividual.LoginActivity;
import com.example.proindividual.models.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayerProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText nameEditText, surnameEditText, emailEditText, heightEditText, weightEditText, birthEditText;
    private ActivityResultLauncher<Intent> editProfileLauncher;

    private ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        birthEditText = findViewById(R.id.birthEditText);
        profile_image = findViewById(R.id.profile_image);
        Button coachButton = findViewById(R.id.coachbutton);

        nameEditText.setEnabled(false);
        surnameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        heightEditText.setEnabled(false);
        weightEditText.setEnabled(false);
        birthEditText.setEnabled(false);

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerProfile.this, PlayerMain.class);
            startActivity(intent);
        });

        coachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfile.this, AddingCoachCode.class);
                startActivity(intent);
            }
        });

        Button changePasswordButton = findViewById(R.id.changepassword_button);
        changePasswordButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if(user != null) {
                String emailAddress = user.getEmail();
                if(emailAddress != null && !emailAddress.isEmpty()) {
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PlayerProfile.this,
                                            "Email do resetowania hasła został wysłany.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(PlayerProfile.this,
                            "Nie udało się odnaleźć adresu email użytkownika.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PlayerProfile.this,
                        "Nie zalogowano użytkownika.",
                        Toast.LENGTH_SHORT).show();
            }
        });


        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadPlayerData();
                    }
                }
        );

        Button editProfileButton = findViewById(R.id.editprofile_button);
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerProfile.this, EditProfile.class);
            editProfileLauncher.launch(intent);
        });


        loadPlayerData();

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(PlayerProfile.this, LoginActivity.class));
            finish();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, PlayerMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }



    private void loadPlayerData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Player player = dataSnapshot.getValue(Player.class);
                    if (player != null) {
                        nameEditText.setText(player.getName());
                        surnameEditText.setText(player.getSurname());
                        emailEditText.setText(player.getEmail());
                        heightEditText.setText(player.getHeight());
                        weightEditText.setText(player.getWeight());
                        birthEditText.setText(player.getBirth());

                        if (player.getProfileImageUrl() != null && !player.getProfileImageUrl().isEmpty()) {
                            Glide.with(PlayerProfile.this)
                                    .load(player.getProfileImageUrl())
                                    .into(profile_image);
                        } else {
                            profile_image.setImageResource(R.drawable.profile_image);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(PlayerProfile.this, "Failed to load user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

