package com.example.proindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CoachTrainingView extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView titleTextView, dateTextView, categoryTextView, descriptionTextView, nameTextView;
    private String trainingId;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_training_view);

        titleTextView = findViewById(R.id.textView7);
        dateTextView = findViewById(R.id.datetextView);
        categoryTextView = findViewById(R.id.categorytextView);
        descriptionTextView = findViewById(R.id.opistextView);
        nameTextView = findViewById(R.id.nametexview);
        deleteButton = findViewById(R.id.delete_button);

        trainingId = getIntent().getStringExtra("trainingId");
        if (trainingId == null) {
            Toast.makeText(this, "Brak ID treningu.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupNavigation();


        mDatabase = FirebaseDatabase.getInstance().getReference("trainings");


        mDatabase.child(trainingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Training training = dataSnapshot.getValue(Training.class);
                if (training != null) {

                    titleTextView.setText(training.getTitle());
                    dateTextView.setText(training.getDate());
                    categoryTextView.setText(training.getCategory());
                    descriptionTextView.setText(training.getDetails());

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    usersRef.child(training.getPlayerUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            String playerName = userSnapshot.child("name").getValue(String.class);
                            String playerSurname = userSnapshot.child("surname").getValue(String.class);
                            if (playerName != null && playerSurname != null) {
                                nameTextView.setText(String.format("Zawodnik: %s %s", playerName, playerSurname));
                            } else {
                                nameTextView.setText("Zawodnik: Nieznany");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(CoachTrainingView.this, "Błąd podczas ładowania danych zawodnika.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(CoachTrainingView.this, "Trening nie istnieje.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CoachTrainingView.this, "Błąd podczas ładowania danych.", Toast.LENGTH_SHORT).show();
            }
        });


        deleteButton.setOnClickListener(view -> deleteTraining());
    }

    private void deleteTraining() {
        mDatabase.child(trainingId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CoachTrainingView.this, "Trening usunięty.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CoachTrainingView.this, "Błąd podczas usuwania treningu.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigation() {
        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> finish());

        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoachTrainingView.this, CoachProfile.class);
            startActivity(intent);
        });
    }
}
