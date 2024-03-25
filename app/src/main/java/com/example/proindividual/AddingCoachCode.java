package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddingCoachCode extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText codeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_coach_code);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        codeEditText = findViewById(R.id.code_editText);
        ImageButton backButton = findViewById(R.id.backbtn);
        Button confirmButton = findViewById(R.id.button);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddingCoachCode.this, PlayerProfile.class);
            startActivity(intent);
        });
        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddingCoachCode.this, PlayerProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinCoachWithCode(codeEditText.getText().toString());
            }
        });
    }

    private void joinCoachWithCode(String inviteCode) {
        mDatabase.child("inviteCodes").child(inviteCode).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                long expirationTime = task.getResult().child("expirationTime").getValue(Long.class);
                if (System.currentTimeMillis() <= expirationTime) {

                    String coachUserId = task.getResult().child("userId").getValue(String.class);
                    String playerUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                    mDatabase.child("CoachPlayers").child(coachUserId).child("playerUserIds").push().setValue(playerUserId)
                            .addOnSuccessListener(aVoid -> Toast.makeText(AddingCoachCode.this, "Dołączyłeś do trenera.", Toast.LENGTH_SHORT).show());


                    mDatabase.child("inviteCodes").child(inviteCode).removeValue();
                } else {

                    Toast.makeText(this, "Kod wygasł.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
