package com.example.proindividual;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuccessfulRegistration extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_registration);
        mAuth = FirebaseAuth.getInstance();

        Button nextButton = findViewById(R.id.nextbutton);
        nextButton.setOnClickListener(v -> checkUserRole());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void checkUserRole() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            databaseReference.child("role").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String role = String.valueOf(task.getResult().getValue());
                    Intent intent;
                    if ("player".equals(role)) {
                        intent = new Intent(SuccessfulRegistration.this, PlayerMain.class);
                    } else if ("coach".equals(role)) {
                        intent = new Intent(SuccessfulRegistration.this, CoachMain.class);
                    } else {
                        Toast.makeText(SuccessfulRegistration.this, "Nie udało się pobrać roli użytkownika.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(SuccessfulRegistration.this, "Nie udało się pobrać roli użytkownika: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
