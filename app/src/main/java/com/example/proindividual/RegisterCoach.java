package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proindividual.models.Coach;

public class RegisterCoach extends AppCompatActivity {
    EditText emailEditText, editTextPassword, editTextPassword2;
    Button next_button, back_button;
    TextView emailError, passwordError, passwordError2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_coach);

        back_button = findViewById(R.id.back_button);
        emailEditText = findViewById(R.id.emailEditText);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        passwordError2 = findViewById(R.id.passwordError2);


        emailError.setVisibility(View.INVISIBLE);
        passwordError.setVisibility(View.INVISIBLE);
        passwordError2.setVisibility(View.INVISIBLE);

        back_button.setOnClickListener(v -> finish());

        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(v -> {

            String email = emailEditText.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextPassword2.getText().toString().trim();
            String role = getIntent().getStringExtra("ROLE");

            boolean isValid = true;


            emailError.setVisibility(View.INVISIBLE);
            passwordError.setVisibility(View.INVISIBLE);
            passwordError2.setVisibility(View.INVISIBLE);

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailError.setVisibility(View.VISIBLE);
                isValid = false;
            }

            if (!password.equals(confirmPassword)) {
                passwordError.setVisibility(View.VISIBLE);
                isValid = false;
            }

            if (!password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,40}$")) {
                passwordError2.setVisibility(View.VISIBLE);
                isValid = false;
            }

            if (isValid) {
                Coach coach = new Coach();
                coach.setEmail(email);
                coach.setPassword(password);
                coach.setRole(role);

                Intent intent = new Intent(RegisterCoach.this, RegisterCoach2.class);
                intent.putExtra("coach", coach);
                startActivity(intent);
            }
        });
    }
}
