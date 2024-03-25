package com.example.proindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        TextView forgotPasswordTextView = findViewById(R.id.textView2);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
            }
        });


        loginButton.setOnClickListener(view -> loginUser());
        createAccButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ChoosingUser.class)));
    }

    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resetowanie hasła");

        final EditText inputEmail = new EditText(this);
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inputEmail.setHint("Wpisz swój e-mail");
        builder.setView(inputEmail);

        // Ustaw przyciski
        builder.setPositiveButton("Resetuj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = inputEmail.getText().toString().trim();
                resetPassword(email);
            }
        });
        builder.setNegativeButton("Anuluj", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void resetPassword(String email) {
        if(email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Wpisz swój adres e-mail.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Instrukcje resetowania hasła zostały wysłane na e-mail.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Wystąpił problem podczas resetowania hasła.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                Intent intent;
                if ("player".equals(role)) {
                    intent = new Intent(LoginActivity.this, PlayerMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if ("coach".equals(role)) {
                    intent = new Intent(LoginActivity.this, CoachMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Role not found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Failed to fetch user role: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
