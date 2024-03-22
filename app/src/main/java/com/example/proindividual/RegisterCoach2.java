package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proindividual.models.Coach;
import com.example.proindividual.models.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterCoach2 extends AppCompatActivity {
    EditText nameEditText, surnameEditText;
    Button register_button2;
    TextView namesError;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_coach2);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        register_button2 = findViewById(R.id.register_button2);
        namesError = findViewById(R.id.namesError);

        namesError.setVisibility(View.INVISIBLE);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                namesError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        nameEditText.addTextChangedListener(textWatcher);
        surnameEditText.addTextChangedListener(textWatcher);



        register_button2.setOnClickListener(v -> {
            boolean isNameFilled = !nameEditText.getText().toString().trim().isEmpty();
            boolean isSurnameFilled = !surnameEditText.getText().toString().trim().isEmpty();

            if (!isNameFilled || !isSurnameFilled) {
                namesError.setVisibility(View.VISIBLE);
            } else {
                Coach coach = (Coach) getIntent().getSerializableExtra("coach");
                coach.setName(nameEditText.getText().toString().trim());
                coach.setSurname(surnameEditText.getText().toString().trim());

                registerUser(coach);
            }
        });
    }
    private void registerUser(Coach coach) {
        mAuth.createUserWithEmailAndPassword(coach.getEmail(), coach.getPassword())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = mDatabase.child("users").child(userId);
                        currentUserDb.setValue(coach).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(RegisterCoach2.this, "Rejestracja pomyślna", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterCoach2.this, SuccessfulRegistration.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterCoach2.this, "Nie udało się zapisać danych użytkownika", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterCoach2.this, "Rejestracja nieudana: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}