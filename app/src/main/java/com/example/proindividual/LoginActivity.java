package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, createAccButton;
    private TextView errorTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.button);
        createAccButton = findViewById(R.id.createacc_button);
        errorTextView = findViewById(R.id.textView9);

        loginButton.setOnClickListener(view -> loginUser());
        createAccButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ChoosingUser.class)));
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();


        if (email.isEmpty() || password.isEmpty()) {
            errorTextView.setText("Pola e-mail oraz hasło nie mogą być puste.");
            errorTextView.setVisibility(View.VISIBLE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                checkUserRole(user.getUid());
            } else {
                errorTextView.setText("Nieprawidłowy e-mail lub hasło.");
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void checkUserRole(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        databaseReference.child("role").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String role = String.valueOf(task.getResult().getValue());
                if ("player".equals(role)) {
                    startActivity(new Intent(LoginActivity.this, PlayerMain.class));
                } else if ("coach".equals(role)) {
                    startActivity(new Intent(LoginActivity.this, CoachMain.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Role not found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Failed to fetch user role: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
