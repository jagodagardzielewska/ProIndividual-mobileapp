package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proindividual.models.Player;

public class RegisterPlayer3 extends AppCompatActivity {

    EditText weightEditText, birthEditText,heightEditText;
    Button next_button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player3);

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        birthEditText = findViewById(R.id.birthEditText);
        next_button3 = findViewById(R.id.next_button3);

        Player player = (Player) getIntent().getSerializableExtra("player");

        next_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.setWeight(weightEditText.getText().toString().trim());
                player.setHeight(heightEditText.getText().toString().trim());
                player.setBirth(birthEditText.getText().toString().trim());

                Intent intent = new Intent(RegisterPlayer3.this, RegisterPlayer4.class);
                intent.putExtra("player", player);
                startActivity(intent);
            }
        });
        }
    }
