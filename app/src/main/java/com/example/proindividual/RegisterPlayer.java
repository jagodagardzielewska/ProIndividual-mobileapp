package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proindividual.models.Player;

public class RegisterPlayer extends AppCompatActivity {

    EditText emailEditText, editTextPassword, editTextPassword2;
    Button next_button, back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player);

        back_button = findViewById(R.id.back_button);
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

                Player player = new Player();
                player.setEmail(email);
                player.setPassword(password);

                Intent intent = new Intent(RegisterPlayer.this, RegisterPlayer2.class);
                intent.putExtra("player", player);
                startActivity(intent);
            }
        });
    }
}