package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InvitePlayer2 extends AppCompatActivity {

    private TextView codeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_player2);

        codeTextView = findViewById(R.id.code_textView);
        ImageButton backButton = findViewById(R.id.backbtn);
        ImageButton profileButton = findViewById(R.id.profilebtn);
        Button mainpagebutton = findViewById(R.id.mainpage_button);

        String inviteCode = getIntent().getStringExtra("inviteCode");
        if (inviteCode != null) {
            codeTextView.setText(inviteCode);
        } else {

            codeTextView.setText("Błąd generowania kodu.");

        }
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(InvitePlayer2.this, InvitePlayer.class);
            startActivity(intent);
            finish();
        });


        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(InvitePlayer2.this, CoachProfile.class);
            startActivity(intent);
            finish();
        });

        mainpagebutton.setOnClickListener(v-> {
            Intent intent = new Intent(InvitePlayer2.this, CoachMain.class);
            startActivity(intent);
            finish();
        });
    }
}
