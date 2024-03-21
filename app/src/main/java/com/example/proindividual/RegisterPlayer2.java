package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proindividual.models.Player;

public class RegisterPlayer2 extends AppCompatActivity {

    EditText nameEditText, surnameEditText;
    Button next_button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player2);

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        next_button2 = findViewById(R.id.next_button2);

        Player player = (Player) getIntent().getSerializableExtra("player");
        next_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.setName(nameEditText.getText().toString().trim());
                player.setSurname(surnameEditText.getText().toString().trim());

                Intent intent = new Intent(RegisterPlayer2.this, RegisterPlayer3.class);
                intent.putExtra("player", player);
                startActivity(intent);
            }
        });
    }
}