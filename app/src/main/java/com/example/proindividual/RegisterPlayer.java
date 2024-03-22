package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proindividual.models.Player;

public class RegisterPlayer extends AppCompatActivity {

    EditText emailEditText, editTextPassword, editTextPassword2;
    Button next_button, back_button;
    TextView passwordError, emailError, passwordError2;

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean doPasswordsMatch(String password1, String password2) {
        return password1.equals(password2);
    }

    private boolean isPasswordComplex(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,40}$";
        return password.matches(passwordPattern);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player);

        back_button = findViewById(R.id.back_button);
        emailEditText = findViewById(R.id.emailEditText);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        emailError = findViewById(R.id.emailError);
        passwordError = findViewById(R.id.passwordError);
        passwordError2 = findViewById(R.id.passwordError2);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextPassword2.getText().toString().trim();
                String role = getIntent().getStringExtra("ROLE");

                boolean isValid = true;

                emailError.setVisibility(View.INVISIBLE);
                passwordError.setVisibility(View.INVISIBLE);
                passwordError2.setVisibility(View.INVISIBLE);

                if (!isEmailValid(email)) {
                    emailError.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                if (!doPasswordsMatch(password, confirmPassword)){
                    passwordError.setVisibility(View.VISIBLE);
                    isValid = false;
                }

                if (!isPasswordComplex(password)) {
                    passwordError2.setVisibility(View.VISIBLE);
                    isValid = false;
                }


                if (isValid) {
                    Player player = new Player();
                    player.setEmail(email);
                    player.setPassword(password);
                    player.setRole(role);

                    Intent intent = new Intent(RegisterPlayer.this, RegisterPlayer2.class);
                    intent.putExtra("player", player);
                    startActivity(intent);
                }

            }
        });


    }
}