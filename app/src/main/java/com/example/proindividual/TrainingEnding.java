package com.example.proindividual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proindividual.models.Training;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrainingEnding extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView titleTextView, coachTextView;
    private String trainingId;

    private EditText satisfactionEditText, effortEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_ending);

        trainingId = getIntent().getStringExtra("trainingId");

        titleTextView = findViewById(R.id.titletexview);
        coachTextView = findViewById(R.id.coachtexview);
        satisfactionEditText = findViewById(R.id.rateedittext);
        effortEditText = findViewById(R.id.rate2edittext);
        submitButton = findViewById(R.id.button);
        mDatabase = FirebaseDatabase.getInstance().getReference("trainings");

        loadTrainingAndCoachInfo();

        submitButton.setOnClickListener(view -> {
            String satisfactionRating = satisfactionEditText.getText().toString();
            String effortRating = effortEditText.getText().toString();
            saveRatings(satisfactionRating, effortRating);
        });

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingEnding.this, TrainingPlayerView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingEnding.this, PlayerProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void loadTrainingAndCoachInfo() {
        if (trainingId != null) {
            mDatabase.child(trainingId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Training training = dataSnapshot.getValue(Training.class);
                    if (training != null) {
                        titleTextView.setText(training.getTitle());

                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(training.getCoachUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("name").getValue(String.class);
                                String surname = dataSnapshot.child("surname").getValue(String.class);
                                coachTextView.setText(String.format("Trener: %s %s", name, surname));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(TrainingEnding.this, "Nie udało się pobrać danych trenera.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TrainingEnding.this, "Nie udało się pobrać danych treningu.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveRatings(String satisfactionRating, String effortRating) {
        int satisfaction = Integer.parseInt(satisfactionRating);
        int effort = Integer.parseInt(effortRating);


        DatabaseReference ratingsRef = mDatabase.child(trainingId).child("ratings");
        ratingsRef.child("satisfaction").setValue(satisfaction);
        ratingsRef.child("effort").setValue(effort).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TrainingEnding.this, "Oceny zostały zapisane.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TrainingEnding.this, "Błąd podczas zapisywania ocen.", Toast.LENGTH_SHORT).show();
            }

        });

        mDatabase.child(trainingId).child("completed").setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(TrainingEnding.this, "Trening zakończony.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TrainingEnding.this, PlayerMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(TrainingEnding.this, "Błąd podczas zapisywania zakończenia treningu.", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("trainings");
        mDatabase.child(trainingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Training training = dataSnapshot.getValue(Training.class);
                if (training != null) {
                    titleTextView.setText(training.getTitle());

                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    usersRef.child(training.getCoachUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String surname = dataSnapshot.child("surname").getValue(String.class);
                            coachTextView.setText(String.format("Trener: %s %s", name, surname));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(TrainingEnding.this, "Nie udało się pobrać danych trenera.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TrainingEnding.this, "Nie udało się pobrać danych treningu.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
