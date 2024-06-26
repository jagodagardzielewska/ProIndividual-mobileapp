package com.example.proindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proindividual.models.Player;
import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTraining extends AppCompatActivity {

    private EditText titleEditText, categoryEditText, dateEditText, detailsEditText;
    private Button addButton, addReadyTrainingButton;
    private DatabaseReference mDatabase;
    private String playerId, coachId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        titleEditText = findViewById(R.id.titleEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        dateEditText = findViewById(R.id.dateEditText);
        detailsEditText = findViewById(R.id.opisEditText);
        addButton = findViewById(R.id.add_button);
        addReadyTrainingButton = findViewById(R.id.add2_button);

        playerId = getIntent().getStringExtra("playerId");
        coachId = getIntent().getStringExtra("coachId");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> finish());

        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddTraining.this, CoachProfile.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> addTraining());

        addReadyTrainingButton.setOnClickListener(v -> {
            if (playerId == null || coachId == null) {
                Toast.makeText(this, "Brak identyfikatorów.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(AddTraining.this, ReadyTrainingsListActivity.class);
            intent.putExtra("playerId", playerId);
            intent.putExtra("coachId", coachId);
            startActivity(intent);
        });

        loadPlayerDetails();
    }

    private void loadPlayerDetails() {
        DatabaseReference playerRef = mDatabase.child("players").child(playerId);
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Player player = dataSnapshot.getValue(Player.class);
                if (player != null) {
                    String fullName = player.getName() + " " + player.getSurname();
                    TextView coachTextView = findViewById(R.id.coachtexview);
                    coachTextView.setText(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddTraining.this, "Nie udało się załadować danych zawodnika.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTraining() {
        String title = titleEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String details = detailsEditText.getText().toString().trim();

        String trainingId = mDatabase.child("trainings").push().getKey();
        Training training = new Training(trainingId, coachId, playerId, title, category, date, details, false);

        mDatabase.child("trainings").child(trainingId).setValue(training).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddTraining.this, "Trening dodany pomyślnie", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddTraining.this, "Błąd podczas dodawania treningu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

