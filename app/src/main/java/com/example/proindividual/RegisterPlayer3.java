package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proindividual.models.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPlayer3 extends AppCompatActivity {

    private EditText weightEditText, birthEditText, heightEditText;
    private Button register_button;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private boolean isWeightValid(String weightString) {
        try {
            int weight = Integer.parseInt(weightString);
            return weight >= 30 && weight <= 150;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isHeightValid(String heightString) {
        try {
            int height = Integer.parseInt(heightString);
            return height >= 130 && height <= 210;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBirthDateValid(String birthDate) {
        return birthDate.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player3);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        birthEditText = findViewById(R.id.birthEditText);
        register_button = findViewById(R.id.register_button);



        Player player = (Player) getIntent().getSerializableExtra("player");

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setWeight(weightEditText.getText().toString().trim());
                player.setHeight(heightEditText.getText().toString().trim());
                player.setBirth(birthEditText.getText().toString().trim());

                String weight = weightEditText.getText().toString().trim();
                String height = heightEditText.getText().toString().trim();
                String birth = birthEditText.getText().toString().trim();

                TextView weightError = findViewById(R.id.weightError);
                TextView heightError = findViewById(R.id.heightError);
                TextView birthError = findViewById(R.id.birthError);

                weightError.setVisibility(View.INVISIBLE);
                heightError.setVisibility(View.INVISIBLE);
                birthError.setVisibility(View.INVISIBLE);

                boolean isValid = true;

                if (!isWeightValid(weight)) {
                    weightError.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                if (!isHeightValid(height)) {
                    heightError.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                if (!isBirthDateValid(birth)) {
                    birthError.setVisibility(View.VISIBLE);
                    isValid = false;
                }
                if (isValid) {
                    registerUser(player);
                }
            }

        });
    }

    private void registerUser(Player player) {
        mAuth.createUserWithEmailAndPassword(player.getEmail(), player.getPassword())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUserDb = mDatabase.child("users").child(userId);
                        currentUserDb.setValue(player).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(RegisterPlayer3.this, "Rejestracja pomyślna", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterPlayer3.this, SuccessfulRegistration.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterPlayer3.this, "Nie udało się zapisać danych użytkownika", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterPlayer3.this, "Rejestracja nieudana: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
