package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class InvitePlayer extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_player);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageButton backButton = findViewById(R.id.backbtn);
        ImageButton profileButton = findViewById(R.id.profilebtn);
        Button generateButton = findViewById(R.id.generate_button);
        generateButton.setOnClickListener(v -> {
            String inviteCode = generateRandomCode(9);
            long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
            String userId = mAuth.getCurrentUser().getUid();


            saveInviteCode(inviteCode, userId, expirationTime);


            Intent intent = new Intent(InvitePlayer.this, InvitePlayer2.class);
            intent.putExtra("inviteCode", inviteCode);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(InvitePlayer.this, CoachProfile.class);
            startActivity(intent);
            finish();
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(InvitePlayer.this, CoachProfile.class);
            startActivity(intent);
            finish();
        });


    }

    private String generateRandomCode(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return sb.toString();
    }

    private void saveInviteCode(String inviteCode, String userId, long expirationTime) {
        Map<String, Object> inviteDetails = new HashMap<>();
        inviteDetails.put("code", inviteCode);
        inviteDetails.put("userId", userId);
        inviteDetails.put("expirationTime", expirationTime);

        mDatabase.child("inviteCodes").child(inviteCode).setValue(inviteDetails)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }
}
