package com.example.proindividual;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

    private EditText nameEditText, surnameEditText, passwordEditText, emailEditText, heightEditText, weightEditText, birthEditText;
    private ActivityResultLauncher<Intent> editProfileLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("players");


        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        birthEditText = findViewById(R.id.birthEditText);

        nameEditText.setEnabled(false);
        surnameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        heightEditText.setEnabled(false);
        weightEditText.setEnabled(false);
        birthEditText.setEnabled(false);

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerProfile.this, PlayerMain.class);
            startActivity(intent);
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

    private void loadPlayerData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Player player = dataSnapshot.getValue(Player.class);
                    if (player != null) {
                        nameEditText.setText(player.getName());
                        surnameEditText.setText(player.getSurname());
                        passwordEditText.setText(player.getPassword());
                        emailEditText.setText(player.getEmail());
                        heightEditText.setText(player.getHeight());
                        weightEditText.setText(player.getWeight());
                        birthEditText.setText(player.getBirth());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(PlayerProfile.this, "Błąd wczytywania danych: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}


