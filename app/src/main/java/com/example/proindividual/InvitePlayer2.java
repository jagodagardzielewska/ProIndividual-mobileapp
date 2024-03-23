package com.example.proindividual;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InvitePlayer2 extends AppCompatActivity {

    private TextView codeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_player2);

        codeTextView = findViewById(R.id.code_textView);

        String inviteCode = getIntent().getStringExtra("inviteCode");
        if (inviteCode != null) {
            codeTextView.setText(inviteCode);
        } else {

            codeTextView.setText("Błąd generowania kodu.");
        }
    }
}
