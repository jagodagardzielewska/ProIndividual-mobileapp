package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ChoosingUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_user);

        Button buttonPlayer = findViewById(R.id.player_button);
        Button buttonCoach = findViewById(R.id.coach_button);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChoosingUser.this, LoginActivity.class);
            startActivity(intent);
        });

        buttonPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosingUser.this, RegisterPlayer.class);
                intent.putExtra("ROLE", "player");
                startActivity(intent);

            }
        });

        buttonCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosingUser.this, RegisterCoach.class);
                intent.putExtra("ROLE", "coach");
                startActivity(intent);
            }
        });
    }
}