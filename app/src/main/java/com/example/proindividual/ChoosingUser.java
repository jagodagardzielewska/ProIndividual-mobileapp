package com.example.proindividual;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoosingUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_user);

        Button buttonPlayer = findViewById(R.id.player_button);
        Button buttonCoach = findViewById(R.id.coach_button);

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