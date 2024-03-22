package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.proindividual.models.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText nameEditText, surnameEditText, emailEditText, heightEditText, weightEditText, birthEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        birthEditText = findViewById(R.id.birthEditText);


        loadUserData();

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> {
            finish();
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            saveUserData();
        });
    }

    private void loadUserData() {
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
                        passwordEditText.setText(player.getPassword());
                        emailEditText.setText(player.getEmail());
                        heightEditText.setText(player.getHeight());
                        weightEditText.setText(player.getWeight());
                        birthEditText.setText(player.getBirth());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(EditProfile.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Player updatedPlayer = new Player(
                    emailEditText.getText().toString().trim(),
                    passwordEditText.getText().toString().trim(),
                    nameEditText.getText().toString().trim(),
                    surnameEditText.getText().toString().trim(),
                    heightEditText.getText().toString().trim(),
                    weightEditText.getText().toString().trim(),
                    birthEditText.getText().toString().trim(),
                    "player"
            );


            mDatabase.child(userId).setValue(updatedPlayer)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditProfile.this, "User data updated.", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        setResult(RESULT_OK, data);
                        finish();

                    })
                    .addOnFailureListener(e -> Toast.makeText(EditProfile.this, "Failed to update user data.", Toast.LENGTH_SHORT).show());
        }
    }
}
